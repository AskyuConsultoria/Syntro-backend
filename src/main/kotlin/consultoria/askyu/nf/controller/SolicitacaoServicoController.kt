package consultoria.askyu.nf.controller

import consultoria.askyu.nf.dominio.SolicitacaoServico
import consultoria.askyu.nf.service.SolicitacaoServicoService
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping


@Controller
@RequestMapping("/solicitacao-servico")
class SolicitacaoServicoController(
    val solicitacaoServicoService: SolicitacaoServicoService
) {

    @PostMapping
    fun cadastrar(@RequestBody solicitacaoServico: SolicitacaoServico): ResponseEntity<SolicitacaoServico>{
        val solicitacaoServicoNova = solicitacaoServicoService.cadastrar(solicitacaoServico)
        return ResponseEntity.status(200).body(solicitacaoServico)
    }

    @GetMapping
    fun buscar(): ResponseEntity<MutableList<SolicitacaoServico>>{
        val listaDeSolicitacaoDeServico = solicitacaoServicoService.buscar()
        return ResponseEntity.status(200).body(listaDeSolicitacaoDeServico)
    }

    @DeleteMapping("/{id}")
    fun excluir(@PathVariable id: Int?): ResponseEntity<SolicitacaoServico>{
        val solicitacaoServicoExcluida = solicitacaoServicoService.excluir(id)
        return ResponseEntity.status(200).body(solicitacaoServicoExcluida)
    }




}