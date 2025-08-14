package consultoria.askyu.syntro.service

import consultoria.askyu.syntro.dominio.NotaFiscal
import consultoria.askyu.syntro.`interface`.IService
import consultoria.askyu.syntro.repository.NotaFiscalRepository
import org.modelmapper.ModelMapper
import org.springframework.stereotype.Service

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
}