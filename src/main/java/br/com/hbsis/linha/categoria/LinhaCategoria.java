package br.com.hbsis.linha.categoria;

import br.com.hbsis.categoria.produto.CategoriaProduto;

import javax.persistence.*;

@Entity
@Table(name = "seg_linhas_categorias")

public class LinhaCategoria {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long idLinhaCategoria;

    @Column(name = "codigo")
    String codigoLinhaCategoria;

    @ManyToOne
    @JoinColumn(name = "id_categoria_produto", referencedColumnName = "id")
    CategoriaProduto categoriaProduto;

    @Column(name = "nome")
    String nomeLinhaCategoria;

    public Long getIdLinhaCategoria() {
        return idLinhaCategoria;
    }

    public void setIdLinhaCategoria(Long idLinhaCategoria) {
        this.idLinhaCategoria = idLinhaCategoria;
    }

    public String getCodigoLinhaCategoria() {
        return codigoLinhaCategoria;
    }

    public void setCodigoLinhaCategoria(String codigoLinhaCategoria) {
        this.codigoLinhaCategoria = codigoLinhaCategoria;
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
                ", codigoLinhaCategoria='" + codigoLinhaCategoria + '\'' +
                ", categoriaProduto=" + categoriaProduto +
                ", nomeLinhaCategoria='" + nomeLinhaCategoria + '\'' +
                '}';
    }
}
