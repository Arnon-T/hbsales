package br.com.hbsis.produtos;

import br.com.hbsis.util.UnidadeMedida;

import java.time.LocalDate;

public class ProdutoDTO {
    private Long id;
    private String codigoProduto;
    private String nomeProduto;
    private Double precoProduto;
    private Long linhaCategoriaId;
    private int unidadesCaixa;
    private Double pesoUnidade;
    private LocalDate dataValidade;
    private UnidadeMedida unidadeMedida;

    public ProdutoDTO() {
    }

    public ProdutoDTO(Long id, String codigoProduto, String nomeProduto, Double precoProduto, Long linhaCategoriaId, int unidadesCaixa, Double pesoUnidade, LocalDate dataValidade, UnidadeMedida unidadeMedida) {
        this.id = id;
        this.codigoProduto = codigoProduto;
        this.nomeProduto = nomeProduto;
        this.precoProduto = precoProduto;
        this.linhaCategoriaId = linhaCategoriaId;
        this.unidadesCaixa = unidadesCaixa;
        this.pesoUnidade = pesoUnidade;
        this.dataValidade = dataValidade;
        this.unidadeMedida = unidadeMedida;
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
                produto.getDataValidade(),
                produto.getUnidadeMedida()
                );
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodigoProduto() {
        return codigoProduto;
    }

    public void setCodigoProduto(String codigoProduto) {
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

    public int getUnidadesCaixa() {
        return unidadesCaixa;
    }

    public void setUnidadesCaixa(int unidadesCaixa) {
        this.unidadesCaixa = unidadesCaixa;
    }

    public Double getPesoUnidade() {
        return pesoUnidade;
    }

    public void setPesoUnidade(Double pesoUnidade) {
        this.pesoUnidade = pesoUnidade;
    }

    public LocalDate getDataValidade() {
        return dataValidade;
    }

    public void setDataValidade(LocalDate dataValidade) {
        this.dataValidade = dataValidade;
    }

    public UnidadeMedida getUnidadeMedida() {
        return unidadeMedida;
    }

    public void setUnidadeMedida(UnidadeMedida unidadeMedida) {
        this.unidadeMedida = unidadeMedida;
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
                ", unidadeMedida=" + unidadeMedida +
                '}';
    }
}
