package consultoria.askyu.syntro.controller

import consultoria.askyu.syntro.dominio.Contrato
import consultoria.askyu.syntro.service.ContratoService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/contrato")
class ContratoController(
    private val service: ContratoService
) {
    @PostMapping
    fun cadastrar(contrato: Contrato): ResponseEntity<Contrato> {
        val contrato = service.cadastrar(contrato)
        return ResponseEntity.ok(contrato)
    }

    @GetMapping("/contratos")
    fun buscarContratos(): List<Contrato> {
        var contratos = service.buscarTodos()
        return contratos
    }

    @GetMapping("/{idContrato}")
    fun buscarPorIdNota(@PathVariable idContrato: Int): ResponseEntity<Contrato> {
        var contrato = service.buscarPorId(idContrato)
        return ResponseEntity.ok(contrato)
    }
}