package br.com.hbsis.periodo.venda;

import br.com.hbsis.fornecedor.Fornecedor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IPeriodoVendaRepository extends JpaRepository<PeriodoVenda, Long> {

    Optional<PeriodoVenda> findByFornecedor_Id(Long id);

    Optional<PeriodoVenda> findByFornecedor(Fornecedor fornecedor);

}
