package consultoria.askyu.syntro.dominio

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import java.sql.Timestamp

@Entity(name = "log_validacao")
data class LogValidacao (
    @field:Id
    @field:GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int? = null,
    var data_hora: Timestamp? = null,
    var etapa: Int? = null,
    var detalhe: String? = null,
    var modulo: String? = null,
    var nota_fiscal_id: Int? = null,
)