package br.com.hbsis.categoria.produto;

import br.com.hbsis.fornecedor.Fornecedor;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.opencsv.bean.CsvBindAndJoinByPosition;
import com.opencsv.bean.CsvBindByName;

import javax.persistence.*;


@Entity
@Table(name= "seg_categorias_produtos")

public class CategoriaProduto {
/*
 A1. O Cadastro deve conter:
A) Nome da categoria
B) Fornecedor da categoria
*/
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @CsvBindByName(column = "codigo_categoria_produto")
    @Column(name = "id_categoria_produto")
    private Long id;

    @CsvBindByName(column = "codigo_categoria_produto")
    @Column(name = "codigo_categoria_produto", unique = true, nullable = false)
    private Long codigoCategoriaProduto;

    @CsvBindByName(column = "nome_categoria_produto")
    @Column(name = "nome_categoria_produto", unique = false, nullable = false, length = 100)
    private String nomeCategoriaProduto;

    @ManyToOne
    @JoinColumn(name = "id_fornecedor_categoria_produto", referencedColumnName = "id_fornecedor")
    @JsonDeserialize
    @CsvBindByName(column = "id_fornecedor_categoria_produto")
    private Fornecedor fornecedor;

    public Long getId() {
        return id;
    }

    public Long getCodigoCategoriaProduto() { return codigoCategoriaProduto; }

    public void setCodigoCategoriaProduto(Long codigoCategoriaProduto) { this.codigoCategoriaProduto = codigoCategoriaProduto; }

    public String getNomeCategoriaProduto() {
        return nomeCategoriaProduto;
    }

    public void setNomeCategoriaProduto(String nomeCategoriaProduto) { this.nomeCategoriaProduto = nomeCategoriaProduto; }

    public Fornecedor getFornecedor() {
        return fornecedor;
    }

    public void setFornecedor(Fornecedor fornecedor) {
        this.fornecedor = fornecedor;
    }

    @Override
    public String toString() {
        return "CategoriaProduto{" +
                "id=" + id +
                ", codigoCategoriaProduto=" + codigoCategoriaProduto +
                ", nomeCategoriaProduto='" + nomeCategoriaProduto + '\'' +
                ", fornecedor=" + fornecedor +
                '}';
    }
}
