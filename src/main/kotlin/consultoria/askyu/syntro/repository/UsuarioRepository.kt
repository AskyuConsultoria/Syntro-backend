package consultoria.askyu.syntro.repository

import consultoria.askyu.syntro.dominio.Usuario
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UsuarioRepository: JpaRepository<Usuario, Int> {

    fun findByEmailAndSenhaEquals(email: String, senha:String): Usuario?
    fun findByNomeUsuarioAndSenhaEquals(email: String, senha:String): Usuario?
    fun findByEmailIgnoreCase(email:String?): Usuario?
    fun findByIdEmpresaAndEmissor(idEmpresa:Int, emissor:Boolean): List<Usuario>
    fun findByIdEmpresaAndAuditor(idEmpresa:Int, auditor:Boolean): List<Usuario>
    fun findByNomeUsuario(nome:String?): Usuario?
    fun findByAuditor(auditor:Boolean): List<Usuario>
    fun findByEmissor(emissor:Boolean): List<Usuario>
    fun findByIdDepartamento(idDepartamento: Int): List<Usuario>
    fun countByIdDepartamento(idDepartamento: Int): Int
}