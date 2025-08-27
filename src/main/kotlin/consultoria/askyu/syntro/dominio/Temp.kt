package consultoria.askyu.syntro.dominio

import jakarta.persistence.*

@Entity
data class Temp (
    @field:Id
    @field:GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int? = null,
    var chave: String? = null,
    var descricao: String? = null
)