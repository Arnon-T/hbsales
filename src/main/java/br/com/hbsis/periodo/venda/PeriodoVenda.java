package br.com.hbsis.periodo.venda;

import br.com.hbsis.fornecedor.Fornecedor;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "seg_periodo_vendas")
public class PeriodoVenda {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @ManyToOne
    @JoinColumn(name = "id_fornecedor", referencedColumnName = "id_fornecedor")
    Fornecedor fornecedor;
    @Column(name = "data_inicio")
    LocalDate dataInicio;
    @Column(name = "data_fim")
    LocalDate dataFim;
    @Column(name = "data_retirada")
    LocalDate dataRetirada;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Fornecedor getFornecedor() {
        return fornecedor;
    }

    public void setFornecedor(Fornecedor fornecedor) {
        this.fornecedor = fornecedor;
    }

    public LocalDate getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(LocalDate dataInicio) {
        this.dataInicio = dataInicio;
    }

    public LocalDate getDataFim() {
        return dataFim;
    }

    public void setDataFim(LocalDate dataFim) {
        this.dataFim = dataFim;
    }

    public LocalDate getDataRetirada() {
        return dataRetirada;
    }

    public void setDataRetirada(LocalDate dataRetirada) {
        this.dataRetirada = dataRetirada;
    }

    @Override
    public String toString() {
        return "PeriodoVenda{" +
                "id=" + id +
                ", fornecedor=" + fornecedor.toString() +
                ", dataInicio=" + dataInicio +
                ", dataFim=" + dataFim +
                ", dataRetirada=" + dataRetirada +
                '}';
    }

    /*
id
Fornecedor
* Inicio
* fim
* retirada*/

}
