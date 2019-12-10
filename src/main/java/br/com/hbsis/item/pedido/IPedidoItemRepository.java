package br.com.hbsis.item.pedido;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IPedidoItemRepository extends JpaRepository<PedidoItem, Long> {

}
