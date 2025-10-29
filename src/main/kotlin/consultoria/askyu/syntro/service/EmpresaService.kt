package consultoria.askyu.syntro.service

import consultoria.askyu.syntro.dominio.Empresa
import consultoria.askyu.syntro.repository.EmpresaRepository
import org.modelmapper.ModelMapper
import org.springframework.stereotype.Service

@Service
class EmpresaService(
    private val empresaRepository: EmpresaRepository,
    val mapper: ModelMapper = ModelMapper()
) {

    fun cadastrar(empresa: Empresa): Empresa {
        return empresaRepository.save(empresa)
    }

    fun listarTodas(): List<Empresa> {
        return empresaRepository.findAll()
    }

    fun buscarPorId(id: Int): Empresa? {
        return empresaRepository.findById(id).orElse(null)
    }

    fun editar(id: Int, dadosAtualizados: Empresa): Empresa? {
        val empresaExistente = empresaRepository.findById(id)
        return if (empresaExistente.isPresent) {
            val empresa = empresaExistente.get().apply {
                nomeServico = dadosAtualizados.nomeServico
                areaAtuacao = dadosAtualizados.areaAtuacao
                fornecedor = dadosAtualizados.fornecedor
                subsidiaria = dadosAtualizados.subsidiaria
                identificacaoFiscal = dadosAtualizados.identificacaoFiscal
                tipoIdentificacaoFiscal = dadosAtualizados.tipoIdentificacaoFiscal
            }
            empresaRepository.save(empresa)
        } else {
            null
        }
    }

    fun deletar(id: Int): Boolean {
        val empresa = empresaRepository.findById(id)
        return if (empresa.isPresent) {
            empresaRepository.deleteById(id)
            true
        } else {
            false
        }
    }
}