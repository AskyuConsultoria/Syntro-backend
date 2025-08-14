package consultoria.askyu.syntro.repository

import consultoria.askyu.syntro.dominio.Empresa
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface EmpresaRepository: JpaRepository<Empresa, Int> {
}