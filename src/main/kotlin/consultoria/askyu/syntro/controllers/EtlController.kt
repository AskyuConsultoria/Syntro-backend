package consultoria.askyu.syntro.controllers

import consultoria.askyu.syntro.dominio.Etl
import consultoria.askyu.syntro.interfaces.IController
import consultoria.askyu.syntro.services.EtlService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/etl")
class EtlController(
    var service: EtlService
) : IController(service)
{
//    @Operation(summary = "Listagem de todos os Insighs",
//        description = "Busca todas os insighs.")
//    @ApiResponses(
//        value = [
//            ApiResponse(responseCode = "200", description = "Insighs encontradas com sucesso!"),
//            ApiResponse(responseCode = "204", description =  "Não foi encontrado nenhum insighs"),
//            ApiResponse(responseCode = "404", description = "Usuário não foi encontrado.", content = [Content(schema = Schema())]),
//        ],
//    )
    @GetMapping
    fun buscar(): ResponseEntity<List<Etl>> {
        val listaInsighs = service.busdarInsighs()
        return ResponseEntity.status(200).body(listaInsighs)
    }
}