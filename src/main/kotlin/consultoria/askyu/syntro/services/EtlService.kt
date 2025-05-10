package consultoria.askyu.syntro.services

import consultoria.askyu.syntro.dominio.Etl
import consultoria.askyu.syntro.interfaces.IService
import consultoria.askyu.syntro.repositorys.EtlRepository
import org.modelmapper.ModelMapper
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile

@Service
class EtlService(
    mapper: ModelMapper = ModelMapper(),
    val repository: EtlRepository
): IService(repository, mapper) {

    fun busdarInsighs():List<Etl>{
        return repository.findAll()
    }

    fun inserir(arquivoDeEtl: MultipartFile): List<Etl> {
        return mutableListOf<Etl>()
    }
}