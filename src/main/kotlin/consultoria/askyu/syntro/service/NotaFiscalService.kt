package consultoria.askyu.syntro.service

import consultoria.askyu.syntro.dominio.NotaFiscal
import consultoria.askyu.syntro.dto.ChartDataDto
import consultoria.askyu.syntro.`interface`.IService
import consultoria.askyu.syntro.repository.NotaFiscalRepository
import consultoria.askyu.syntro.repository.UsuarioRepository
import org.modelmapper.ModelMapper
import org.springframework.stereotype.Service
import kotlinx.coroutines.*
import org.springframework.http.ResponseEntity
import org.springframework.web.multipart.MultipartFile
import java.sql.Timestamp
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.YearMonth
import java.util.UUID
import java.time.format.DateTimeFormatter

@Service
class NotaFiscalService(
    private val repository: NotaFiscalRepository,
    private val usuarioRepository: UsuarioRepository,
    val mapper: ModelMapper = ModelMapper()
): IService {
    fun cadastrar(nota: NotaFiscal): NotaFiscal {
        return repository.save(nota)
    }

    fun buscarTodas(): MutableList<NotaFiscal>{
        val notas = repository.findAll()
        listValidation(notas)
        return notas
    }

    fun buscarPorIdEmpresa(empresaId: Int): List<NotaFiscal>? {
        return repository.findByIdEmpresa(empresaId)
    }

    fun buscarPorNumeroIdentificador(numeroIdentificador: String): NotaFiscal? {
        return repository.findByNumeroIdentificador(numeroIdentificador)
    }

    fun buscarPorStatus(status: Int): List<NotaFiscal>? {
        return repository.findByStatusEquals(status)
    }

    fun buscarPorDataVencimentoMenorQueData(data: LocalDateTime): List<NotaFiscal>? {
        return repository.findByDataVencimentoLessThan(data)
    }

    fun buscarPorDataEmIntervalo(dataInicio: LocalDateTime, dataFim: LocalDateTime): List<NotaFiscal>? {
        return repository.findByDataVencimentoBetween(dataInicio, dataFim)
    }

    fun atualizarCampoContrato(idNota: Int, idContrato: Int): NotaFiscal {
        var nota = repository.findById(idNota).get()
        nota.idContrato = idContrato
        return repository.save(nota)
    }

    fun atualizarCampoStatus(IdNota: Int, status: Int): NotaFiscal? {
        var nota = repository.findById(IdNota).get()
        nota.status = status
        return repository.save(nota)
    }

    fun buscarPorPeriodo(inicioString: String, fimString: String): List<ChartDataDto> {

        val formatter = DateTimeFormatter.ofPattern("yyyy-MM")
        val ymInicio = YearMonth.parse(inicioString, formatter)
        val ymFim = YearMonth.parse(fimString, formatter)
        val inicio = Timestamp.valueOf(
            ymInicio.atDay(1).atStartOfDay()
        )
        val fim = Timestamp.valueOf(
            ymFim.atEndOfMonth().atTime(23, 59, 59)
        )

        val listaUsuariosId = mutableListOf<Int>()

        val notas = repository.findByDataEmissaoBetween(inicio, fim)

        notas.forEach { nota ->
            if(!listaUsuariosId.contains(nota.idUsuario)) {
                listaUsuariosId.add(nota.idUsuario!!)
            }
        }

        val datas = mutableListOf<ChartDataDto>()

        listaUsuariosId.forEach { id ->
            val nomeUsuario = usuarioRepository.findById(id).get().nomeUsuario!!
            val reprovadas = repository.countByDataEmissaoBetweenAndIdUsuarioAndStatus(inicio,fim, id,4)
            val aprovadas = repository.countByDataEmissaoBetweenAndIdUsuarioAndStatus(inicio,fim, id,3)
            val naoIniciadas = repository.countByDataEmissaoBetweenAndIdUsuarioAndStatus(inicio,fim, id,1)
            val emAndamento = repository.countByDataEmissaoBetweenAndIdUsuarioAndStatus(inicio,fim, id,2)
            val dto = ChartDataDto(nomeUsuario, id, aprovadas,reprovadas,naoIniciadas, emAndamento)
            datas.add(dto)
        }

        return datas
    }

}