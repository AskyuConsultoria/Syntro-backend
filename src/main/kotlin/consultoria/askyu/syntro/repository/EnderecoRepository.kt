package consultoria.askyu.syntro.repository

import consultoria.askyu.syntro.dominio.Endereco
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface EnderecoRepository: JpaRepository<Endereco, Int> {
}