package consultoria.askyu.nf.service

import consultoria.askyu.nf.abstratas.Servico
import consultoria.askyu.nf.dominio.NotaFiscal
import consultoria.askyu.nf.repository.NotaFiscalRepository
import org.aspectj.weaver.ast.Not
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.RequestBody

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