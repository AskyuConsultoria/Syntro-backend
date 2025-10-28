package consultoria.askyu.syntro.dominio

import consultoria.askyu.syntro.enums.TipoIndentificacaoFiscal
import jakarta.persistence.*

@Entity
data class Empresa (
    @field:Id
    @field:GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int? = null,
    @Column(name = "nome_servico")
    var nomeServico: String? = null,
    @Column(name = "nome_fantasia")
    var nomeFantasia: String? = null,
    @Column(name = "area_atuacao")
    var areaAtuacao: String? = null,
    var fornecedor: Boolean? = null,
    var subsidiaria: String? = null,
    @Column(name = "identificacao_fiscal")
    var identificacaoFiscal: String? = null,
    @Column(name = "tipo_identificacao_fiscal")
    var tipoIdentificacaoFiscal: Int? = null
)