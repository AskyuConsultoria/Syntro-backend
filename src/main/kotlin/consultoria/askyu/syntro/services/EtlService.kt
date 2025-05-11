package consultoria.askyu.syntro.services

import consultoria.askyu.syntro.dominio.NotaFiscal
import consultoria.askyu.syntro.dtos.NotaFiscalDto
import consultoria.askyu.syntro.interfaces.IService
import consultoria.askyu.syntro.repositorys.EtlRepository
import org.springframework.stereotype.Service
import java.io.File

@Service
class EtlService(
    val repository: EtlRepository
): IService(repository) {

    fun busdarInsights():List<NotaFiscal>{
        return repository.findAll()
    }

    fun inserir(arquivoDeNotas: File): List<NotaFiscal> {
        return lerCsv(arquivoDeNotas)
    }

    fun lerCsv(arquivo: File): List<NotaFiscal> {
        val resultado = mutableListOf<NotaFiscalDto>()
        val linhas = arquivo.readLines()
        val buffer = StringBuilder()

        for ((i, linha) in linhas.withIndex()) {
            if (i == 0) continue

            buffer.appendLine(linha)

            val conteudo = buffer.toString().trim()
            val campos = conteudo.split(";")

            if (campos.size < 8) continue

            if (campos.size == 8) {
                val dto = NotaFiscalDto(
                    cnpjContratante = campos[0],
                    nomeEmpresa = campos[1],
                    cnpjFornecedor = campos[2],
                    enderecoFornecedor = campos[3],
                    dataVencimento = campos[4],
                    numero = campos[5],
                    descricao = campos[6],
                    valorTotal = campos[7]
                )
                resultado.add(mapper.map(dto, NotaFiscalDto::class.java))
                buffer.clear()
            }
        }
        return salvarNota(resultado)
    }

    fun salvarNota(notas: List<NotaFiscalDto>, result:MutableList<NotaFiscal> = mutableListOf()):List<NotaFiscal>{
        notas.forEach {
            val nota = mapper.map(it, NotaFiscal::class.java)
            repository.save(nota)
            result.add(nota)
        }
        return result
    }
}