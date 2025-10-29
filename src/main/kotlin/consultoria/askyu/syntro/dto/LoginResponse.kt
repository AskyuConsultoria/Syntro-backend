package consultoria.askyu.syntro.dto

data class LoginResponse(
    var id: Int? = null,
    var nomeUsuario: String? = null,
    var nomeCompleto: String? = null,
    var email: String? = null,
    var cargo: String? = null,
    var permissao: String? = null,
    var auditor: Boolean = false,
    var emissor: Boolean = false,
    var idDepartamento: Int? = null,
    var idEmpresa: Int? = null
)