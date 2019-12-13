package br.com.hbsis.produtos;


import br.com.hbsis.linha.categoria.LinhaCategoria;
import br.com.hbsis.util.UnidadeMedida;

import javax.persistence.*;
import java.time.LocalDate;


@Entity
@Table(name = "seg_produtos")
public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "codigo")
    private String codigoProduto;
    @Column(name = "nome")
    private String nomeProduto;
    @Column(name = "preco")
    private Double precoProduto;
    @ManyToOne
    @JoinColumn(name = "id_linha_categoria", referencedColumnName = "id")
    private LinhaCategoria linhaCategoria;
    @Column(name = "unidades_caixa")
    private int unidadesCaixa;
    @Column(name = "peso_unidade")
    private Double pesoUnidade;
    @Column(name = "data_validade")
    private LocalDate dataValidade;
    @Column(name = "unidade_medida")
    @Enumerated(EnumType.STRING)
    private UnidadeMedida unidadeMedida;

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

    public LinhaCategoria getLinhaCategoria() {
        return linhaCategoria;
    }

    public void setLinhaCategoria(LinhaCategoria linhaCategoria) {
        this.linhaCategoria = linhaCategoria;
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
        return "Produto{" +
                "id=" + id +
                ", codigoProduto=" + codigoProduto +
                ", nomeProduto='" + nomeProduto + '\'' +
                ", precoProduto=" + precoProduto +
                ", linhaCategoria=" + linhaCategoria +
                ", unidadesCaixa=" + unidadesCaixa +
                ", pesoUnidade=" + pesoUnidade +
                ", dataValidade=" + dataValidade +
                ", unidadeMedida=" + unidadeMedida +
                '}';
    }
}
