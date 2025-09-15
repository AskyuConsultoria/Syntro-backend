package consultoria.askyu.syntro.dto

class LoginRequest(val login: String? = null, val password: String? = null)
{
    constructor(): this(null, null)
}