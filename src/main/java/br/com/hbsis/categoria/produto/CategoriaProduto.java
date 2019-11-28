package br.com.hbsis.categoria.produto;

import br.com.hbsis.fornecedor.Fornecedor;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.opencsv.bean.CsvBindAndJoinByPosition;
import com.opencsv.bean.CsvBindByName;

import javax.persistence.*;


@Entity
@Table(name = "seg_categorias_produtos")

public class CategoriaProduto {
    /*
     A1. O Cadastro deve conter:
    A) Nome da categoria
    B) Fornecedor da categoria
    */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_categoria_produto")
    private Long id;

    @Column(name = "codigo_categoria_produto", unique = true, nullable = false)
    private Long codigoCategoriaProduto;

    @Column(name = "nome_categoria_produto", unique = false, nullable = false, length = 100)
    private String nomeCategoriaProduto;

    @ManyToOne
    @JoinColumn(name = "id_fornecedor_categoria_produto", referencedColumnName = "id_fornecedor")
    @JsonDeserialize
    private Long fornecedorId;

    public Long getId() {
        return id;
    }

    public Long getCodigoCategoriaProduto() {
        return codigoCategoriaProduto;
    }

    public void setCodigoCategoriaProduto(Long codigoCategoriaProduto) {
        this.codigoCategoriaProduto = codigoCategoriaProduto;
    }

    public String getNomeCategoriaProduto() {
        return nomeCategoriaProduto;
    }

    public void setNomeCategoriaProduto(String nomeCategoriaProduto) {
        this.nomeCategoriaProduto = nomeCategoriaProduto;
    }

    public Long getFornecedorId() {
        return fornecedorId;
    }

    public void setFornecedorId(Long fornecedorId) {
        this.fornecedorId = fornecedorId;
    }

    @Override
    public String toString() {
        return "CategoriaProduto{" +
                "id=" + id +
                ", codigoCategoriaProduto=" + codigoCategoriaProduto +
                ", nomeCategoriaProduto='" + nomeCategoriaProduto + '\'' +
                ", fornecedor=" + fornecedorId +
                '}';
    }
}
