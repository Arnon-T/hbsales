package br.com.hbsis.pedido;

import br.com.hbsis.funcionario.Funcionario;
import br.com.hbsis.item.pedido.PedidoItem;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "seg_pedidos")
public class Pedido {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "id_funcionario", referencedColumnName = "id")
    private Funcionario funcionario;
    @Column(name = "valor_total")
    private Double valorTotal;
    @Column(name = "data")
    private LocalDate data;
    @OneToMany
    List<PedidoItem> pedidoItemList;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Funcionario getFuncionario() {
        return funcionario;
    }

    public void setFuncionario(Funcionario funcionario) {
        this.funcionario = funcionario;
    }

    public Double getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(Double valorTotal) {
        this.valorTotal = valorTotal;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public List<PedidoItem> getPedidoItemList() {
        return pedidoItemList;
    }

    public void setPedidoItemList(List<PedidoItem> pedidoItemList) {
        this.pedidoItemList = pedidoItemList;
    }

    @Override
    public String toString() {
        return "Pedido{" +
                "id=" + id +
                ", funcionario=" + funcionario +
                ", valorTotal=" + valorTotal +
                ", data=" + data +
                ", pedidoItemList=" + pedidoItemList +
                '}';
    }
}
