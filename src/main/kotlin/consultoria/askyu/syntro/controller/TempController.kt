package consultoria.askyu.syntro.controller

import consultoria.askyu.syntro.dominio.Temp
import consultoria.askyu.syntro.dto.TempDto
import consultoria.askyu.syntro.service.TempService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/temp")
class TempController(
    private val tempService: TempService
) {

    @PostMapping
    fun inserir(@RequestBody temp: Temp): ResponseEntity<Temp?> {
        val novoTemp = tempService.add(temp)
        return ResponseEntity.ok(novoTemp)
    }

    @GetMapping("/{chave}")
    fun buscarPorChave(@PathVariable chave: String): ResponseEntity<Temp> {
        val temp = tempService.buscarPorChave(chave)
        return ResponseEntity.ok(temp)
    }

    @DeleteMapping("/{chave}")
    fun deletar(@PathVariable chave: String): ResponseEntity<Void> {
        tempService.deletar(chave)
        return ResponseEntity.noContent().build()
    }

    @GetMapping("/ativos")
    fun buscarTodosPorUsuario(@RequestParam usuarioId: Int): ResponseEntity<List<Temp>> {
        val temps = tempService.buscarPorUsuario(usuarioId)
        return ResponseEntity.ok(temps)
    }
}