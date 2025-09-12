package consultoria.askyu.syntro.service

import consultoria.askyu.syntro.dominio.TokenResetSenha
import consultoria.askyu.syntro.dominio.Usuario
import consultoria.askyu.syntro.dto.LoginResponse
import consultoria.askyu.syntro.`interface`.IService
import consultoria.askyu.syntro.repository.TokenResetSenhaRepository
import consultoria.askyu.syntro.repository.UsuarioRepository
import consultoria.askyu.syntro.utils.PasswordUtils
import consultoria.askyu.syntro.utils.TokenUtils
import org.modelmapper.ModelMapper
import org.springframework.http.HttpStatusCode
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import java.time.Instant
import java.time.temporal.ChronoUnit

@Service
class UsuarioService(
    val repository: UsuarioRepository,
    val mapper: ModelMapper = ModelMapper(),
    val tokenRepository: TokenResetSenhaRepository,
    val passwordHasher: PasswordHasher,
    val emailService: EmailService,
    val passwordUtils: PasswordUtils = PasswordUtils()
): IService {

    private val qtdMinutos: Long = 15

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

    fun resetRequest(email: String, ipRequisicao: String?, usuarioDeCriacao: String?){
        val usuario = buscarPorEmail(email)
        if(usuario != null){
            // TODO: criar função na service de tokens para exluir/desativar os tokens do usuário
            val token = TokenUtils.generateToken(32)
            val novoTokenHash = TokenUtils.sha256Hex(token)
            val novoToken = TokenResetSenha(
                id = null,
                tokenHash = novoTokenHash,
                usuario = usuario,
                tempoExpiracao = Instant.now().plus(qtdMinutos, ChronoUnit.MINUTES),
                usado = false,
                ipRequisicao = ipRequisicao,
                usuarioDeCriacao = usuarioDeCriacao,
            )
            tokenRepository.save(novoToken)
            val restUrl = "https://lorem.com/reset?token=$token" // Mudar Utilizando o IP de forma dinâmica (Via INFRA)

            emailService.enviarResetDeSenha(usuario.email!!, restUrl, qtdMinutos)
        }
    }

    fun resetarSenha(token:String, novaSenha: String){
        passwordUtils.validarForcaDaSenha(novaSenha)
        val tokenHash = TokenUtils.sha256Hex(token)
        val registroToken = tokenRepository.findByTokenHash(tokenHash)
            ?: throw IllegalArgumentException("Token Inválido")

        if(registroToken.usado || registroToken.tempoExpiracao.isBefore(Instant.now())){
            throw IllegalArgumentException("Token Inválido ou Expirado")
        }

        val usuario = buscarPorId(registroToken.usuario!!.id!!)
        registroToken.usado = true
        tokenRepository.save(registroToken)

        usuario.senha = passwordHasher.hash(novaSenha)
        cadastrar(usuario)
    }
}