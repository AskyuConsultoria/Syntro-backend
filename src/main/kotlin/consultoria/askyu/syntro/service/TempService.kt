package consultoria.askyu.syntro.service

import consultoria.askyu.syntro.dominio.Usuario
import consultoria.askyu.syntro.dto.LoginResponse
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

    fun cadastrar(temp: Temp): Temp {
        return repository.save(temp)
    }

    fun buscarTodos(): MutableList<Temp>{
        val temps = repository.findAll()
        listValidation(temps)
        return temps
    }

    fun buscarTemp(chaveTemp: String): Temp{
        return repository.findByChave(chaveTemp) ?: throw ResponseStatusException(HttpStatusCode.valueOf(404), "Temp não encontrado!")
    }

    fun deletar(idTemp: Int) {
        idValidation(repository, idTemp)
        repository.delete(repository.findById(idTemp).get())
    }

}