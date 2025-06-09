package consultoria.askyu.nf.controller

import consultoria.askyu.nf.dominio.NotaFiscal
import consultoria.askyu.nf.service.NotaFiscalService
import consultoria.askyu.nf.service.S3Service
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@Controller
@RequestMapping("/nota-fiscal")
class NotaFiscalController(
    val notaFiscalService: NotaFiscalService,
    val s3Service: S3Service
) {

    @PostMapping
    fun cadastrar(@RequestBody notaFiscal: NotaFiscal): ResponseEntity<NotaFiscal>{
        val notaFiscalNova = notaFiscalService.cadastrar(notaFiscal)
        return ResponseEntity.status(200).body(notaFiscalNova)
    }

    @GetMapping
    fun buscar(): ResponseEntity<MutableList<NotaFiscal>>{
        val listaNotaFiscal = notaFiscalService.buscar()
        return ResponseEntity.status(200).body(listaNotaFiscal)
    }

    @PostMapping("/upload")
    fun upload(@RequestParam("pdf") pdf: MultipartFile): ResponseEntity<Map<String, String>> {
        val fileUrl = s3Service.uploadArquivo(pdf)
        return ResponseEntity.ok(mapOf("message" to "Upload foi feito com sucesso.", "url" to fileUrl))
    }

}