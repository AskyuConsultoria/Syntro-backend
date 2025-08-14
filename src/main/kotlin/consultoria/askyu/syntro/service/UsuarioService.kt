package consultoria.askyu.syntro.service

import consultoria.askyu.syntro.dominio.Usuario
import consultoria.askyu.syntro.`interface`.IService
import consultoria.askyu.syntro.repository.UsuarioRepository
import org.modelmapper.ModelMapper
import org.springframework.http.HttpStatusCode
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException

@Service
class UsuarioService(
    val repository: UsuarioRepository,
    val mapper: ModelMapper = ModelMapper()
): IService {

    fun cadastrar(usuario: Usuario): Usuario {
        return repository.save(usuario)
    }

    fun buscarTodos(): MutableList<Usuario>{
        val usuarios = repository.findAll()
        listValidation(usuarios)
        return usuarios
    }

    fun buscarPorId(idUsuario:Int): Usuario{
        idValidation(repository, idUsuario)
        return repository.findById(idUsuario).get()
    }

    fun login(login: String, password: String): Usuario {
        if (login.isBlank() || password.isBlank()) {
            throw ResponseStatusException(HttpStatusCode.valueOf(400), "Os Dados preenchidos estão incompletos")
        }

        val usuario = if (login.isEmail()) {
            repository.findByEmailAndSenhaEquals(login, password)
        } else {
            repository.findByNomeUsuarioAndSenhaEquals(login, password)
        } ?: throw ResponseStatusException(HttpStatusCode.valueOf(400), "Usuário não encontrado!")

        return usuario
    }

    fun String.isEmail(): Boolean {
        val regex = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}\$")
        return this.matches(regex)
    }
}