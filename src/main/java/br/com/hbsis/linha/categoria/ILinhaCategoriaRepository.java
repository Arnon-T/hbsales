package br.com.hbsis.linha.categoria;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ILinhaCategoriaRepository extends JpaRepository<LinhaCategoria, Long> {
}
