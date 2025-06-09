package consultoria.askyu.nf.dominio

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import java.util.Date


@Entity
data class NotaFiscal(
    @field:Id
    @field:GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int? = null,
    var cnpjContratante: String? = null,
    var nomeEmpresa: String? = null,
    var cnpjFornecedor: String? = null,
    var enderecoFornecedor: String? = null,
    var dataVencimento: Date? = null,
    var numero: Int? = null,
    var descricao: String? = null,
    var valorTotal: Double? = null,
    var ativo: Boolean? = null
)