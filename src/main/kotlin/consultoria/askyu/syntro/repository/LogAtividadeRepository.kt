package consultoria.askyu.syntro.repository

import consultoria.askyu.syntro.dominio.LogAtividade
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.sql.Timestamp

@Repository
interface LogAtividadeRepository: JpaRepository<LogAtividade, Int> {
    fun findByNotaFiscalId(notaFiscalId: Int): List<LogAtividade>
    fun findByDataHoraBetween(dataHoraComeco: Timestamp, dataHoraFim: Timestamp): List<LogAtividade>
}