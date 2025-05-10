package consultoria.askyu.syntro.controllers
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.io.ByteArrayInputStream
import net.sourceforge.tess4j.Tesseract
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.rendering.PDFRenderer
import java.util.regex.Pattern

@RestController
@RequestMapping("/ocr")
class OcrController {
    @PostMapping("/upload-pdf")
    fun processPdf(@RequestBody file: MultipartFile): Map<String, String?> {
        val text = extractTextFromPdf(file.bytes)
        return extractFields(text)
    }

    private fun extractTextFromPdf(pdfBytes: ByteArray): String {
        val document = PDDocument.load(ByteArrayInputStream(pdfBytes))
        val pdfRenderer = PDFRenderer(document)
        val tesseract = Tesseract().apply { setDatapath("C:\\Program Files\\Tesseract-OCR\\tessdata") }

        val extractedText = StringBuilder()
        for (pageIndex in 0 until document.numberOfPages) {
            val bufferedImage = pdfRenderer.renderImageWithDPI(pageIndex, 300f)
            extractedText.append(tesseract.doOCR(bufferedImage)).append(" ")
        }
        document.close()
        return extractedText.toString()
    }

    private fun extractFields(text: String): Map<String, String?> {
        val nome = extractField(text, "Nome:\\s*(.+)")
        val cpf = extractField(text, "CPF:\\s*(\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2})")
        val valor = extractField(text, "Valor a pagar:\\s*R\\$\\s*([\\d,.]+)")
        val dataVencimento = extractField(text, "Data de Vencimento:\\s*(\\d{2}/\\d{2}/\\d{4})")

        return mapOf(
            "Nome" to nome,
            "CPF" to cpf,
            "Valor" to valor,
            "DataVencimento" to dataVencimento
        )
    }

    private fun extractField(text: String, regex: String): String? {
        val matcher = Pattern.compile(regex).matcher(text)
        return if (matcher.find()) matcher.group(1) else null
    }
}
