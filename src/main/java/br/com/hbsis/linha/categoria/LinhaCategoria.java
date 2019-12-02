package br.com.hbsis.linha.categoria;

import br.com.hbsis.categoria.produto.CategoriaProduto;

import javax.persistence.*;

@Entity
@Table(name = "seg_linhas_categorias")

public class LinhaCategoria {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_linha_categoria")
    private Long idLinhaCategoria;

    @ManyToOne
    @JoinColumn(name = "id_categoria_produto", referencedColumnName = "id_categoria_produto")
    CategoriaProduto categoriaProduto;

    @Column(name = "nome_linha_categoria")
    String nomeLinhaCategoria;

    public void setIdLinhaCategoria(Long idLinhaCategoria) {
        this.idLinhaCategoria = idLinhaCategoria;
    }

    public Long getIdLinhaCategoria() {
        return idLinhaCategoria;
    }

    public CategoriaProduto getCategoriaProduto() {
        return categoriaProduto;
    }

    public void setCategoriaProduto(CategoriaProduto categoriaProduto) {
        this.categoriaProduto = categoriaProduto;
    }

    public String getNomeLinhaCategoria() {
        return nomeLinhaCategoria;
    }

    public void setNomeLinhaCategoria(String nomeLinhaCategoria) {
        this.nomeLinhaCategoria = nomeLinhaCategoria;
    }

    @Override
    public String toString() {
        return "LinhaCategoria{" +
                "idLinhaCategoria=" + idLinhaCategoria +
                ", categoriaProdutoId=" + categoriaProduto.toString() +
                ", nomeLinhaCategoria='" + nomeLinhaCategoria + '\'' +
                '}';
    }
}
