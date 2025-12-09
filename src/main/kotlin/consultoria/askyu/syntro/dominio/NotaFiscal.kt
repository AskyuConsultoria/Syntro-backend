package consultoria.askyu.syntro.dominio

import jakarta.persistence.*
import java.sql.Timestamp
import java.util.Date


@Entity(name = "nota_fiscal")
data class NotaFiscal(
    @field:Id
    @field:GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int? = null,
    @Column(name = "numero_identificador")
    var numeroIdentificador: String? = null,
    var descricao: String? = null,
    @Column(name = "valor_total")
    var valorTotal: Double? = null,
    @Column(name = "informacao_adicional")
    var informacaoAdicional: String? = null,
    @Column(name = "valor_deducoes")
    var valorDeducoes: Double? = null,
    @Column(name = "base_calculo")
    var baseCalculo: Double? = null,
    var aliquota: Double? = null,
    @Column(name = "valor_inss")
    var valorInss: Double? = null,
    @Column(name = "credito_iptu")
    var creditoIptu: Double? = null,
    @Column(name = "data_emissao")
    var dataEmissao: Timestamp? = null,
    @Column(name = "data_vencimento")
    var dataVencimento: Timestamp? = null,
    @Column(name = "nome_moeda")
    var nomeMoeda: String? = null,
    @Column(name = "id_contrato")
    var idContrato: Int? = null,
    @Column(name = "cnpj_emitente")
    var cnpjEmitente: String? = null,
    @Column(name = "inscricao_municipal")
    var inscricaoMunicipal: String? = null,
    @Column(name = "tipo_nota")
    var tipoNota: String? = null,
    @Column(name = "id_empresa")
    var idEmpresa: Int? = null,
    @Column(name = "id_usuario")
    var idUsuario: Int? = null,
    @Column
    var status: Int? = null
    // Adicionei o status como INT para evitar erros de normalização. A ideia é que sejam 0 não iniciadas, 1 em andamento, 2 finalizadas.
    // Atrasadas ou a vencer são verificadas a partir de um endpoint temporal.

)