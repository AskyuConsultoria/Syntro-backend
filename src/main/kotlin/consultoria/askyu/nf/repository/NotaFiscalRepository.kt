package consultoria.askyu.nf.repository

import consultoria.askyu.nf.dominio.NotaFiscal
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface NotaFiscalRepository: JpaRepository<NotaFiscal, Int>