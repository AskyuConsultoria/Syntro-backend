package consultoria.askyu.syntro.controller

import consultoria.askyu.syntro.dominio.Departamento
import consultoria.askyu.syntro.dto.DepartamentoDetailsDto
import consultoria.askyu.syntro.dto.DepartamentoResponseDto
import consultoria.askyu.syntro.dto.MembroDepartamentoResponseDto
import consultoria.askyu.syntro.service.DepartamentoService
import consultoria.askyu.syntro.service.UsuarioService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/departamentos")
class DepartamentoController(
    private val departamentoService: DepartamentoService,
    private val usuarioService: UsuarioService
) {

    @PostMapping
    fun cadastrar(@RequestBody departamento: Departamento): ResponseEntity<Departamento> {
        val novoDepartamento = departamentoService.cadastrar(departamento)
        return ResponseEntity.ok(novoDepartamento)
    }

    @GetMapping
    fun listar(): ResponseEntity<List<DepartamentoResponseDto>> {
        val departamentos = departamentoService.listarTodos().map { dept ->
            DepartamentoResponseDto(
                id = dept.id!!,
                nome = dept.nomeDepartamento,
                qtdMembros = usuarioService.contarUsuariosPorDepartamento(dept.id!!),
                servicosAtivos = usuarioService.contarServicosAtivosPorDepartamento(dept.id!!)
            )
        }
        return ResponseEntity.ok(departamentos)
    }

    @GetMapping("/{id}")
    fun detalhes(@PathVariable id: Int): ResponseEntity<DepartamentoDetailsDto> {
        val departamento = departamentoService.buscarPorId(id) ?: return ResponseEntity.notFound().build()

        val membros = usuarioService.listarUsuariosPorDepartamento(id).map { usuario ->
            MembroDepartamentoResponseDto(
                id = usuario.id!!,
                nome = usuario.nomeCompleto ?: usuario.nomeUsuario ?: "Sem Nome",
                accessLevel = usuario.permissao ?: "Desconhecido"
            )
        }

        val dto = DepartamentoDetailsDto(
            id = departamento.id!!,
            nome = departamento.nomeDepartamento,
            membros = membros
        )

        return ResponseEntity.ok(dto)
    }

    @DeleteMapping("/{id}")
    fun deletar(@PathVariable id: Int): ResponseEntity<Void> {
        return if (departamentoService.deletar(id)) ResponseEntity.noContent().build()
        else ResponseEntity.notFound().build()
    }
}
