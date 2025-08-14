package consultoria.askyu.syntro.service

import consultoria.askyu.syntro.abstratas.Servico
import consultoria.askyu.syntro.dominio.NotaFiscal
import consultoria.askyu.syntro.repository.NotaFiscalRepository
import org.springframework.stereotype.Service

@Service
class NotaFiscalService(
    val repository: NotaFiscalRepository
): Servico {

    fun cadastrar(notaFiscal: NotaFiscal): NotaFiscal{
        return repository.save(notaFiscal)
    }

    fun buscar(): MutableList<NotaFiscal>{
        val listaNotasFiscais = repository.findAll()
        listValidation(listaNotasFiscais)
        return listaNotasFiscais
    }
}