package br.com.hbsis.linha.categoria;

public class LinhaCategoriaDTO {

    private Long idLinhaCategoria;
    private String codigoLinhaCategoria;
    private Long categoriaProdutoId;
    private String nomeLinhaCategoria;

    public LinhaCategoriaDTO(){ }

    public LinhaCategoriaDTO(Long idLinhaCategoria, String codigoLinhaCategoria, Long categoriaProdutoId, String nomeLinhaCategoria) {
        this.idLinhaCategoria = idLinhaCategoria;
        this.codigoLinhaCategoria = codigoLinhaCategoria;
        this.categoriaProdutoId = categoriaProdutoId;
        this.nomeLinhaCategoria = nomeLinhaCategoria;
    }

    public static LinhaCategoriaDTO of(LinhaCategoria linhaCategoria){
        return new LinhaCategoriaDTO(
                linhaCategoria.getIdLinhaCategoria(),
                linhaCategoria.getCodigoLinhaCategoria(),
                linhaCategoria.getCategoriaProduto().getId(),
                linhaCategoria.getNomeLinhaCategoria()
        );
    }

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

    public Long getCategoriaProdutoId() {
        return categoriaProdutoId;
    }

    public void setCategoriaProdutoId(Long categoriaProdutoId) {
        this.categoriaProdutoId = categoriaProdutoId;
    }

    public String getNomeLinhaCategoria() {
        return nomeLinhaCategoria;
    }

    public void setNomeLinhaCategoria(String nomeLinhaCategoria) {
        this.nomeLinhaCategoria = nomeLinhaCategoria;
    }

    @Override
    public String toString() {
        return "LinhaCategoriaDTO{" +
                "idLinhaCategoria=" + idLinhaCategoria +
                ", codigoLinhaCategoria='" + codigoLinhaCategoria + '\'' +
                ", categoriaProdutoId=" + categoriaProdutoId +
                ", nomeLinhaCategoria='" + nomeLinhaCategoria + '\'' +
                '}';
    }
}
