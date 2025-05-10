package consultoria.askyu.syntro.repositorys

import consultoria.askyu.syntro.dominio.Etl
import consultoria.askyu.syntro.interfaces.IRepository
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface EtlRepository:JpaRepository<Etl ,Int>, IRepository {

    fun findById(usuarioId: Int, agendamentoId: Int): List<Etl>
}