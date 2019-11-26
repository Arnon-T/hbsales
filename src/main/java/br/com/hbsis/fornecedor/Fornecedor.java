package br.com.hbsis.fornecedor;

import javax.persistence.*;


@Entity
@Table(name= "seg_fornecedores")

public class Fornecedor {
/*a.	Razão Social
b.	CNPJ
c.	Nome Fantasia
d.	Endereço
e.	Telefone de Contato
f.	E-mail de contato
*/
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name= "id_fornecedor")
    private Long id;
    @Column(name = "razao_social_fornecedor", unique = false, nullable = false, length = 100)
    private String razaoSocial;
    @Column(name = "cnpj_fornecedor" , unique = true, nullable = false, length = 20)
    private String cnpj;
    @Column(name = "nome_fantasia_fornecedor", unique = true, nullable = false, length = 100)
    private String nomeFantasia;
    @Column(name = "endereco_fornecedor", unique = false, nullable = false, length = 255)
    private String endereco;
    @Column(name = "telefone_fornecedor", unique = false, nullable = false, length = 20)
    private int telefone;
    @Column(name = "email_fornecedor", unique = false, nullable = false)
    private String email;

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
        return "Fornecedor{" +
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
