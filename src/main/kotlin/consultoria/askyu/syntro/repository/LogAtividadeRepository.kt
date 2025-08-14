package consultoria.askyu.syntro.repository

import consultoria.askyu.syntro.dominio.LogAtividade
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface LogAtividadeRepository: JpaRepository<LogAtividade, Int> {
}