package br.com.hbsis.fornecedor;

import javax.persistence.*;
import javax.validation.constraints.Email;


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
    @Column(name= "id")
    private Long id;
    @Column(name = "razao_social", unique = false, nullable = false, length = 100)
    private String razaoSocial;
    @Column(name = "cnpj" , unique = false, nullable = false, length = 14)
    private String cnpj;
    @Column(name = "nome_fantasia", unique = true, nullable = false, length = 100)
    private String nomeFantasia;
    @Column(name = "endereco", unique = false, nullable = false, length = 100)
    private String endereco;
    @Column(name = "telefone", unique = false, nullable = false, length = 12)
    private String telefone;
    @Column(name = "email", unique = false, nullable = false, length = 50)
    @Email
    private String email;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
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
