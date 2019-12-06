package br.com.hbsis.categoria.produto;

public class CategoriaProdutoDTO {
    private Long id;
    private String nomeCategoriaProduto;
    private String codigoCategoriaProduto;
    private Long fornecedorId;


    public CategoriaProdutoDTO() {
    }

    public CategoriaProdutoDTO(Long id, String nomeCategoriaProduto, String codigoCategoriaProduto, Long fornecedorId) {
        this.id = id;
        this.nomeCategoriaProduto = nomeCategoriaProduto;
        this.codigoCategoriaProduto = codigoCategoriaProduto;
        this.fornecedorId = fornecedorId;
    }

    public static CategoriaProdutoDTO of(CategoriaProduto categoriaProduto) {
        return new CategoriaProdutoDTO(
                categoriaProduto.getId(),
                categoriaProduto.getNomeCategoriaProduto(),
                categoriaProduto.getCodigoCategoriaProduto(),
                categoriaProduto.getFornecedor().getId()
        );
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomeCategoriaProduto() {
        return nomeCategoriaProduto;
    }

    public void setNomeCategoriaProduto(String nomeCategoriaProduto) {
        this.nomeCategoriaProduto = nomeCategoriaProduto;
    }

    public String getCodigoCategoriaProduto() {
        return codigoCategoriaProduto;
    }

    public void setCodigoCategoriaProduto(String codigoCategoriaProduto) {
        this.codigoCategoriaProduto = codigoCategoriaProduto;
    }

    public Long getFornecedorId() {
        return fornecedorId;
    }

    public void setFornecedorId(Long fornecedorId) {
        this.fornecedorId = fornecedorId;
    }

    @Override
    public String toString() {
        return "CategoriaProdutoDTO{" +
                "id=" + id +
                ", nomeCategoriaProduto='" + nomeCategoriaProduto + '\'' +
                ", codigoCategoriaProduto=" + codigoCategoriaProduto +
                ", fornecedor=" + fornecedorId +
                '}';
    }
}

