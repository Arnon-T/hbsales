package br.com.hbsis.linha.categoria;

import br.com.hbsis.categoria.produto.CategoriaProduto;

import javax.persistence.*;

/*A1. O Cadastro deve conter:
a) Código da Linha da Categoria de produtos
b) Categoria da linha
c) Nome
A2. Uma categoria pode ter N linhas, porém uma linha pode ter apenas UMA categoria.
*/
@Entity
@Table(name= "seg_linhas_categorias")

public class LinhaCategoria {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name= "id_linha_categoria")
    private Long idLinhaCategoria;

    @ManyToOne
    @JoinColumn(name= "id_categoria_produto", referencedColumnName = "id_categoria_produto")
    CategoriaProduto categoriaProduto;

    @Column(name= "nome_linha_categoria")
    String nomeLinhaCategoria;

    public Long getIdLinhasCategorias() {
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
                "idLinhasCategorias=" + idLinhaCategoria +
                ", categoriaProduto=" + categoriaProduto +
                ", nomeLinhaCategoria='" + nomeLinhaCategoria + '\'' +
                '}';
    }
}
