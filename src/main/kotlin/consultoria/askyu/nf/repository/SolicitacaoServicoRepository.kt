package consultoria.askyu.nf.repository

import consultoria.askyu.nf.dominio.SolicitacaoServico
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface SolicitacaoServicoRepository: JpaRepository<SolicitacaoServico, Int>