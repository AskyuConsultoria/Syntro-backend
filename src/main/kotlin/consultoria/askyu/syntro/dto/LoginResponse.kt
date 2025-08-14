package consultoria.askyu.syntro.dto

data class LoginResponse(
    var nomeUsuario: String,
    var nomeCompleto: String,
    var email: String,
    var cargo: String,
    var representanteInterno: Boolean,
    var representanteExterno: Boolean,
    var idDepartamento: Int
)