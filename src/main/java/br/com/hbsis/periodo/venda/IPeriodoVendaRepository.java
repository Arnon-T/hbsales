package br.com.hbsis.periodo.venda;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IPeriodoVendaRepository extends JpaRepository<PeriodoVenda, Long> {
}
