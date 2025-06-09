package consultoria.askyu.nf.service

import consultoria.askyu.nf.abstratas.Servico
import consultoria.askyu.nf.dominio.SolicitacaoServico
import consultoria.askyu.nf.repository.NotaFiscalRepository
import consultoria.askyu.nf.repository.SolicitacaoServicoRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpStatusCode
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.server.ResponseStatusException


@Service
class SolicitacaoServicoService(
    val repository: SolicitacaoServicoRepository,
    val notaFiscalRepository: NotaFiscalRepository
): Servico {

    fun cadastrar(solicitacaoServico: SolicitacaoServico): SolicitacaoServico{
        if(solicitacaoServico.notaFiscal != null) idValidation(notaFiscalRepository,
            solicitacaoServico.notaFiscal!!.id!!
        )
        return repository.save(solicitacaoServico)
    }

    fun buscar(): MutableList<SolicitacaoServico>{
        val listaSolicitacaoServico = repository.findAll()
        listValidation(listaSolicitacaoServico)
        return listaSolicitacaoServico
    }

    fun excluir(id: Int?): SolicitacaoServico{
        val solicitacaoServico = repository.findByIdOrNull(id)
        solicitacaoServico!!.ativo = false
        return repository.save(solicitacaoServico)
    }
}