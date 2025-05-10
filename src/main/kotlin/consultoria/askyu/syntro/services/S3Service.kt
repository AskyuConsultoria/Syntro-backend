package consultoria.askyu.syntro.services

import org.springframework.beans.factory.annotation.Value
import org.springframework.web.multipart.MultipartFile
import software.amazon.awssdk.core.sync.RequestBody
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.model.PutObjectRequest

//@Service
class S3Service {

    @Value("\${aws.s3.bucket-name}")
    lateinit var nomeDoBucket: String
    val s3 = S3Client.create()

    fun uploadArquivo(file: MultipartFile): String {
        val nomeDoArquivo = "notas-raw/${file.originalFilename}"
        val request = PutObjectRequest.builder()
            .bucket(nomeDoBucket)
            .key(nomeDoArquivo)
            .build()

        s3.putObject(request, RequestBody.fromBytes(file.bytes))

        return "https://$nomeDoBucket.s3.amazonaws.com/$nomeDoArquivo"
    }
}
