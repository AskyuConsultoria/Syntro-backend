package consultoria.askyu.syntro.controller

import consultoria.askyu.syntro.dominio.LogAtividade
import consultoria.askyu.syntro.service.LogAtividadeService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/comentar")
class LogAtividadeController(
    private val logAtividadeService: LogAtividadeService
) {

    @PostMapping
    fun comentar(@RequestBody atividade: LogAtividade): ResponseEntity<LogAtividade> {
        val novoTemp = logAtividadeService.cadastrar(atividade)
        return ResponseEntity.ok(novoTemp)
    }

    @GetMapping
    fun buscarTodosPorNota(@RequestParam notaId: Int): ResponseEntity<List<LogAtividade>> {
        val logAtividade = logAtividadeService.buscarTodasPorNota(notaId)
        return ResponseEntity.ok(logAtividade)
    }

    @GetMapping("/data-hora/{idNota}")
    fun buscarPorDataHora(@RequestParam dataHoraComeco: String, @RequestParam dataHoraFim: String, @PathVariable idNota:Int): ResponseEntity<List<LogAtividade>> {
        val logAtividade = logAtividadeService.buscarPorDataHora(dataHoraComeco, dataHoraFim, idNota)
        return ResponseEntity.ok(logAtividade)
    }

    @DeleteMapping("/{id}")
    fun deletar(@PathVariable id: Int): ResponseEntity<Void> {
        logAtividadeService.deletar(id)
        return ResponseEntity.noContent().build()
    }
}