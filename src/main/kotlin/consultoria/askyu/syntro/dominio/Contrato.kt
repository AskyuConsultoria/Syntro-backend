package consultoria.askyu.syntro.dominio

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import java.sql.Timestamp
import java.time.LocalDate

@Entity
data class Contrato (
    @field:Id
    @field:GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int? = null,
    var nome_servico: String? = null,
    var descricao: String? = null,
    var data_realizacao: Timestamp? = null,
    var status_contrato: Boolean? = null,
    var tempo_contrato: String? = null,
    var valor: Double? = null,
    var nome_moeda: String? = null,
    var id_departamento: Int? = null,
    var id_empresa: Int? = null,
)