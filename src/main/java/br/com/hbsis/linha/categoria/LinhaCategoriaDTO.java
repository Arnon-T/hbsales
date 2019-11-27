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

    public static CategoriaProdutoDTO of(Long idLinhaCategoria, CategoriaProduto categoriaProduto, String nomeLinhaCategoria){
        
    }

}
