package consultoria.askyu.syntro.controllers

import consultoria.askyu.syntro.services.S3Service
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

//@RestController
//@RequestMapping("/nota-fiscal")
class NotaPdfController(
    val s3Service: S3Service
) {

    @PostMapping("/upload")
    fun upload(@RequestParam("pdf") pdf: MultipartFile): ResponseEntity<Map<String, String>> {
        val fileUrl = s3Service.uploadArquivo(pdf)
        return ResponseEntity.ok(mapOf("message" to "Upload foi feito com sucesso.", "url" to fileUrl))
    }

}