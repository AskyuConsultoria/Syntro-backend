package consultoria.askyu.syntro.dominio

import jakarta.persistence.*

@Entity
data class Usuario(
    @field:Id
    @field:GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int? = null,
    @Column(name = "nome_usuario")
    var nomeUsuario: String? = null,
    @Column(name = "nome_completo")
    var nomeCompleto: String? = null,
    var email: String? = null,
    var cargo: String? = null,
    @Column(name = "representante_interno")
    var representanteInterno: Int? = null,
    @Column(name = "representa_externo")
    var representaExterno: Int? = null,
    var senha: String? = null,
    @Column(name = "id_departamento")
    var idDepartamento: Int? = null
)