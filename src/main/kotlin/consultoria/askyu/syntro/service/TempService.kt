package consultoria.askyu.syntro.service

import consultoria.askyu.syntro.dominio.Temp
import consultoria.askyu.syntro.dominio.Usuario
import consultoria.askyu.syntro.dto.LoginResponse
import consultoria.askyu.syntro.dto.TempDto
import consultoria.askyu.syntro.`interface`.IService
import consultoria.askyu.syntro.repository.TempRepository
import org.modelmapper.ModelMapper
import org.springframework.http.HttpStatusCode
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException

@Service
class TempService(
    val repository: TempRepository,
    val mapper: ModelMapper = ModelMapper()
): IService {

    fun add(temp: Temp): Temp {
        return repository.save(temp)
    }

    fun buscarTodos(): MutableList<Temp>{
        val temps = repository.findAll()
        listValidation(temps)
        return temps
    }

    fun buscarPorUsuario(id:Int): List<Temp>{
        val temps = repository.findByIdUsuario(id)
        listValidation(temps)
        return temps
    }

    fun buscarTemp(chave: String): Temp{
        return repository.findByChave(chave) ?: throw ResponseStatusException(HttpStatusCode.valueOf(404), "Temp não encontrado!")
    }

    fun buscarPorChave(chave: String): Temp {
        var temp = repository.findByChave(chave) ?: throw ResponseStatusException(HttpStatusCode.valueOf(404), "Temp não encontrado!")
        return temp
    }

    fun deletar(chave: String) {
        var temp = repository.findByChave(chave) ?: throw ResponseStatusException(HttpStatusCode.valueOf(404), "Temp não encontrado!")
        repository.delete(temp)
    }

}