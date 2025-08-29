package consultoria.askyu.syntro.repository

import consultoria.askyu.syntro.dominio.Usuario
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Repository

@Repository
interface UsuarioRepository: JpaRepository<Usuario, Int> {

    fun findByEmailAndSenhaEquals(email: String, senha:String): Usuario?
    fun findByNomeUsuarioAndSenhaEquals(email: String, senha:String): Usuario?
    fun findByIdEmpresaAndRepresentanteExterno(idEmpresa:Int, representante:Boolean): List<Usuario>
    fun findByIdEmpresaAndRepresentanteInterno(idEmpresa:Int, representante:Boolean): List<Usuario>
}