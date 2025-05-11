package consultoria.askyu.syntro.dominio

import consultoria.askyu.syntro.interfaces.IDomain
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id

@Entity
data class NotaFiscal(
    @field:Id
    @field:GeneratedValue(strategy = GenerationType.IDENTITY)
    var id:Int? = null,
    val cnpjContratante: String,
    val nomeEmpresa: String,
    val cnpjFornecedor: String,
    val enderecoFornecedor: String,
    val dataVencimento: String,
    val numero: String,
    val descricao: String,
    val valorTotal: String
): IDomain()