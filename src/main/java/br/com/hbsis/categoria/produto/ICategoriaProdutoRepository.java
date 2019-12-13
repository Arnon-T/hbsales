package br.com.hbsis.categoria.produto;

import br.com.hbsis.categoria.produto.CategoriaProduto;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Classe responsável pela comunciação com o banco de dados
 */
@Repository
public interface ICategoriaProdutoRepository extends JpaRepository<CategoriaProduto, Long> {

    boolean existsCategoriaProdutoByCodigoCategoriaProduto(String codigoCategoriaProduto);

    boolean existsCategoriaProdutoByFornecedorId(Long id);

    @Override
    boolean existsById(Long id);

    CategoriaProduto findByCodigoCategoriaProduto(String codigoCategoriaProduto);

}
