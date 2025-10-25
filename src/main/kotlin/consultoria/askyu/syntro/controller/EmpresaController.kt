package consultoria.askyu.syntro.controller

import consultoria.askyu.syntro.dominio.Empresa
import consultoria.askyu.syntro.service.EmpresaService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/empresas")
class EmpresaController(
    private val empresaService: EmpresaService
) {

    @PostMapping
    fun cadastrar(@RequestBody empresa: Empresa): ResponseEntity<Empresa> {
        val novaEmpresa = empresaService.cadastrar(empresa)
        return ResponseEntity.ok(novaEmpresa)
    }

    @GetMapping
    fun listar(): ResponseEntity<List<Empresa>> {
        return ResponseEntity.ok(empresaService.listarTodas())
    }

    @GetMapping("/{id}")
    fun buscarPorId(@PathVariable id: Int): ResponseEntity<Empresa> {
        val empresa = empresaService.buscarPorId(id)
        return if (empresa != null) ResponseEntity.ok(empresa)
        else ResponseEntity.notFound().build()
    }

    @PutMapping("/{id}")
    fun editar(@PathVariable id: Int, @RequestBody dadosAtualizados: Empresa): ResponseEntity<Empresa> {
        val empresa = empresaService.editar(id, dadosAtualizados)
        return if (empresa != null) ResponseEntity.ok(empresa)
        else ResponseEntity.notFound().build()
    }

    @DeleteMapping("/{id}")
    fun deletar(@PathVariable id: Int): ResponseEntity<Void> {
        return if (empresaService.deletar(id)) ResponseEntity.noContent().build()
        else ResponseEntity.notFound().build()
    }
}