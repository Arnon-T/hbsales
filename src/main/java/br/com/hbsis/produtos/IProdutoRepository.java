package br.com.hbsis.produtos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IProdutoRepository extends JpaRepository<Produto, Long> {

    boolean existsByCodigoProduto(Long codigoProduto);

    Optional<Produto> findByCodigoProduto(Long codigoProduto);
}
