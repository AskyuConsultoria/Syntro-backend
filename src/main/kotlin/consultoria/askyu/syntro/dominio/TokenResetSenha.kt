package consultoria.askyu.syntro.dominio

import jakarta.persistence.*
import java.time.Instant

@Entity
@Table(name = "tokens_reset_senha")
open class TokenResetSenha(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    open var id: Int? = null,

    @Column(nullable = false)
    open var tokenHash: String? = null,

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_usuario", referencedColumnName = "id")
    open var usuario: Usuario?,

    @Column(nullable = false)
    open var tempoExpiracao: Instant = Instant.now(),

    @Column(nullable = false)
    open var usado: Boolean = false,

    @Column(nullable = true)
    open var ipRequisicao: String? = null,

    @Column(nullable = true)
    open var usuarioDeCriacao: String? = null
) {
    // 🔹 Construtor vazio obrigatório para JPA
    constructor() : this(null, null, null, Instant.now(), false, null, null)
    constructor(id: Nothing?, tokenHash: String, idUsuario: Int, tempoExpiracao: Instant?, usado: Boolean, ipRequisicao: String?, usuarioDeCriacao: String?) : this()
}
