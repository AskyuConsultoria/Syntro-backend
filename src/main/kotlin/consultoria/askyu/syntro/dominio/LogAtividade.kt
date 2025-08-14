package consultoria.askyu.syntro.dominio

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import java.sql.Timestamp
import java.time.LocalTime

@Entity(name = "log_atividade")
data class LogAtividade (
    @field:Id
    @field:GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int? = null,
    var tipo_acao: String? = null,
    var descricao: String? = null,
    var data_hora: Timestamp? = null,
    var usuario_id: Int? = null,
    var nota_fiscal_id: Int? = null,
)