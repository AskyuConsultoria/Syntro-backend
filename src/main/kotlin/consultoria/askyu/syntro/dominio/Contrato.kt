package consultoria.askyu.syntro.dominio

import jakarta.persistence.*
import java.sql.Timestamp

@Entity
data class Contrato (
    @field:Id
    @field:GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int? = null,
    @Column(name = "nome_servico")
    var nomeServico: String? = null,
    var descricao: String? = null,
    @Column(name = "data_realizacao")
    var dataRealizacao: Timestamp? = null,
    @Column(name = "status_contrato")
    var statusContrato: Boolean? = null,
    @Column(name = "tempo_contrato")
    var tempoContrato: String? = null,
    var valor: Double? = null,
    @Column(name = "nome_moeda")
    var nomeMoeda: String? = null,
    @Column(name = "id_departamento")
    var idDepartamento: Int? = null,
    @Column(name = "id_empresa")
    var idEmpresa: Int? = null,
)