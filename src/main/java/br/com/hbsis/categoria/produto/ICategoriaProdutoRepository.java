package br.com.hbsis.categoria.produto;

import br.com.hbsis.categoria.produto.CategoriaProduto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Classe responsável pela comunciação com o banco de dados
 */
@Repository
public interface ICategoriaProdutoRepository extends JpaRepository<CategoriaProduto, Long> {
}
