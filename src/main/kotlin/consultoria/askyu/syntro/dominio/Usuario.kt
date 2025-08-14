package consultoria.askyu.syntro.dominio

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.ManyToOne

@Entity
data class Usuario(
    @field:Id
    @field:GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int? = null,
    var nome_usuario: String? = null,
    var nome_completo: String? = null,
    var email: String? = null,
    var cargo: String? = null,
    var representante_interno: Int? = null,
    var representa_externo: Int? = null,
    var senha: String? = null,
    var id_departamento: Int? = null
)