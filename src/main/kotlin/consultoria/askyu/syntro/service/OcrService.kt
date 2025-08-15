package consultoria.askyu.syntro.service

import consultoria.askyu.syntro.dominio.NotaFiscal
import consultoria.askyu.syntro.repository.NotaFiscalRepository
import net.sourceforge.tess4j.Tesseract
import net.sourceforge.tess4j.TesseractException
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.rendering.PDFRenderer
import org.modelmapper.ModelMapper
import org.springframework.stereotype.Service
import java.awt.image.BufferedImage
import java.io.InputStream
import java.sql.Timestamp
import java.text.SimpleDateFormat

@Service
class OcrService(
    private val tessDataPath: String = "C:/Tesseract-OCR/tessdata",
    private val notaFiscalService: NotaFiscalService
) {

    fun processarNotaFiscal(pdfInputStream: InputStream): NotaFiscal {
        println("Iniciando OCR inteligente da nota fiscal...")
        val texto = extrairTextoPdf(pdfInputStream)
        println("Texto extraído: ${texto.length} caracteres")
        println("Texto OCR extraído:\n$texto")
        val nota = inferirCamposNotaFiscal(texto)
        validarCamposObrigatorios(nota)
        println("OCR inteligente concluído com sucesso")
        return nota
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

    private fun inferirCamposNotaFiscal(textoOriginal: String): NotaFiscal {
        val nota = NotaFiscal()

        // Normalização
        val texto = textoOriginal
            .lowercase()
            .replace(Regex("[áàâã]"), "a")
            .replace(Regex("[éèê]"), "e")
            .replace(Regex("[íìî]"), "i")
            .replace(Regex("[óòôõ]"), "o")
            .replace(Regex("[úùû]"), "u")
            .replace(Regex("[ç]"), "c")
            .replace(Regex("[^a-z0-9\\s\\.,:/-]"), " ") // remove símbolos estranhos
            .replace(Regex("\\s+"), " ") // espaços múltiplos para um só
            .trim()

        // Número da NFS-e
        Regex("numero.*nfs?.*\\b(\\d{1,6})\\b").find(texto)?.let {
            nota.numeroIdentificador = it.groupValues[1].toIntOrNull()
        }

        // Data e hora de emissão
        Regex("(\\d{2}/\\d{2}/\\d{4} \\d{2}:\\d{2}:\\d{2})").find(texto)?.let {
            nota.dataEmissao = Timestamp(SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse(it.value).time)
        }

        // CNPJ emitente
        Regex("\\b\\d{2}\\.\\d{3}\\.\\d{3}/\\d{4}-\\d{2}\\b").find(texto)?.let {
            nota.cnpjEmitente = it.value
        }

        val valorTotalRegex = Regex("valor total[^\\d]*([\\d]{1,3}(?:[\\.,]\\d{3})*(?:[\\.,]\\d{2}))")
        valorTotalRegex.find(texto)?.let {
            nota.valorTotal = it.groupValues[1].replace(".", "").replace(",", ".").toDoubleOrNull()
        } ?: run {
            // Se não achou, procura por "valor do servico"
            val valorServicoRegex = Regex("valor do servic[o]?[\\s\\S]*?([\\d]{1,3}(?:[\\.,]\\d{3})*(?:[\\.,]\\d{2}))")
            valorServicoRegex.find(texto)?.let {
                nota.valorTotal = it.groupValues[1].replace(".", "").replace(",", ".").toDoubleOrNull()
            }
        }

        // Base de cálculo ISSQN
        Regex("bc\\s*issqn.*?([\\d\\.,]+)").find(texto)?.let {
            nota.baseCalculo = it.groupValues[1].replace(".", "").replace(",", ".").toDoubleOrNull()
        }

        val descricaoRegex = Regex("descricao do servic[o]?(.*?) (banco:|tributacao|valor do servico|valor total)", RegexOption.DOT_MATCHES_ALL)
        descricaoRegex.find(texto)?.let {
            nota.descricao = it.groupValues[1].trim()
        } ?: run {
            // Fallback: se não encontrar, tenta pegar a próxima linha após "Descrição do Serviço"
            val linhas = texto.lines()
            val idx = linhas.indexOfFirst { it.contains("descricao do servic") }
            if (idx != -1 && idx + 1 < linhas.size) {
                nota.descricao = linhas[idx + 1].trim()
            }
        }

        // Alíquota
        Regex("aliquota.*?(\\d+(?:,\\d+)?)%").find(texto)?.let {
            nota.aliquota = it.groupValues[1].replace(",", ".").toDoubleOrNull()
        }

        // Valor INSS
        Regex("inss.*?([\\d\\.,]+)").find(texto)?.let {
            nota.valorInss = it.groupValues[1].replace(".", "").replace(",", ".").toDoubleOrNull()
        }

        // Crédito IPTU
        Regex("iptu.*?([\\d\\.,]+)").find(texto)?.let {
            nota.creditoIptu = it.groupValues[1].replace(".", "").replace(",", ".").toDoubleOrNull()
        }

        // Valor deduções
        Regex("(total.*deducoes|valor.*deducoes).*?([\\d\\.,]+)").find(texto)?.let {
            nota.valorDeducoes = it.groupValues[2].replace(".", "").replace(",", ".").toDoubleOrNull()
        }

        // Informação adicional
        Regex("nbs[:\\s-]*(\\d+)").find(texto)?.let {
            nota.informacaoAdicional = "NBS: ${it.groupValues[1]}"
        } ?: run {
            val linhas = texto.lines()
            val idx = linhas.indexOfFirst { it.contains("informacoes complementares") }
            if (idx != -1 && idx + 1 < linhas.size) {
                nota.informacaoAdicional = linhas[idx + 1].trim()
            }
        }

        // NBS como idContrato só de exemplo (caso precise)
        Regex("nbs[:\\s-]*(\\d+)").find(texto)?.let {
            nota.idContrato = it.groupValues[1].toIntOrNull()
        }

        // Moeda fixa
        nota.nomeMoeda = "BRL"

        // Tipo da nota
        nota.tipoNota = "NFS-e"

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
        notaFiscalService.cadastrar(nota);
    }
}


