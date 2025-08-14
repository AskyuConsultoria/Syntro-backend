package consultoria.askyu.syntro.dominio

import jakarta.persistence.*

@Entity
data class Endereco (
    @field:Id
    @field:GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int? = null,
    var logradouro: String? = null,
    var bairro: String? = null,
    var cidade: String? = null,
    var complemento: String? = null,
    var uf: String? = null,
    var cep: Int? = null,
    @Column(name = "id_fornecedor")
    var idFornecedor: Int? = null,
    @Column(name = "id_usuario")
    var idUsuario: Int? = null,
)