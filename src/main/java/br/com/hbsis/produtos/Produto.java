package br.com.hbsis.produtos;

import br.com.hbsis.linha.categoria.LinhaCategoria;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;

/*A1. O Cadastro deve conter:
a.	Código do Produto
b.	Nome
c.	Preço
d.	Linha de Categoria
e.	Unidade por Caixa
f.	Peso por Unidade
g.	Validade
A2. Uma linha pode ter N produtos, porém um produto pode ter apenas UMA linha.
*/
@Entity
@Table(name = "seg_produtos")
public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    Long id;
    @Column(name = "codigo")
    Long codigoProduto;
    @Column(name = "nome")
    String nomeProduto;
    @Column(name = "preco")
    Double precoProduto;
    @ManyToOne
    @JoinColumn(name = "id_linha_categoria", referencedColumnName = "id_linha_categoria")
    LinhaCategoria linhaCategoria;
    @Column(name = "unidades_caixa")
    Double unidadesCaixa;
    @Column(name = "peso_unidade")
    Double pesoUnidade;
    @Column(name = "data_validade")
    String dataValidade;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public LinhaCategoria getLinhaCategoria() {
        return linhaCategoria;
    }

    public void setLinhaCategoria(LinhaCategoria linhaCategoria) {
        this.linhaCategoria = linhaCategoria;
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
        return "Produto{" +
                "id=" + id +
                ", codigoProduto=" + codigoProduto +
                ", nomeProduto='" + nomeProduto + '\'' +
                ", precoProduto=" + precoProduto +
                ", linhaCategoria=" + linhaCategoria.toString() +
                ", unidadesCaixa=" + unidadesCaixa +
                ", pesoUnidade=" + pesoUnidade +
                ", dataValidade='" + dataValidade + '\'' +
                '}';
    }
}
