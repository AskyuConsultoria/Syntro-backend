package consultoria.askyu.syntro.service

import consultoria.askyu.syntro.dominio.NotaFiscal
import consultoria.askyu.syntro.`interface`.IService
import consultoria.askyu.syntro.repository.NotaFiscalRepository
import org.modelmapper.ModelMapper
import org.springframework.stereotype.Service
import kotlinx.coroutines.*
import org.springframework.http.ResponseEntity
import org.springframework.web.multipart.MultipartFile
import java.util.UUID

@Service
class NotaFiscalService(
    private val repository: NotaFiscalRepository,
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

    fun atualizarCampoContrato(idNota: Int, idContrato: Int): NotaFiscal {
        var nota = repository.findById(idNota).get()
        nota.idContrato = idContrato
        return repository.save(nota)
    }

}