package consultoria.askyu.syntro.controller

import consultoria.askyu.syntro.dto.EsqueceuSenhaRequest
import consultoria.askyu.syntro.dto.ResetSenhaRequest
import consultoria.askyu.syntro.service.ResetSenhaService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/auth")
class ResetSenhaController(
    private val service: ResetSenhaService
) {

    @PostMapping("/esqueceu-senha")
    fun esqueceu(
        @RequestBody req: EsqueceuSenhaRequest,
        request: jakarta.servlet.http.HttpServletRequest
    ): ResponseEntity<Map<String, String>> {
        service.resetRequest(
            email = req.email.trim(),
            ipRequisicao = request.remoteAddr,
            usuarioDeCriacao = request.getHeader("User-Agent")
        )
        return ResponseEntity.ok(mapOf("message" to "Se o e-mail existir, enviaremos instruções."))
    }

    @PostMapping("/resetar-senha")
    fun reset(@RequestBody req: ResetSenhaRequest): ResponseEntity<Map<String, String>> {
        validarForcaDaSenha(req.novaSenha)
        service.resetarSenha(req.token, req.novaSenha)
        return ResponseEntity.ok(mapOf("message" to "Senha redefinida com sucesso."))
    }

    private fun validarForcaDaSenha(senha: String) {
//        require(senha.length >= 12) { "Senha deve ter ao menos 12 caracteres." }
    }
}
