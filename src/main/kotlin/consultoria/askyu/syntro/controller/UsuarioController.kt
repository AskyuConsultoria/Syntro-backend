package consultoria.askyu.syntro.controller

import consultoria.askyu.syntro.dominio.Usuario
import consultoria.askyu.syntro.dto.EsqueceuSenhaRequest
import consultoria.askyu.syntro.dto.LoginRequest
import consultoria.askyu.syntro.dto.LoginResponse
import consultoria.askyu.syntro.dto.ResetSenhaRequest
import consultoria.askyu.syntro.service.UsuarioService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/user")
class UsuarioController(
    private val usuarioService: UsuarioService
) {

    @PostMapping
    fun cadastrar(@RequestBody usuario: Usuario): ResponseEntity<Usuario> {
        val novoUsuario = usuarioService.cadastrar(usuario)
        return ResponseEntity.ok(novoUsuario)
    }

    @GetMapping
    fun buscarTodos(): ResponseEntity<List<Usuario>> {
        val usuarios = usuarioService.buscarTodos()
        return ResponseEntity.ok(usuarios)
    }

    @GetMapping("/{id}")
    fun buscarPorId(@PathVariable id: Int): ResponseEntity<Usuario> {
        val usuario = usuarioService.buscarPorId(id)
        return ResponseEntity.ok(usuario)
    }

    @GetMapping("/auditor/{id}")
    fun buscarPorEmpresaAndAuditor(@PathVariable id: Int): ResponseEntity<List<Usuario>> {
        val usuario = usuarioService.buscarPorIdEmpresaAndRepInterno(id)
        return ResponseEntity.ok(usuario)
    }

    @GetMapping("/emissor/{id}")
    fun buscarPorEmpresaAndEmissor(@PathVariable id: Int): ResponseEntity<List<Usuario>> {
        val usuario = usuarioService.buscarPorIdEmpresaAndRepExterno(id)
        return ResponseEntity.ok(usuario)
    }

    @GetMapping("/emissor")
    fun buscarPorEmissor(@RequestParam id: Int): ResponseEntity<List<Usuario>> {
        val usuario = usuarioService.buscarPorEmissor(id)
        return ResponseEntity.ok(usuario)
    }

    @GetMapping("/auditor")
    fun buscarPorAudiotor(): ResponseEntity<List<Usuario>> {
        val usuario = usuarioService.buscarPorAuditor()
        return ResponseEntity.ok(usuario)
    }

    @CrossOrigin(origins = ["*"], allowedHeaders = ["*"], methods = [RequestMethod.POST, RequestMethod.OPTIONS])
    @PostMapping("/login")
    fun login(@RequestBody request: LoginRequest): ResponseEntity<LoginResponse> {
        val usuario = usuarioService.login(request.login!!, request.password!!)
        return ResponseEntity.ok(usuario)
    }

    @DeleteMapping("/{id}")
    fun deletar(@PathVariable id: Int): ResponseEntity<Void> {
        usuarioService.deletar(id)
        return ResponseEntity.noContent().build()
    }

    @PatchMapping("/{id}")
    fun atualizarCampo(
        @PathVariable id: Int,
        @RequestBody updates: Map<String, Any>
    ): ResponseEntity<Usuario> {
        val usuarioAtualizado = usuarioService.atualizarCampos(id, updates)
        return ResponseEntity.ok(usuarioAtualizado)
    }

    @CrossOrigin(origins = ["*"], allowedHeaders = ["*"], methods = [RequestMethod.POST, RequestMethod.OPTIONS])
    @PostMapping("/esqueceu-senha")
    fun esqueceu(
        @RequestBody req: EsqueceuSenhaRequest,
        request: jakarta.servlet.http.HttpServletRequest
    ): ResponseEntity<Map<String, String>> {
        usuarioService.resetRequest(
            email = req.email.trim(),
            ipRequisicao = request.remoteAddr,
            usuarioDeCriacao = request.getHeader("User-Agent")
        )
        return ResponseEntity.ok(mapOf("message" to "Se o e-mail existir, enviaremos instruções."))
    }

    @CrossOrigin(origins = ["*"], allowedHeaders = ["*"], methods = [RequestMethod.POST, RequestMethod.OPTIONS])
    @PostMapping("/resetar-senha")
    fun reset(@RequestBody req: ResetSenhaRequest): ResponseEntity<Map<String, String>> {
        usuarioService.resetarSenha(req.token, req.novaSenha)
        return ResponseEntity.ok(mapOf("message" to "Senha redefinida com sucesso."))
    }


}