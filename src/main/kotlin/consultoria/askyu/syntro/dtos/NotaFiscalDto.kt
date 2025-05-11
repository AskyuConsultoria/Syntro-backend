package consultoria.askyu.syntro.dtos

data class NotaFiscalDto(
    val cnpjContratante: String,
    val nomeEmpresa: String,
    val cnpjFornecedor: String,
    val enderecoFornecedor: String,
    val dataVencimento: String,
    val numero: String,
    val descricao: String,
    val valorTotal: String
)