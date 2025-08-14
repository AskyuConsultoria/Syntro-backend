package consultoria.askyu.syntro.repository

import consultoria.askyu.syntro.dominio.Contrato
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ContratoRepository: JpaRepository<Contrato, Int> {
}