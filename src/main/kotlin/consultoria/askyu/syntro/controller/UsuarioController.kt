package consultoria.askyu.syntro.controller

import consultoria.askyu.syntro.dominio.Usuario
import consultoria.askyu.syntro.dto.LoginRequest
import consultoria.askyu.syntro.dto.LoginResponse
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

    @GetMapping("/representante-interno")
    fun buscarPorEmpresaAndInterno(@RequestParam id: Int): ResponseEntity<List<Usuario>> {
        val usuario = usuarioService.buscarPorIdEmpresaAndRepInterno(id)
        return ResponseEntity.ok(usuario)
    }

    @GetMapping("/representante-externo")
    fun buscarPorEmpresaAndExterno(@RequestParam id: Int): ResponseEntity<List<Usuario>> {
        val usuario = usuarioService.buscarPorIdEmpresaAndRepExterno(id)
        return ResponseEntity.ok(usuario)
    }

    @PostMapping("/login")
    fun login(@RequestBody request: LoginRequest): ResponseEntity<LoginResponse> {
        val usuario = usuarioService.login(request.login, request.password)
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

}