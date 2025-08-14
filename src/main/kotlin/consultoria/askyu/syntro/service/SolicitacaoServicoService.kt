package consultoria.askyu.syntro.service

import consultoria.askyu.syntro.abstratas.Servico
import consultoria.askyu.syntro.dominio.SolicitacaoServico
import consultoria.askyu.syntro.repository.NotaFiscalRepository
import consultoria.askyu.syntro.repository.SolicitacaoServicoRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service


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