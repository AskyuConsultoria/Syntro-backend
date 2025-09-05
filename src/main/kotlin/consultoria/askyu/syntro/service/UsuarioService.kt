package consultoria.askyu.syntro.service

import consultoria.askyu.syntro.dominio.Usuario
import consultoria.askyu.syntro.dto.LoginResponse
import consultoria.askyu.syntro.`interface`.IService
import consultoria.askyu.syntro.repository.EmpresaRepository
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

    fun buscarPorIdEmpresaAndRepInterno(idEmpresa:Int):List<Usuario>{
        var reps = repository.findByIdEmpresaAndRepresentanteInterno(idEmpresa, true)
        listValidation(reps)
        return reps
    }

    fun buscarPorIdEmpresaAndRepExterno(idEmpresa:Int):List<Usuario>{
        var reps = repository.findByIdEmpresaAndRepresentanteExterno(idEmpresa, true)
        listValidation(reps)
        return reps
    }

    fun login(login: String, password: String): LoginResponse {
        if (login.isBlank() || password.isBlank()) {
            throw ResponseStatusException(HttpStatusCode.valueOf(400), "Os Dados preenchidos estão incompletos")
        }

        val usuario = if (login.isEmail()) {
            repository.findByEmailAndSenhaEquals(login, password)
        } else {
            repository.findByNomeUsuarioAndSenhaEquals(login, password)
        } ?: throw ResponseStatusException(HttpStatusCode.valueOf(404), "Usuário não encontrado!")

        return mapper.map(usuario, LoginResponse::class.java)
    }

    fun deletar(idUsuario: Int) {
        idValidation(repository, idUsuario)
        repository.delete(repository.findById(idUsuario).get())
    }

    fun atualizarCampos(idUsuario: Int, updates: Map<String, Any>): Usuario {
        val usuario = repository.findById(idUsuario)
            .orElseThrow { ResponseStatusException(HttpStatusCode.valueOf(404), "Usuário não encontrado!") }

        updates.forEach { (campo, valor) ->
            when (campo) {
                "nomeUsuario" -> usuario.nomeUsuario = valor as String
                "nomeCompleto" -> usuario.nomeCompleto = valor as String
                "email" -> usuario.email = valor as String
                "senha" -> usuario.senha = valor as String
                "cargo" -> usuario.cargo = valor as String
                "representanteInterno" -> usuario.representanteInterno = valor as Boolean
                "representanteExterno" -> usuario.representanteExterno = valor as Boolean
                "idDepartamento" -> usuario.idDepartamento = (valor as Number).toInt()
                else -> throw ResponseStatusException(HttpStatusCode.valueOf(400), "Campo inválido: $campo")
            }
        }

        return repository.save(usuario)
    }


    fun String.isEmail(): Boolean {
        val regex = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}\$")
        return this.matches(regex)
    }

    fun buscarPorEmail(email: String): Usuario? {
        return repository.findByEmailIgnoreCase(email)
    }
}