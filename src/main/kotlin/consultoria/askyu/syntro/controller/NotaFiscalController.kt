package consultoria.askyu.syntro.controller

import consultoria.askyu.syntro.dominio.NotaFiscal
import consultoria.askyu.syntro.dominio.Usuario
import consultoria.askyu.syntro.service.NotaFiscalService
import consultoria.askyu.syntro.service.OcrService
import consultoria.askyu.syntro.service.S3Service
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@Controller
@RequestMapping("/nota-fiscal")
class NotaFiscalController(
    private val notaFiscalService: NotaFiscalService,
    private val s3Service: S3Service,
    private val ocrService: OcrService
) {

    //@PostMapping("/upload")
    //fun upload(@RequestParam("pdf") pdf: MultipartFile): ResponseEntity<Map<String, String>> {
    //    val fileUrl = s3Service.uploadArquivo(pdf)
    //    return ResponseEntity.ok(mapOf("message" to "Upload foi feito com sucesso.", "url" to fileUrl))
    //}

    @GetMapping
    fun buscarTodos(): ResponseEntity<List<NotaFiscal>> {
        val notas = notaFiscalService.buscarTodas()
        return ResponseEntity.ok(notas)
    }

    @PostMapping("/upload")
    fun uploadNotaFiscal(@RequestParam("file") file: MultipartFile): ResponseEntity<NotaFiscal> {
        if (file.isEmpty) return ResponseEntity.badRequest().build()
        return try {
            val nota = ocrService.processarNotaFiscal(file.inputStream)
            ResponseEntity.ok(nota)
        } catch (e: Exception) {
            e.printStackTrace()
            ResponseEntity.badRequest().body(null)
        }
    }

}