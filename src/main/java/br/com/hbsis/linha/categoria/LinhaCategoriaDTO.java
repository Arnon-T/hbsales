package br.com.hbsis.linha.categoria;

import br.com.hbsis.categoria.produto.CategoriaProduto;
import br.com.hbsis.categoria.produto.CategoriaProdutoDTO;

public class LinhaCategoriaDTO {

    private Long idLinhaCategoria;
    private CategoriaProduto categoriaProduto;
    private String nomeLinhaCategoria;

    public LinhaCategoriaDTO(){ }

    public LinhaCategoriaDTO(Long idLinhaCategoria, CategoriaProduto categoriaProduto, String nomeLinhaCategoria){
        this.idLinhaCategoria = idLinhaCategoria;
        this.categoriaProduto = categoriaProduto;
        this.nomeLinhaCategoria = nomeLinhaCategoria;
    }

    public LinhaCategoriaDTO(CategoriaProduto categoriaProduto, String nomeLinhaCategoria) {
        this.categoriaProduto = categoriaProduto;
        this.nomeLinhaCategoria = nomeLinhaCategoria;
    }

    public static LinhaCategoriaDTO of(LinhaCategoria linhaCategoria){
        return new LinhaCategoriaDTO(
                linhaCategoria.getIdLinhaCategoria(),
                linhaCategoria.getCategoriaProduto(),
                linhaCategoria.getNomeLinhaCategoria()
        );
    }

    public Long getIdLinhaCategoria() {
        return idLinhaCategoria;
    }

    public CategoriaProduto getCategoriaProduto() { return categoriaProduto; }

    public void setCategoriaProduto(CategoriaProduto categoriaProduto) { this.categoriaProduto = categoriaProduto;}

    public String getNomeLinhaCategoria() { return nomeLinhaCategoria; }

    public void setNomeLinhaCategoria(String nomeLinhaCategoria) { this.nomeLinhaCategoria = nomeLinhaCategoria; }

    @Override
    public String toString() {
        return "LinhaCategoriaDTO{" +
                "idLinhaCategoria=" + idLinhaCategoria +
                ", categoriaProduto=" + categoriaProduto +
                ", nomeLinhaCategoria='" + nomeLinhaCategoria + '\'' +
                '}';
    }
}
