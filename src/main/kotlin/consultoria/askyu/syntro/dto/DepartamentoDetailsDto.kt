package consultoria.askyu.syntro.dto

data class DepartamentoDetailsDto(
    val id: Int,
    val nome: String?,
    val membros: List<MembroDepartamentoResponseDto>
)
