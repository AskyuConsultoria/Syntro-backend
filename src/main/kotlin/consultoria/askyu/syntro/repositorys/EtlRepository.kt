package consultoria.askyu.syntro.repositorys

import consultoria.askyu.syntro.dominio.NotaFiscal
import consultoria.askyu.syntro.interfaces.IRepository
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface EtlRepository:JpaRepository<NotaFiscal ,Int>, IRepository {

    fun findById(usuarioId: Int, agendamentoId: Int): List<NotaFiscal>
}