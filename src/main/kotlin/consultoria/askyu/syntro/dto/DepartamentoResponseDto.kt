package consultoria.askyu.syntro.dto

data class DepartamentoResponseDto(
    val id: Int,
    val nome: String?,
    val qtdMembros: Int,
    val servicosAtivos: Int
)