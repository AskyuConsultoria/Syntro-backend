package consultoria.askyu.syntro.dominio

import jakarta.persistence.*

@Entity
data class Departamento(
    @field:Id
    @field:GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int? = null,
    @Column(name = "nome_departamento")
    var nomeDepartamento: String? = null
)