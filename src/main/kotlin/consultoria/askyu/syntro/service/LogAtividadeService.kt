package consultoria.askyu.syntro.service

import consultoria.askyu.syntro.dominio.LogAtividade
import consultoria.askyu.syntro.`interface`.IService
import consultoria.askyu.syntro.repository.LogAtividadeRepository
import org.modelmapper.ModelMapper
import org.springframework.stereotype.Service
import java.security.Timestamp

@Service
class LogAtividadeService(
    private val repository: LogAtividadeRepository,
    val mapper: ModelMapper = ModelMapper()
): IService {

    fun cadastrar(log: LogAtividade): LogAtividade {
        return repository.save(log)
    }

    fun buscarTodasPorNota(notaId: Int): List<LogAtividade>{
        val logs = repository.findByNotaFiscalId(notaId)
        listValidation(logs)
        return logs
    }

    fun deletar(id: Int) {
        var logAtividade = repository.findById(id).get()
        repository.delete(logAtividade)
    } 

    fun buscarPorDataHora(dataHoraComeco:Timestamp, dataHoraFim: Timestamp): List<LogAtividade> {
        val logs = repository.findByDataHoraBetween(dataHoraComeco, dataHoraFim)
        listValidation(logs)
        return logs
    }

}