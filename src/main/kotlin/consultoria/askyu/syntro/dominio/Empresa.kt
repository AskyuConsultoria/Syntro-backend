package consultoria.askyu.syntro.dominio

import consultoria.askyu.syntro.enums.TipoIndentificacaoFiscal
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id

@Entity
data class Empresa (
    @field:Id
    @field:GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int? = null,
    var nome_servico: String? = null,
    var area_atuacao: String? = null,
    var fornecedor: Boolean? = null,
    var subsidiaria: String? = null,
    var identificacao_fiscal: String? = null,
    var tipo_identificacao_fiscal: Int? = null,
)