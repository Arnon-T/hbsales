package br.com.hbsis.periodo.venda;

import org.apache.tomcat.jni.Local;

import java.time.LocalDate;

public class PeriodoVendaDTO {

    Long id;
    Long fornecedorId;
    LocalDate dataInicio;
    LocalDate dataFim;
    LocalDate dataRetirada;

    public PeriodoVendaDTO(){}

    public PeriodoVendaDTO(Long id, Long fornecedorId, LocalDate dataInicio, LocalDate dataFim, LocalDate dataRetirada) {
        this.id = id;
        this.fornecedorId = fornecedorId;
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
        this.dataRetirada = dataRetirada;
    }

    public static PeriodoVendaDTO of(PeriodoVenda periodoVenda){
        return new PeriodoVendaDTO(
                periodoVenda.getId(),
                periodoVenda.getFornecedor().getId(),
                periodoVenda.getDataInicio(),
                periodoVenda.getDataFim(),
                periodoVenda.getDataRetirada()
        );
    }

    public Long getId() {
        return id;
    }

    public Long getFornecedorId() {
        return fornecedorId;
    }

    public void setFornecedorId(Long fornecedorId) {
        this.fornecedorId = fornecedorId;
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
        return "PeriodoVendaDTO{" +
                "id=" + id +
                ", fornecedorId=" + fornecedorId +
                ", dataInicio=" + dataInicio +
                ", dataFim=" + dataFim +
                ", dataRetirada=" + dataRetirada +
                '}';
    }
}
