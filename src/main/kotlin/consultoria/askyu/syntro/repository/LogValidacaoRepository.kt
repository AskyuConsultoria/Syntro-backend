package consultoria.askyu.syntro.repository

import consultoria.askyu.syntro.dominio.LogValidacao
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface LogValidacaoRepository: JpaRepository<LogValidacao, Int> {
}