package br.com.hbsis.produtos;

import java.time.LocalDate;

public class ProdutoDTO {
    private Long id;
    private Long codigoProduto;
    private String nomeProduto;
    private Double precoProduto;
    private Long linhaCategoriaId;
    private Double unidadesCaixa;
    private Double pesoUnidade;
    private String dataValidade;

    public ProdutoDTO(Long id, Long codigoProduto, String nomeProduto, Double precoProduto, Long linhaCategoriaId, Double unidadesCaixa, Double pesoUnidade, String dataValidade) {
        this.id = id;
        this.codigoProduto = codigoProduto;
        this.nomeProduto = nomeProduto;
        this.precoProduto = precoProduto;
        this.linhaCategoriaId = linhaCategoriaId;
        this.unidadesCaixa = unidadesCaixa;
        this.pesoUnidade = pesoUnidade;
        this.dataValidade = dataValidade;
    }

    public static ProdutoDTO of (Produto produto){
        return new ProdutoDTO(
                produto.getId(),
                produto.getCodigoProduto(),
                produto.getNomeProduto(),
                produto.getPrecoProduto(),
                produto.getLinhaCategoria().getIdLinhaCategoria(),
                produto.getUnidadesCaixa(),
                produto.getPesoUnidade(),
                produto.getDataValidade()
                );
    }

    public Long getId() {
        return id;
    }

    public Long getCodigoProduto() {
        return codigoProduto;
    }

    public void setCodigoProduto(Long codigoProduto) {
        this.codigoProduto = codigoProduto;
    }

    public String getNomeProduto() {
        return nomeProduto;
    }

    public void setNomeProduto(String nomeProduto) {
        this.nomeProduto = nomeProduto;
    }

    public Double getPrecoProduto() {
        return precoProduto;
    }

    public void setPrecoProduto(Double precoProduto) {
        this.precoProduto = precoProduto;
    }

    public Long getLinhaCategoriaId() {
        return linhaCategoriaId;
    }

    public void setLinhaCategoriaId(Long linhaCategoriaId) {
        this.linhaCategoriaId = linhaCategoriaId;
    }

    public Double getUnidadesCaixa() {
        return unidadesCaixa;
    }

    public void setUnidadesCaixa(Double unidadesCaixa) {
        this.unidadesCaixa = unidadesCaixa;
    }

    public Double getPesoUnidade() {
        return pesoUnidade;
    }

    public void setPesoUnidade(Double pesoUnidade) {
        this.pesoUnidade = pesoUnidade;
    }

    public String getDataValidade() {
        return dataValidade;
    }

    public void setDataValidade(String dataValidade) {
        this.dataValidade = dataValidade;
    }

    @Override
    public String toString() {
        return "ProdutoDTO{" +
                "id=" + id +
                ", codigoProduto=" + codigoProduto +
                ", nomeProduto='" + nomeProduto + '\'' +
                ", precoProduto=" + precoProduto +
                ", linhaCategoriaId=" + linhaCategoriaId +
                ", unidadesCaixa=" + unidadesCaixa +
                ", pesoUnidade=" + pesoUnidade +
                ", dataValidade=" + dataValidade +
                '}';
    }
}
