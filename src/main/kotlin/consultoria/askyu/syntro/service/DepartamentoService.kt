package consultoria.askyu.syntro.service

import consultoria.askyu.syntro.dominio.Departamento
import consultoria.askyu.syntro.repository.DepartamentoRepository
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import org.springframework.http.HttpStatus

@Service
class DepartamentoService(
    private val departamentoRepository: DepartamentoRepository
) {

    fun cadastrar(departamento: Departamento): Departamento {
        return departamentoRepository.save(departamento)
    }

    fun listarTodos(): List<Departamento> {
        val departamentos = departamentoRepository.findAll()
        if (departamentos.isEmpty()) {
            throw ResponseStatusException(HttpStatus.NOT_FOUND, "Nenhum departamento encontrado")
        }
        return departamentos
    }

    fun buscarPorId(id: Int): Departamento? {
        return departamentoRepository.findById(id).orElse(null)
    }

    fun deletar(id: Int): Boolean {
        val departamento = buscarPorId(id) ?: return false
        departamentoRepository.delete(departamento)
        return true
    }
}
