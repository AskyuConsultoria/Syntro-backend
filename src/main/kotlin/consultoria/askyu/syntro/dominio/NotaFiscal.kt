package consultoria.askyu.syntro.dominio

import jakarta.persistence.*
import java.sql.Timestamp
import java.util.Date


@Entity(name = "nota_fiscal")
data class NotaFiscal(
    @field:Id
    @field:GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int? = null,
    var numero_Identificador: Int? = null,
    var descricao: String? = null,
    var valor_total: Double? = null,
    var informacao_adicional: String? = null,
    var valor_deducoes: Double? = null,
    var base_calculo: Double? = null,
    var aliquota: Double? = null,
    var valor_inss: Double? = null,
    var credito_iptu: Double? = null,
    var data_emissao: Timestamp? = null,
    var data_vencimento: Timestamp? = null,
    var nome_moeda: String? = null,
    var id_contrato: Int? = null,
)