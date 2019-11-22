package br.com.hbsis.fornecedor;

public class FornecedorDTO {
    private Long id;
    private String razaoSocial;
    private String cnpj;
    private String nomeFantasia;
    private String endereco;
    private int telefone;
    private String email;

    public FornecedorDTO(){
    }

    public FornecedorDTO(String razaoSocial, String cnpj, String endereco, int telefone, String email) {
        this.razaoSocial = razaoSocial;
        this.cnpj = cnpj;
        this.endereco = endereco;
        this.telefone = telefone;
        this.email = email;
    }

    public FornecedorDTO(Long id, String razaoSocial, String nomeFantasia, String endereco, int telefone, String email) {
        this.id = id;
        this.razaoSocial = razaoSocial;
        this.cnpj = cnpj;
        this.nomeFantasia = nomeFantasia;
        this.endereco = endereco;
        this.telefone = telefone;
        this.email = email;
    }



    public static FornecedorDTO of(Fornecedor fornecedor){
        return new FornecedorDTO(
                fornecedor.getId(),
                fornecedor.getRazaoSocial(),
                fornecedor.getNomeFantasia(),
                fornecedor.getEndereco(),
                fornecedor.getTelefone(),
                fornecedor.getEmail()
        );
    }

    public Long getId() {
        return id;
    }

    public String getRazaoSocial() {
        return razaoSocial;
    }

    public void setRazaoSocial(String razaoSocial) {
        this.razaoSocial = razaoSocial;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public String getNomeFantasia() {
        return nomeFantasia;
    }

    public void setNomeFantasia(String nomeFantasia) {
        this.nomeFantasia = nomeFantasia;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public int getTelefone() {
        return telefone;
    }

    public void setTelefone(int telefone) {
        this.telefone = telefone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "FornecedorDTO{" +
                "id=" + id +
                ", razaoSocial='" + razaoSocial + '\'' +
                ", cnpj='" + cnpj + '\'' +
                ", nomeFantasia='" + nomeFantasia + '\'' +
                ", endereco='" + endereco + '\'' +
                ", telefone=" + telefone +
                ", email='" + email + '\'' +
                '}';
    }
}
