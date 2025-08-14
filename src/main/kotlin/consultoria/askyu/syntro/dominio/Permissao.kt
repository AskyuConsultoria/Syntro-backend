package consultoria.askyu.syntro.dominio

import jakarta.persistence.*

@Entity
data class Permissao (
    @field:Id
    @field:GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int? = null,
    @Column(name = "user_roles")
    var userRoles: Int? = null,
)