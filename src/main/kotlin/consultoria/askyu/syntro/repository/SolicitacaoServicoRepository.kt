package consultoria.askyu.syntro.repository

import consultoria.askyu.syntro.dominio.SolicitacaoServico
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface SolicitacaoServicoRepository: JpaRepository<SolicitacaoServico, Int>