package consultoria.askyu.syntro.service

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import software.amazon.awssdk.core.sync.RequestBody
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.model.PutObjectRequest

@Service
class S3Service(
    @Value("\${aws.s3.bucket-name}") private val nomeDoBucket: String,
    @Value("\${aws.s3.region}") private val region: String
) {

    private val s3: S3Client = S3Client.builder()
        .region(Region.of(region))
        .build()

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
