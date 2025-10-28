package consultoria.askyu.syntro.service

import consultoria.askyu.syntro.dominio.Contrato
import consultoria.askyu.syntro.repository.ContratoRepository
import org.modelmapper.ModelMapper
import org.springframework.data.jpa.domain.AbstractPersistable_.id
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service

@Service
class ContratoService(
    private val repository: ContratoRepository,
    val mapper: ModelMapper = ModelMapper()
) {
    fun cadastrar(contrato: Contrato): Contrato {
        var contratoSalvo = repository.save(contrato)
        return contratoSalvo
    }

    fun buscarPorId(id:Int): Contrato{
        return repository.findById(id).get()
    }
}