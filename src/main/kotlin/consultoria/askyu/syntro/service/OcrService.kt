package consultoria.askyu.syntro.service

import consultoria.askyu.syntro.dominio.NotaFiscal
import consultoria.askyu.syntro.dominio.Temp
import consultoria.askyu.syntro.dto.TempDto
import consultoria.askyu.syntro.repository.NotaFiscalRepository
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import net.sourceforge.tess4j.Tesseract
import net.sourceforge.tess4j.TesseractException
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.rendering.PDFRenderer
import org.modelmapper.ModelMapper
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.awt.image.BufferedImage
import java.io.InputStream
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.UUID
import kotlin.collections.forEach

@Service
class OcrService(
    @Value("\${tesseract.ocr-path}") private val tessDataPath: String,
    private val notaFiscalService: NotaFiscalService,
    private val tempService: TempService,
    private val usuarioService: UsuarioService,
    private val s3Service: S3Service
) {

    fun processarNotaFiscal(pdfInputStream: InputStream, uuid: String, idUsuario: Int): NotaFiscal {
        tempService.add(Temp(null, uuid, "Um processamento de nota fiscal", idUsuario))
        println("Iniciando OCR inteligente da nota fiscal...")
        val texto = extrairTextoPdf(pdfInputStream)
        println("Texto extraído: ${texto.length} caracteres")
        val nota = inferirCamposNotaFiscal(texto)
        validarCamposObrigatorios(nota)
        nota.idUsuario = idUsuario
        nota.idEmpresa = usuarioService.buscarPorId(idUsuario).idEmpresa
        println("OCR concluído com sucesso")
        tempService.deletar(uuid)
        return nota
    }

    @OptIn(DelicateCoroutinesApi::class)
    fun processarNotaFiscalv2(pdfInputStream: InputStream, uuid: String, idUsuario: Int): String {
        GlobalScope.launch(Dispatchers.IO) {
            launch {
                processarNotaFiscal(pdfInputStream, uuid, idUsuario)
            }
        }

        return uuid
    }

    @OptIn(DelicateCoroutinesApi::class)
    fun processarNotasFiscais(files: List<MultipartFile>, idUsuario: Int): List<String> {
        val listaUUIDs = mutableListOf<String>()

        GlobalScope.launch(Dispatchers.IO) {
            files.forEach { file ->
                val uuid = UUID.randomUUID().toString()
                listaUUIDs.add(uuid)

                launch {
                    processarNotaFiscal(file.inputStream, uuid, idUsuario)
                }
            }
        }

        return listaUUIDs
    }

    private fun extrairTextoPdf(pdfInputStream: InputStream): String {
        PDDocument.load(pdfInputStream).use { doc ->
            val renderer = PDFRenderer(doc)
            val sb = StringBuilder()
            println("Número de páginas: ${doc.numberOfPages}")
            for (i in 0 until doc.numberOfPages) {
                val image: BufferedImage = renderer.renderImageWithDPI(i, 300f)
                sb.append(extrairTextoImagem(image))
            }
            print(sb.toString())
            return sb.toString()
        }
    }

    private fun extrairTextoImagem(image: BufferedImage): String {
        val tesseract = Tesseract()
        tesseract.setDatapath(tessDataPath)
        tesseract.setLanguage("por")
        return try {
            tesseract.doOCR(image)
        } catch (e: TesseractException) {
            println("Erro no OCR: ${e.message}")
            ""
        }
    }

    private fun inferirCamposNotaFiscal(texto: String): NotaFiscal {
        val nota = NotaFiscal()

        // Normaliza linhas (remove múltiplos espaços / NBSP etc)
        val rawLines = texto.lines()
            .map { it.replace("\u00A0", " ").replace(Regex("\\s+"), " ").trim() }
            .filter { it.isNotEmpty() }

        // Precompila regex úteis
        val cnpjRegex = Regex("\\d{2}\\.\\d{3}\\.\\d{3}/\\d{4}-\\d{2}")
        val moneyRegex = Regex("R?\\$?\\s*([0-9]{1,3}(?:[.,][0-9]{3})*(?:[.,][0-9]{2}))")
        val multiDateTimeRegex = Regex("(\\d+)\\D*(\\d{2}/\\d{2}/\\d{4}).*?(\\d{2}/\\d{2}/\\d{4}).*?(\\d{2}:\\d{2}:\\d{2})")
        val dateOnlyRegex = Regex("\\d{2}/\\d{2}/\\d{4}")
        val nbsRegex = Regex("NBS\\s*[:\\-]?\\s*(\\d+)", RegexOption.IGNORE_CASE)

        // Date formats com timezone explícito (evita deslocamentos inesperados)
        val sdfDate = SimpleDateFormat("dd/MM/yyyy")
        sdfDate.timeZone = java.util.TimeZone.getTimeZone("America/Sao_Paulo")
        val sdfDateTime = SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
        sdfDateTime.timeZone = java.util.TimeZone.getTimeZone("America/Sao_Paulo")

        // 1) coleta todos os CNPJs em ordem de aparição (usa depois)
        val foundCnpjs = mutableListOf<String>()
        rawLines.forEach { linha ->
            cnpjRegex.findAll(linha).forEach { foundCnpjs.add(it.value) }
        }
        if (foundCnpjs.size >= 1) nota.cnpjEmitente = foundCnpjs[0]

        // 2) percorre linhas com índice para poder olhar a linha seguinte
        for ((i, linha) in rawLines.withIndex()) {
            val lower = linha.lowercase()

            // --- Número da NFS-e + Competência + Data/Hora (cabeçalho em uma linha, valores na próxima)
            if (lower.contains("número da nfs-e") && i + 1 < rawLines.size) {
                val valores = rawLines[i + 1].replace("[^0-9/: \\-:]".toRegex(), " ")
                val m = multiDateTimeRegex.find(valores)
                if (m != null) {
                    // grupo 1 = número, 2 = competência (data), 3 = data emissão, 4 = hora emissão
                    nota.numeroIdentificador = m.groupValues[1]
                    try {
                        nota.dataVencimento = Timestamp(sdfDate.parse(m.groupValues[2]).time) // competência
                    } catch (_: Exception) { /* ignore */ }
                    try {
                        val dt = sdfDateTime.parse("${m.groupValues[3]} ${m.groupValues[4]}")
                        nota.dataEmissao = Timestamp(dt.time)
                    } catch (_: Exception) { /* ignore */ }
                } else {
                    // fallback: se não casar perfeitamente, tenta extrair número + primeira data + hora
                    val number = Regex("\\d+").find(valores)?.value
                    val firstDate = dateOnlyRegex.find(valores)?.value
                    val time = Regex("\\d{2}:\\d{2}:\\d{2}").find(valores)?.value
                    if (number != null) nota.numeroIdentificador = number
                    if (firstDate != null) {
                        try { nota.dataVencimento = Timestamp(sdfDate.parse(firstDate).time) } catch (_: Exception) {}
                    }
                    if (firstDate != null && time != null) {
                        try {
                            nota.dataEmissao = Timestamp(sdfDateTime.parse("$firstDate $time").time)
                        } catch (_: Exception) {}
                    }
                }
            }

            // --- Valor do Serviço (cabeçalho -> valor na linha seguinte)
            if (lower.contains("valor do serviço")) {
                val possivel = rawLines.getOrNull(i + 1) ?: linha
                val mm = moneyRegex.find(possivel) ?: moneyRegex.find(linha)
                mm?.let {
                    nota.valorTotal = it.groupValues[1].replace(".", "").replace(",", ".").toDoubleOrNull()
                }
            }

            // --- Valor Total da NFS-e (resumo) - às vezes está 1-2 linhas abaixo
            if (lower.contains("valor total da nfs-e") && nota.valorTotal == null) {
                // tenta as próximas 3 linhas por segurança
                for (j in 1..3) {
                    val possivel = rawLines.getOrNull(i + j) ?: ""
                    val mm = moneyRegex.find(possivel)
                    if (mm != null) {
                        nota.valorTotal = mm.groupValues[1].replace(".", "").replace(",", ".").toDoubleOrNull()
                        break
                    }
                }
            }

            // --- Descrição do Serviço (cabeçalho -> conteúdo na próxima linha)
            if (lower.contains("descrição do serviço")) {
                val next = rawLines.getOrNull(i + 1)?.trim()
                if (!next.isNullOrBlank()) {
                    nota.descricao = next
                } else {
                    // fallback: se a mesma linha tiver algo após dois-pontos/hífen
                    val after = linha.substringAfter(":", "").substringAfter("-", "").trim()
                    if (after.isNotEmpty()) nota.descricao = after
                }
            } else if (lower.startsWith("descrição") && nota.descricao.isNullOrBlank()) {
                val after = linha.substringAfter(":", "").substringAfter("-", "").trim()
                if (after.isNotEmpty() && !after.equals("descrição do serviço", ignoreCase = true)) {
                    nota.descricao = after
                }
            }

            // --- Informações complementares (cabeçalho -> próxima linha)
            if ((lower.contains("informações complementares") || lower.contains("informacao complementar"))) {
                val next = rawLines.getOrNull(i + 1)?.trim()
                if (!next.isNullOrBlank()) {
                    // tenta extrair NBS se existir
                    val m = nbsRegex.find(next)
                    nota.informacaoAdicional = m?.groupValues?.get(1) ?: next
                }
            }

            // --- Outros valores (dota fallback: se ainda não pegou valorTotal, captura primeiro R$ válido que aparecer após as seções de valores)
            if (nota.valorTotal == null) {
                val mm = moneyRegex.find(linha)
                mm?.let {
                    nota.valorTotal = it.groupValues[1].replace(".", "").replace(",", ".").toDoubleOrNull()
                }
            }

            // --- Exemplos de campos já existentes (mantive para não perder dados antigos)
            // Base de cálculo, INSS, IPTU, Valor Deduções, Aliquota, Tipo de Nota, Contrato
            if (linha.contains("Cálculo", ignoreCase = true)) {
                moneyRegex.find(linha)?.let { nota.baseCalculo = it.groupValues[1].replace(".", "").replace(",", ".").toDoubleOrNull() }
            }
            if (linha.contains("INSS", ignoreCase = true)) {
                moneyRegex.find(linha)?.let { nota.valorInss = it.groupValues[1].replace(".", "").replace(",", ".").toDoubleOrNull() }
            }
            if (linha.contains("IPTU", ignoreCase = true)) {
                moneyRegex.find(linha)?.let { nota.creditoIptu = it.groupValues[1].replace(".", "").replace(",", ".").toDoubleOrNull() }
            }
            if (linha.contains("Valor Deducoes", ignoreCase = true) || linha.contains("Deduções", ignoreCase = true)) {
                moneyRegex.find(linha)?.let { nota.valorDeducoes = it.groupValues[1].replace(".", "").replace(",", ".").toDoubleOrNull() }
            }
            if (linha.contains("Alíquota", ignoreCase = true) || linha.contains("Aliquota", ignoreCase = true)) {
                val a = Regex("(\\d+(?:[.,]\\d+)?)%").find(linha)?.groupValues?.get(1)
                a?.let { nota.aliquota = it.replace(",", ".").toDoubleOrNull() }
            }
            if (linha.contains("Contrato", ignoreCase = true)) {
                Regex("\\d+").find(linha)?.value?.toIntOrNull()?.let { nota.idContrato = it }
            }
        }

        // salva e debug
        notaFiscalService.cadastrar(nota)
        println("OCR EXTRAIDO -> numero=${nota.numeroIdentificador} valor=${nota.valorTotal} emissao=${nota.dataEmissao} venc=${nota.dataVencimento} descricao=${nota.descricao} cnpjEmitente=${nota.cnpjEmitente} info=${nota.informacaoAdicional}")
        return nota
    }


    private fun validarCamposObrigatorios(nota: NotaFiscal) {
        val erros = mutableListOf<String>()
        if (nota.numeroIdentificador == null) erros.add("Número Identificador não encontrado")
        if (nota.valorTotal == null) erros.add("Valor Total não encontrado")
        if (nota.dataEmissao == null) erros.add("Data de Emissão não encontrada")
        if (erros.isNotEmpty()) {
            throw IllegalArgumentException("Campos obrigatórios faltando: ${erros.joinToString(", ")}")
        }
    }
}


