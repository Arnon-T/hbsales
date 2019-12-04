package br.com.hbsis.item.pedido;

import br.com.hbsis.pedido.Pedido;
import br.com.hbsis.produtos.Produto;

import javax.persistence.*;

@Entity
@Table(name = "seg_pedidos_itens")
public class PedidoItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @ManyToOne
    @JoinColumn(name = "id_pedido", referencedColumnName = "id")
    private Pedido pedido;
    @ManyToMany
    @JoinTable(name = "seg_pedidos_itens", joinColumns = @JoinColumn(name = "id_pedido"), inverseJoinColumns = @JoinColumn(name = "id_produto"))
    private Produto produto;
    @Column(name = "valor")
    private Double valorUnitario;
    @Column(name = "quantidade")
    private Double quantidade;

    public Long getId() {
        return id;
    }

    public Pedido getPedido() {
        return pedido;
    }

    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    public Double getValorUnitario() {
        return valorUnitario;
    }

    public void setValorUnitario(Double valorUnitario) {
        this.valorUnitario = valorUnitario;
    }

    public Double getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Double quantidade) {
        this.quantidade = quantidade;
    }
}
