package consultoria.askyu.syntro.service

import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.stereotype.Service

@Service
class EmailService(private val enviadorDeEmail: JavaMailSender) {
    fun enviarResetDeSenha(to: String, resetUrl: String, qtdMinutos: Long) {
        val msg = SimpleMailMessage()
        msg.setTo(to)
        msg.subject = "Redefinição de senha"
        msg.text = """
            Recebemos uma solicitação para redefinir sua senha.
            Use o link abaixo dentro de $qtdMinutos minutos:

            $resetUrl

            Se você não solicitou, ignore este e-mail.
        """.trimIndent()
        enviadorDeEmail.send(msg)
    }
}
