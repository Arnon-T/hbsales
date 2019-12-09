package br.com.hbsis.item.pedido;

public class PedidoItemDTO {
    private Long id;
    private Long pedidoId;
    private Long produtoId;
    private Double valorUnitario;
    private Double quantidade;

    public PedidoItemDTO() {
    }

    public PedidoItemDTO(Long id, Long pedidoId, Long produtoId, Double valorUnitario, Double quantidade) {
        this.id = id;
        this.pedidoId = pedidoId;
        this.produtoId = produtoId;
        this.valorUnitario = valorUnitario;
        this.quantidade = quantidade;
    }

    public static PedidoItemDTO of(PedidoItem pedidoItem){
        return new PedidoItemDTO(
            pedidoItem.getId(),
            pedidoItem.getPedido().getId(),
            pedidoItem.getProduto().getId(),
            pedidoItem.getValorUnitario(),
            pedidoItem.getQuantidade()
        );
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPedidoId() {
        return pedidoId;
    }

    public void setPedidoId(Long pedidoId) {
        this.pedidoId = pedidoId;
    }

    public Long getProdutoId() {
        return produtoId;
    }

    public void setProdutoId(Long produtoId) {
        this.produtoId = produtoId;
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
