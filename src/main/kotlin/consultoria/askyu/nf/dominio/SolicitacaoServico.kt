package consultoria.askyu.nf.dominio

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.ManyToOne

@Entity
data class SolicitacaoServico(
  @field:Id
  @field:GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int? = null,
    var cardId: Int? = null,
    var email: String? = null,
    var operacao: String? = null,
    var justificativa: String? = null,
    @field: ManyToOne
    var notaFiscal: NotaFiscal? = null,
    var ativo: Boolean? = null
)