package consultoria.askyu.syntro.repository

import consultoria.askyu.syntro.dominio.Usuario
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UsuarioRepository: JpaRepository<Usuario, Int> {

    fun findByEmailAndSenhaEquals(email: String, senha:String): Usuario?
    fun findByNomeUsuarioAndSenhaEquals(email: String, senha:String): Usuario?
}