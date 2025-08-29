package consultoria.askyu.syntro.service

import consultoria.askyu.syntro.dominio.NotaFiscal
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
    private val tessDataPath: String = "C:/Tesseract-OCR/tessdata",
    private val notaFiscalService: NotaFiscalService,
    private val tempService: TempService
) {

    fun processarNotaFiscal(pdfInputStream: InputStream, uuid: String, idUsuario: Int): NotaFiscal {
        tempService.add(TempDto(uuid, "Um processamento de nota fiscal", idUsuario))
        println("Iniciando OCR inteligente da nota fiscal...")
        val texto = extrairTextoPdf(pdfInputStream)
        println("Texto extraído: ${texto.length} caracteres")
        val nota = inferirCamposNotaFiscal(texto)
        validarCamposObrigatorios(nota)
        println("OCR concluído com sucesso")
        tempService.deletar(uuid)
        return nota
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
        val linhas = texto.lines().map { it.trim() }.filter { it.isNotEmpty() }

        // Heurísticas para identificar campos
        linhas.forEach { linha ->
            when {
                linha.matches(Regex("(?i).*Nº\\s*[:\\-]?\\s*\\d+.*")) -> {
                    nota.numeroIdentificador = Regex("\\d+").find(linha)?.value?.toString()
                }
                linha.matches(Regex("(?i).*Total.*\\$?\\s*[\\d.,]+.*")) -> {
                    nota.valorTotal = Regex("([\\d.,]+)").find(linha)?.value?.replace(".", "")?.replace(",", ".")?.toDoubleOrNull()
                }
                linha.matches(Regex("(?i).*Cálculo.*\\$?\\s*[\\d.,]+.*")) -> {
                    nota.baseCalculo = Regex("([\\d.,]+)").find(linha)?.value?.replace(".", "")?.replace(",", ".")?.toDoubleOrNull()
                }
                linha.matches(Regex("(?i).*INSS.*\\$?\\s*[\\d.,]+.*")) -> {
                    nota.valorInss = Regex("([\\d.,]+)").find(linha)?.value?.replace(".", "")?.replace(",", ".")?.toDoubleOrNull()
                }
                linha.matches(Regex("(?i).*IPTU.*\\$?\\s*[\\d.,]+.*")) -> {
                    nota.creditoIptu = Regex("([\\d.,]+)").find(linha)?.value?.replace(".", "")?.replace(",", ".")?.toDoubleOrNull()
                }
                linha.matches(Regex("(?i).*Valor Deducoes.*\\$?\\s*[\\d.,]+.*")) -> {
                    nota.creditoIptu = Regex("([\\d.,]+)").find(linha)?.value?.replace(".", "")?.replace(",", ".")?.toDoubleOrNull()
                }
                linha.matches(Regex("(?i).*Alíquota.*\\d+%.*")) -> {
                    nota.aliquota = Regex("(\\d+(?:,\\d+)?)%").find(linha)?.groupValues?.get(1)?.replace(",", ".")?.toDoubleOrNull()
                }
                linha.matches(Regex("(?i).*Emissão.*\\d{2}/\\d{2}/\\d{4}.*")) -> {
                    nota.dataEmissao = Timestamp(SimpleDateFormat("dd/MM/yyyy").parse(Regex("\\d{2}/\\d{2}/\\d{4}").find(linha)!!.value).time)
                }
                linha.matches(Regex("(?i).*Vencimento.*\\d{2}/\\d{2}/\\d{4}.*")) -> {
                    nota.dataVencimento = Timestamp(SimpleDateFormat("dd/MM/yyyy").parse(Regex("\\d{2}/\\d{2}/\\d{4}").find(linha)!!.value).time)
                }
                linha.matches(Regex("(?i).*CNPJ.*\\d{2}\\.\\d{3}\\.\\d{3}/\\d{4}-\\d{2}.*")) -> {
                    nota.cnpjEmitente = Regex("\\d{2}\\.\\d{3}\\.\\d{3}/\\d{4}-\\d{2}").find(linha)?.value
                }
                linha.matches(Regex("(?i).*Moeda.*")) -> {
                    nota.nomeMoeda = linha.split(":"," ").last()
                }
                linha.matches(Regex("(?i).*Descrição.*")) -> {
                    nota.descricao = linha.split(":", "-").last().trim()
                }
                linha.matches(Regex("(?i).*Informação Adicional.*")) -> {
                    nota.informacaoAdicional = linha.split(":", "-").last().trim()
                }
                linha.matches(Regex("(?i).*Tipo de Nota.*")) -> {
                    nota.tipoNota = linha.split(":", "-").last().trim()
                }
                linha.matches(Regex("(?i).*Contrato.*\\d+.*")) -> {
                    nota.idContrato = Regex("\\d+").find(linha)?.value?.toIntOrNull()
                }
            }
        }

        notaFiscalService.cadastrar(nota);
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


