package consultoria.askyu.syntro.repository

import consultoria.askyu.syntro.dominio.NotaFiscal
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface NotaFiscalRepository: JpaRepository<NotaFiscal, Int>{
    fun findByIdEmpresa(empresaId: Int): List<NotaFiscal>
    fun findByNumeroIdentificador(numeroIdentificador: String): NotaFiscal
}