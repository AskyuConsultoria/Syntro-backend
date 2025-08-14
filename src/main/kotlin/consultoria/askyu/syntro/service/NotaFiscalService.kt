package consultoria.askyu.syntro.service

import consultoria.askyu.syntro.dominio.NotaFiscal
import consultoria.askyu.syntro.`interface`.IService
import consultoria.askyu.syntro.repository.NotaFiscalRepository
import org.modelmapper.ModelMapper
import org.springframework.stereotype.Service

@Service
class NotaFiscalService(
    val repository: NotaFiscalRepository,
    val mapper: ModelMapper = ModelMapper()
): IService {

    fun cadastrar(notaFiscal: NotaFiscal): NotaFiscal{
        return repository.save(notaFiscal)
    }

    fun buscar(): MutableList<NotaFiscal>{
        val listaNotasFiscais = repository.findAll()
        listValidation(listaNotasFiscais)
        return listaNotasFiscais
    }
}