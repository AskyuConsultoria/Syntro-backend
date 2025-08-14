package consultoria.askyu.syntro.controller

import consultoria.askyu.syntro.dominio.Usuario
import consultoria.askyu.syntro.dto.LoginRequest
import consultoria.askyu.syntro.service.UsuarioService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/usuarios")
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

    @PostMapping("/login")
    fun login(@RequestBody request: LoginRequest): ResponseEntity<Usuario> {
        val usuario = usuarioService.login(request.login, request.password)
        return ResponseEntity.ok(usuario)
    }
}