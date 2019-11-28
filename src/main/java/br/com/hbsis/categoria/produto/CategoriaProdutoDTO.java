package br.com.hbsis.categoria.produto;

import br.com.hbsis.fornecedor.Fornecedor;

public class CategoriaProdutoDTO {
    private Long id;
    private String nomeCategoriaProduto;
    private Long codigoCategoriaProduto;
    private Fornecedor fornecedor;


    public CategoriaProdutoDTO() {
    }

    public CategoriaProdutoDTO(String nomeCategoriaProduto, Long codigoCategoriaProduto, Fornecedor fornecedor) {
        this.nomeCategoriaProduto = nomeCategoriaProduto;
        this.codigoCategoriaProduto = codigoCategoriaProduto;
        this.fornecedor = fornecedor;
    }

    public CategoriaProdutoDTO(Long id, String nomeCategoriaProduto, Long codigoCategoriaProduto, Fornecedor fornecedor) {
        this.id = id;
        this.nomeCategoriaProduto = nomeCategoriaProduto;
        this.codigoCategoriaProduto = codigoCategoriaProduto;
        this.fornecedor = fornecedor;
    }


    public static CategoriaProdutoDTO of(CategoriaProduto categoriaProduto) {
        return new CategoriaProdutoDTO(
                categoriaProduto.getId(),
                categoriaProduto.getNomeCategoriaProduto(),
                categoriaProduto.getCodigoCategoriaProduto(),
                categoriaProduto.getFornecedor()
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

    public Fornecedor getFornecedor() {
        return fornecedor;
    }

    public void setFornecedor(Fornecedor fornecedor) {
        this.fornecedor = fornecedor;
    }

    public Long getCodigoCategoriaProduto() { return codigoCategoriaProduto; }

    public void setCodigoCategoriaProduto(Long codigoCategoriaProduto) { this.codigoCategoriaProduto = codigoCategoriaProduto; }

    @Override
    public String toString() {
        return "CategoriaProdutoDTO{" +
                "id=" + id +
                ", nomeCategoriaProduto='" + nomeCategoriaProduto + '\'' +
                ", codigoCategoriaProduto=" + codigoCategoriaProduto +
                ", fornecedor=" + fornecedor.getId() +
                '}';
    }
}

