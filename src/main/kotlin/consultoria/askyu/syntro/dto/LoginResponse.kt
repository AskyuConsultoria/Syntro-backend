package consultoria.askyu.syntro.dto

data class LoginResponse(
    var nomeUsuario: String? = null,
    var nomeCompleto: String? = null,
    var email: String? = null,
    var cargo: String? = null,
    var representanteInterno: Boolean = false,
    var representanteExterno: Boolean = false,
    var idDepartamento: Int? = null
)