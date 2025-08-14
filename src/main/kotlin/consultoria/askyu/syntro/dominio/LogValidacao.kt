package consultoria.askyu.syntro.dominio

import jakarta.persistence.*
import java.sql.Timestamp

@Entity(name = "log_validacao")
data class LogValidacao (
    @field:Id
    @field:GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int? = null,
    @Column(name = "data_hora")
    var dataHora: Timestamp? = null,
    var etapa: Int? = null,
    var detalhe: String? = null,
    var modulo: String? = null,
    @Column(name = "nota_fiscal_id")
    var notaFiscalId: Int? = null,
)