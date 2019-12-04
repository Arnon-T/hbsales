package br.com.hbsis.pedido;

import java.time.LocalDate;
import java.util.List;

public class PedidoDTO {
    private Long id;
    private Long funcionarioId;
    private Double valorTotal;
    private LocalDate data;
    private List<Long> pedidoItemIdList;

    public PedidoDTO() {
    }

    public PedidoDTO(Long id, Long funcionarioId, Double valorTotal, LocalDate data) {
        this.id = id;
        this.funcionarioId = funcionarioId;
        this.valorTotal = valorTotal;
        this.data = data;
    }

    public static PedidoDTO of(Pedido pedido){
        return new PedidoDTO(
                pedido.getId(),
                pedido.getFuncionario().getId(),
                pedido.getValorTotal(),
                // VERIFICAR ESSA LINHA pedido.getPedidoItemList().get().getId(),
                pedido.getData()
        );
    }

    public Long getId() {
        return id;
    }

    public Long getFuncionarioId() {
        return funcionarioId;
    }

    public void setFuncionarioId(Long funcionarioId) {
        this.funcionarioId = funcionarioId;
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

    public List<Long> getPedidoItemIdList() {
        return pedidoItemIdList;
    }

    public void setPedidoItemIdList(List<Long> pedidoItemIdList) {
        this.pedidoItemIdList = pedidoItemIdList;
    }

    @Override
    public String toString() {
        return "PedidoDTO{" +
                "id=" + id +
                ", funcionarioId=" + funcionarioId +
                ", valorTotal=" + valorTotal +
                ", data=" + data +
                '}';
    }
}
