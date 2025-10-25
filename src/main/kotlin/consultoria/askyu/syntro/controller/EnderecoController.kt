package consultoria.askyu.syntro.controller

import consultoria.askyu.syntro.dominio.Endereco
import consultoria.askyu.syntro.service.EnderecoService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/enderecos")
class EnderecoController(
    private val enderecoService: EnderecoService
) {

    @PostMapping
    fun cadastrar(@RequestBody endereco: Endereco): ResponseEntity<Endereco> {
        val novoEndereco = enderecoService.cadastrar(endereco)
        return ResponseEntity.ok(novoEndereco)
    }

    @GetMapping
    fun listar(): ResponseEntity<List<Endereco>> {
        return ResponseEntity.ok(enderecoService.listarTodos())
    }

    @GetMapping("/{id}")
    fun buscarPorId(@PathVariable id: Int): ResponseEntity<Endereco> {
        val endereco = enderecoService.buscarPorId(id)
        return if (endereco != null) ResponseEntity.ok(endereco)
        else ResponseEntity.notFound().build()
    }

    @PutMapping("/{id}")
    fun editar(@PathVariable id: Int, @RequestBody dadosAtualizados: Endereco): ResponseEntity<Endereco> {
        val endereco = enderecoService.editar(id, dadosAtualizados)
        return if (endereco != null) ResponseEntity.ok(endereco)
        else ResponseEntity.notFound().build()
    }

    @DeleteMapping("/{id}")
    fun deletar(@PathVariable id: Int): ResponseEntity<Void> {
        return if (enderecoService.deletar(id)) ResponseEntity.noContent().build()
        else ResponseEntity.notFound().build()
    }
}