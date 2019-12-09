package br.com.hbsis.fornecedor;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Classe responsável pela comunciação com o banco de dados
 */
@Repository
public interface IFornecedorRepository extends JpaRepository<Fornecedor, Long> {

    List<Fornecedor> findByNomeFantasia(String nome);

    Fornecedor findByCnpj(String cnpj);

    @Override
    boolean existsById(Long id);
}
