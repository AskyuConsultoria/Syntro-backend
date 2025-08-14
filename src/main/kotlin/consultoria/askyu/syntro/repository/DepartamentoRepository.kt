package consultoria.askyu.syntro.repository

import consultoria.askyu.syntro.dominio.Departamento
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface DepartamentoRepository: JpaRepository<Departamento, Int> {
}