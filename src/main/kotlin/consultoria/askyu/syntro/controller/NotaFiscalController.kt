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
import java.util.UUID

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

    @GetMapping("/empresa")
    fun buscarTodosPorEmpresa(@RequestParam empresaId: Int): ResponseEntity<List<NotaFiscal>> {
        val notas = notaFiscalService.buscarPorIdEmpresa(empresaId)
        return ResponseEntity.ok(notas)
    }

    @GetMapping("/{numeroIdentificador}")
    fun buscarPorIdentificador(@PathVariable numeroIdentificador: String): ResponseEntity<NotaFiscal?> {
        val nota = notaFiscalService.buscarPorNumeroIdentificador(numeroIdentificador)
        return ResponseEntity.ok(nota)
    }

    @PutMapping("/{idNota}")
    fun AtualizarContratoNota(@PathVariable idNota: Int, @RequestParam idContrato: Int): ResponseEntity<NotaFiscal?> {
        return ResponseEntity.ok(notaFiscalService.atualizarCampoContrato(idNota, idContrato))
    }

    @PostMapping("/upload")
    fun uploadNotaFiscal(@RequestParam("file") file: MultipartFile): ResponseEntity<String> {
        if (file.isEmpty) return ResponseEntity.badRequest().build()
        return try {
            var uuid = UUID.randomUUID().toString()
            val nota = ocrService.processarNotaFiscal(file.inputStream, uuid)
            ResponseEntity.ok(uuid)
        } catch (e: Exception) {
            e.printStackTrace()
            ResponseEntity.badRequest().body(null)
        }
    }

    @PostMapping("/upload-multiple")
    fun uploadMultiplasNotaFiscal(@RequestParam("file") file: List<MultipartFile>): ResponseEntity<List<String>> {
        if (file.isEmpty()) return ResponseEntity.badRequest().build()
        return try {
            val notas = ocrService.processarNotasFiscais(file)
            ResponseEntity.ok(notas)
        } catch (e: Exception) {
            e.printStackTrace()
            ResponseEntity.badRequest().body(null)
        }
    }

}