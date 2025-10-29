package consultoria.askyu.syntro.service

import consultoria.askyu.syntro.dominio.Endereco
import consultoria.askyu.syntro.repository.EnderecoRepository
import org.springframework.stereotype.Service

@Service
class EnderecoService(
    private val enderecoRepository: EnderecoRepository
) {

    fun cadastrar(endereco: Endereco): Endereco {
        return enderecoRepository.save(endereco)
    }

    fun listarTodos(): List<Endereco> {
        return enderecoRepository.findAll()
    }

    fun buscarPorId(id: Int): Endereco? {
        return enderecoRepository.findById(id).orElse(null)
    }

    fun editar(id: Int, dadosAtualizados: Endereco): Endereco? {
        val enderecoExistente = enderecoRepository.findById(id)
        return if (enderecoExistente.isPresent) {
            val endereco = enderecoExistente.get().apply {
                logradouro = dadosAtualizados.logradouro
                bairro = dadosAtualizados.bairro
                cidade = dadosAtualizados.cidade
                complemento = dadosAtualizados.complemento
                uf = dadosAtualizados.uf
                cep = dadosAtualizados.cep
                idFornecedor = dadosAtualizados.idFornecedor
                idUsuario = dadosAtualizados.idUsuario
            }
            enderecoRepository.save(endereco)
        } else {
            null
        }
    }

    fun deletar(id: Int): Boolean {
        val endereco = enderecoRepository.findById(id)
        return if (endereco.isPresent) {
            enderecoRepository.deleteById(id)
            true
        } else {
            false
        }
    }
}