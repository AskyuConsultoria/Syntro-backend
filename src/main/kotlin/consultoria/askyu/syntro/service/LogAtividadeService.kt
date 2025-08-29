package consultoria.askyu.syntro.service

import consultoria.askyu.syntro.dominio.LogAtividade
import consultoria.askyu.syntro.`interface`.IService
import consultoria.askyu.syntro.repository.LogAtividadeRepository
import org.modelmapper.ModelMapper
import org.springframework.stereotype.Service
import java.sql.Timestamp
import java.text.SimpleDateFormat

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
        idValidation(repository, id)
        var logAtividade = repository.findById(id).get()
        repository.delete(logAtividade)
    } 

    fun buscarPorDataHora(dataHoraComeco:String, dataHoraFim: String): List<LogAtividade> {
        val logs = repository.findByDataHoraBetween(stringParaTimestamp(dataHoraComeco), stringParaTimestamp(dataHoraFim))
        listValidation(logs)
        return logs
    }

    fun stringParaTimestamp(dataStr: String, formato: String = "yyyy-MM-dd HH:mm:ss"): Timestamp {
        val sdf = SimpleDateFormat(formato)
        val date = sdf.parse(dataStr)
        return Timestamp(date.time)
    }


}