package consultoria.askyu.syntro.repository

import consultoria.askyu.syntro.dominio.Temp
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface TempRepository: JpaRepository<Temp, Int> {

    fun findByChave(chave: String): Temp
    fun findByIdUsuario(idUsuario: Int): List<Temp>
}