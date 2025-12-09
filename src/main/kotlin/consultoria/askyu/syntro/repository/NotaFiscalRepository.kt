package consultoria.askyu.syntro.repository

import consultoria.askyu.syntro.dominio.NotaFiscal
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.sql.Timestamp
import java.time.LocalDateTime

@Repository
interface NotaFiscalRepository: JpaRepository<NotaFiscal, Int>{
    fun findByIdEmpresa(empresaId: Int): List<NotaFiscal>
    fun findByNumeroIdentificador(numeroIdentificador: String): NotaFiscal?
    fun findByStatusEquals(status: Int): List<NotaFiscal>?
    fun findByDataVencimentoLessThan(data: LocalDateTime): List<NotaFiscal>?
    fun findByDataVencimentoBetween(dataInicio: LocalDateTime, dataFim: LocalDateTime): List<NotaFiscal>?
    fun findByDataEmissaoBetween(
        dataInicial: Timestamp,
        dataFinal: Timestamp,
    ): List<NotaFiscal>
    fun countByDataEmissaoBetweenAndIdUsuarioAndStatus(
        dataInicial: Timestamp,
        dataFinal: Timestamp,
        status: Int,
        idUsuario: Int,
    ):Int
}