package consultoria.askyu.nf.abstratas

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.http.HttpStatusCode
import org.springframework.web.server.ResponseStatusException

interface Servico {

    fun listValidation(list: List<*>){
        if(list.isEmpty()){
            throw ResponseStatusException(HttpStatusCode.valueOf(204),  "O resultado da busca foi uma lista vazia!")
        }
    }

    fun idValidation(repository: JpaRepository<*, Int>, id: Int){
       if(!repository.existsById(id)){
           throw ResponseStatusException(HttpStatusCode.valueOf(404), "Valor de repositorio $repository não encontrado.")
       }
    }

}