package br.com.hbsis.categoria.produto;

import br.com.hbsis.fornecedor.Fornecedor;

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
    @Column(name = "id")
    private Long id;

    @Column(name = "codigo", unique = true, nullable = false, length = 10)
    private String codigoCategoriaProduto;

    @Column(name = "nome", unique = false, nullable = false, length = 100)
    private String nomeCategoriaProduto;

    @ManyToOne
    @JoinColumn(name = "id_fornecedor", referencedColumnName = "id")
    private Fornecedor fornecedor;

    public Long getId() {
        return id;
    }

    public String getCodigoCategoriaProduto() {
        return codigoCategoriaProduto;
    }

    public void setCodigoCategoriaProduto(String codigoCategoriaProduto) {
        this.codigoCategoriaProduto = codigoCategoriaProduto;
    }

    public String getNomeCategoriaProduto() {
        return nomeCategoriaProduto;
    }

    public void setNomeCategoriaProduto(String nomeCategoriaProduto) {
        this.nomeCategoriaProduto = nomeCategoriaProduto;
    }

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
                ", fornecedor=" + fornecedor.toString() +
                '}';
    }
}
