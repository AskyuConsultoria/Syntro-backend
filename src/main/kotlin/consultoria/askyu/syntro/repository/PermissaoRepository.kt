package consultoria.askyu.syntro.repository

import consultoria.askyu.syntro.dominio.Permissao
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface PermissaoRepository: JpaRepository<Permissao, Int> {
}