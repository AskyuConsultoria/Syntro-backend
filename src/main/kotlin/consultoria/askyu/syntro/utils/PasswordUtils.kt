package consultoria.askyu.syntro.utils

import consultoria.askyu.syntro.service.PasswordHasher
import org.springframework.http.HttpStatusCode
import org.springframework.web.server.ResponseStatusException

class PasswordUtils {
    fun validarForcaDaSenha(senha: String) {
//        require(senha.length >= 12) { "Senha deve ter ao menos 12 caracteres." }
    }
    fun verificarSenha(hash: String, senha: String, passwordHasher: PasswordHasher): Boolean{
        if(!passwordHasher.verify(hash, senha)){
            throw ResponseStatusException(HttpStatusCode.valueOf(403), "Usuário ou senha inválidos")
        }
        else return true
    }
}