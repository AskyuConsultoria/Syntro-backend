package consultoria.askyu.syntro.dto

data class ChartDataDto(
    val nomeUsuario: String,
    val idUsuario: Int,
    val notasAprovadas: Int,
    val notasReprovadas: Int,
    val notasNaoIniciadas: Int,
    val notasEmAndamento: Int
)