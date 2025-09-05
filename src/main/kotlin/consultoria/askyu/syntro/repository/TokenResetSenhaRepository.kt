package consultoria.askyu.syntro.repository

import consultoria.askyu.syntro.dominio.TokenResetSenha
import consultoria.askyu.syntro.dominio.Usuario
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.time.Instant

@Repository
interface TokenResetSenhaRepository: JpaRepository<TokenResetSenha, Int> {
    fun findByTokenHash(tokenHash: String): TokenResetSenha?
    fun deleteAllByUsuarioIdAndTempoExpiracaoBefore(usuarioId: Int, now: Instant)
}