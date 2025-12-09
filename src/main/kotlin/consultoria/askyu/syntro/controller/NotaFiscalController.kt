package consultoria.askyu.syntro.controller

import consultoria.askyu.syntro.dominio.NotaFiscal
import consultoria.askyu.syntro.dominio.Usuario
import consultoria.askyu.syntro.dto.ChartDataDto
import consultoria.askyu.syntro.service.NotaFiscalService
import consultoria.askyu.syntro.service.OcrService
import consultoria.askyu.syntro.service.S3Service
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.sql.Timestamp
import java.time.LocalDate
import java.time.LocalDateTime
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
    fun uploadNotaFiscal(@RequestBody file: MultipartFile, @RequestParam idUsuario: Int): ResponseEntity<String> {
        if (file.isEmpty) return ResponseEntity.badRequest().build()
        return try {
            val uuid = UUID.randomUUID().toString()
            val nota = ocrService.processarNotaFiscalv2(file.inputStream, uuid, idUsuario)
            s3Service.uploadArquivo(file)
            ResponseEntity.ok(nota)
        } catch (e: Exception) {
            e.printStackTrace()
            ResponseEntity.badRequest().body(null)
        }
    }

    @PostMapping("/upload-multiple")
    fun uploadMultiplasNotaFiscal(@RequestBody file: List<MultipartFile>, @RequestParam idUsuario:Int): ResponseEntity<List<String>> {
        if (file.isEmpty()) return ResponseEntity.badRequest().build()
        return try {
            val notas = ocrService.processarNotasFiscais(file, idUsuario)
            file.forEach{ arquivo ->
                s3Service.uploadArquivo(arquivo)
            }
            ResponseEntity.ok(notas)
        } catch (e: Exception) {
            e.printStackTrace()
            ResponseEntity.badRequest().body(null)
        }
    }




    @GetMapping("/status/{status}")
    fun buscarPorStatus(@PathVariable status: Int): ResponseEntity<List<NotaFiscal>> {
        val notas = notaFiscalService.buscarPorStatus(status)
        notaFiscalService.listValidation(notas!!)
        return ResponseEntity.ok(notas)
    }

    @GetMapping("/data-vencimento/menor-que")
    fun buscarPorDataVencimentoMenorQueData(@RequestParam  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) data: LocalDateTime): ResponseEntity<List<NotaFiscal>> {
        val notas = notaFiscalService.buscarPorDataVencimentoMenorQueData(data)
        notaFiscalService.listValidation(notas!!)
        return ResponseEntity.ok(notas)
    }

    @GetMapping("/data-vencimento/entre")
    fun buscarPorDataVencimentoEmIntervalo(@RequestParam  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) dataInicio: LocalDateTime,  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) dataFim: LocalDateTime): ResponseEntity<List<NotaFiscal>> {
        val notas = notaFiscalService.buscarPorDataEmIntervalo(dataInicio, dataFim)
        notaFiscalService.listValidation(notas!!)
        return ResponseEntity.ok(notas)
    }

    @PutMapping("/status/{idNota}")
    fun atualizarStatusDaNotaFiscal(@PathVariable idNota: Int, @RequestParam status: Int): ResponseEntity<NotaFiscal?> {
        val nota = notaFiscalService.atualizarCampoStatus(idNota, status)
        return ResponseEntity.ok(nota)
    }

    @GetMapping("/periodo")
    fun buscarPorPeriodo(
        @RequestParam inicio: String,
        @RequestParam fim: String
    ): ResponseEntity<List<ChartDataDto>> {
        return try {
            ResponseEntity.ok(notaFiscalService.buscarPorPeriodo(inicio, fim))
        } catch (e: Exception) {
            e.printStackTrace()
            ResponseEntity.badRequest().body(null)
        }
    }

    @PostMapping("/cadastrar-mock")
    fun cadastrarMock(@RequestBody notaFiscal: NotaFiscal): ResponseEntity<NotaFiscal> {
        val novaNota = notaFiscalService.cadastrar(notaFiscal)
        return ResponseEntity.ok(novaNota)
    }
}
