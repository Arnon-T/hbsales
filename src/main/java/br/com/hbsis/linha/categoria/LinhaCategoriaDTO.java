package br.com.hbsis.linha.categoria;

public class LinhaCategoriaDTO {

    private Long idLinhaCategoria;
    private Long categoriaProdutoId;
    private String nomeLinhaCategoria;

    public LinhaCategoriaDTO(){ }

    public LinhaCategoriaDTO(Long idLinhaCategoria, Long categoriaProdutoId, String nomeLinhaCategoria){
        this.idLinhaCategoria = idLinhaCategoria;
        this.categoriaProdutoId = categoriaProdutoId;
        this.nomeLinhaCategoria = nomeLinhaCategoria;
    }

    public static LinhaCategoriaDTO of(LinhaCategoria linhaCategoria){
        return new LinhaCategoriaDTO(
                linhaCategoria.getIdLinhaCategoria(),
                linhaCategoria.getCategoriaProduto().getId(),
                linhaCategoria.getNomeLinhaCategoria()
        );
    }

    public Long getIdLinhaCategoria() {
        return idLinhaCategoria;
    }

    public Long getCategoriaProdutoId() { return categoriaProdutoId; }

    public void setCategoriaProduto(Long categoriaProdutoId) { this.categoriaProdutoId = categoriaProdutoId;}

    public String getNomeLinhaCategoria() { return nomeLinhaCategoria; }

    public void setNomeLinhaCategoria(String nomeLinhaCategoria) { this.nomeLinhaCategoria = nomeLinhaCategoria; }

    @Override
    public String toString() {
        return "LinhaCategoriaDTO{" +
                "idLinhaCategoria=" + idLinhaCategoria +
                ", categoriaProdutoId=" + categoriaProdutoId +
                ", nomeLinhaCategoria='" + nomeLinhaCategoria + '\'' +
                '}';
    }
}
