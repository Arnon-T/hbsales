package br.com.hbsis.categoria.produto;

import br.com.hbsis.fornecedor.Fornecedor;

public class CategoriaProdutoDTO {
    private Long id;
    private String nomeCategoriaProduto;
    private Long codigoCategoriaProduto;
    private Long fornecedorId;


    public CategoriaProdutoDTO() {
    }

    public CategoriaProdutoDTO(String nomeCategoriaProduto, Long codigoCategoriaProduto, Long fornecedorId) {
        this.nomeCategoriaProduto = nomeCategoriaProduto;
        this.codigoCategoriaProduto = codigoCategoriaProduto;
        this.fornecedorId = fornecedorId;
    }

    public CategoriaProdutoDTO(Long id, String nomeCategoriaProduto, Long codigoCategoriaProduto, Long fornecedorId) {
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
                categoriaProduto.g
        );
    }



    public Long getId() {
        return id;
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

    public Long getCodigoCategoriaProduto() { return codigoCategoriaProduto; }

    public void setCodigoCategoriaProduto(Long codigoCategoriaProduto) { this.codigoCategoriaProduto = codigoCategoriaProduto; }

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

