package consultoria.askyu.syntro.repository

import consultoria.askyu.syntro.dominio.GrupoPermissao
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface GrupoPermissaoRepository: JpaRepository<GrupoPermissao, Int> {
}