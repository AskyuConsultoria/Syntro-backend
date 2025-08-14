package consultoria.askyu.syntro.dominio

import jakarta.persistence.*

@Entity
data class GrupoPermissao (
    @field:Id
    @field:GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int? = null,
    @Column(name = "id_usuario")
    var idUsuario: Int? = null,
    @Column(name = "id_permissao")
    var idPermissao: Int? = null,
)