package consultoria.askyu.syntro.dominio

import jakarta.persistence.*
import java.sql.Timestamp
import java.time.LocalTime

@Entity(name = "log_atividade")
data class LogAtividade (
    @field:Id
    @field:GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int? = null,
    @Column(name = "tipo_acao")
    var tipoAcao: String? = null,
    var descricao: String? = null,
    @Column(name = "data_hora")
    var dataHora: Timestamp? = null,
    @Column(name = "usuario_id")
    var usuarioId: Int? = null,
    @Column(name = "nota_fiscal_id")
    var notaFiscalId: Int? = null,
)