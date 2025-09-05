package consultoria.askyu.syntro.service

import consultoria.askyu.syntro.dominio.TokenResetSenha
import consultoria.askyu.syntro.repository.TokenResetSenhaRepository
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import java.time.Instant
import java.time.temporal.ChronoUnit

@Service
open class ResetSenhaService(
    private val usuarioService: UsuarioService,
    private val tokenRepository: TokenResetSenhaRepository,
    private val passwordHasher: PasswordHasher,
    private val emailService: EmailService
) {
    private val qtdMinutos: Long = 15


    fun resetRequest(email: String, ipRequisicao: String?, usuarioDeCriacao: String?){
        val usuario = usuarioService.buscarPorEmail(email)
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
        val tokenHash = TokenUtils.sha256Hex(token)
        val registroToken = tokenRepository.findByTokenHash(tokenHash)
            ?: throw IllegalArgumentException("Token Inválido")

        if(registroToken.usado || registroToken.tempoExpiracao.isBefore(Instant.now())){
            throw IllegalArgumentException("Token Inválido ou Expirado")
        }

        val usuario = usuarioService.buscarPorId(registroToken.usuario!!.id!!)
        registroToken.usado = true
        tokenRepository.save(registroToken)

        usuario.senha = passwordHasher.hash(novaSenha)
        usuarioService.cadastrar(usuario)
    }
}