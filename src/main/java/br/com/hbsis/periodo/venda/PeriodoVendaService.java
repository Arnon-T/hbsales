package br.com.hbsis.periodo.venda;

import br.com.hbsis.fornecedor.IFornecedorRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class PeriodoVendaService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PeriodoVendaService.class);

    private final IPeriodoVendaRepository iPeriodoVendaRepository;
    private final IFornecedorRepository iFornecedorRepository;

    public PeriodoVendaService(IPeriodoVendaRepository iPeriodoVendaRepository, IFornecedorRepository iFornecedorRepository){
        this.iPeriodoVendaRepository = iPeriodoVendaRepository;
        this.iFornecedorRepository = iFornecedorRepository;
    }

    public PeriodoVendaDTO save(PeriodoVendaDTO periodoVendaDTO){

        LOGGER.info("Salvando Periodo Vendas");
        LOGGER.debug("Payload [{}]", periodoVendaDTO);

        this.validate(periodoVendaDTO);

        PeriodoVenda periodoVenda = new PeriodoVenda();

        periodoVenda.setFornecedor(iFornecedorRepository.findById(periodoVendaDTO.fornecedorId).get());
        periodoVenda.setDataInicio(periodoVendaDTO.getDataInicio());
        periodoVenda.setDataFim(periodoVendaDTO.getDataFim());
        periodoVenda.setDataRetirada(periodoVenda.getDataRetirada());

        this.iPeriodoVendaRepository.save(periodoVenda);

        return PeriodoVendaDTO.of(periodoVenda);
    }

    public void validate(PeriodoVendaDTO periodoVendaDTO){
        LOGGER.info("Validando Periodo Venda");

        if(periodoVendaDTO == null){
            throw new IllegalArgumentException("Periodo Venda não pode ser nulo.");
        }
    }

}
