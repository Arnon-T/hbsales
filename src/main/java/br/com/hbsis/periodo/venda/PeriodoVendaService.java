package br.com.hbsis.periodo.venda;

import br.com.hbsis.fornecedor.FornecedorDTO;
import br.com.hbsis.fornecedor.IFornecedorRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class PeriodoVendaService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PeriodoVendaService.class);

    private final IPeriodoVendaRepository iPeriodoVendaRepository;
    private final IFornecedorRepository iFornecedorRepository;

    public PeriodoVendaService(IPeriodoVendaRepository iPeriodoVendaRepository, IFornecedorRepository iFornecedorRepository) {
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
        periodoVenda.setDataRetirada(periodoVendaDTO.getDataRetirada());

        this.iPeriodoVendaRepository.save(periodoVenda);

        return PeriodoVendaDTO.of(periodoVenda);
    }

    public void validate(PeriodoVendaDTO periodoVendaDTO){
        LOGGER.info("Validando Periodo Venda");

        if(periodoVendaDTO == null){
            throw new IllegalArgumentException("Periodo Venda não pode ser nulo.");
        }
    }

    public PeriodoVendaDTO update(PeriodoVendaDTO periodoVendaDTO, Long id){

        Optional<PeriodoVenda> periodoVendaOptional = this.iPeriodoVendaRepository.findById(id);

        if(periodoVendaOptional.isPresent()){
            PeriodoVenda periodoVendaExistente = periodoVendaOptional.get();

            LOGGER.info("Atualizando Periodo Venda do Fornecedor...[{}]", periodoVendaOptional.get().fornecedor.getId());
            LOGGER.debug("Payload... [{}]", periodoVendaDTO);
            LOGGER.debug("Periodo existente... [{}]", periodoVendaExistente);

            periodoVendaExistente.setId(periodoVendaDTO.getId());
            periodoVendaExistente.setFornecedor(iFornecedorRepository.findById(periodoVendaOptional.get().getFornecedor().getId()).get());
            periodoVendaExistente.setDataInicio(periodoVendaDTO.getDataInicio());
            periodoVendaExistente.setDataFim(periodoVendaDTO.getDataFim());
            periodoVendaExistente.setDataRetirada(periodoVendaDTO.getDataRetirada());

            this.iPeriodoVendaRepository.save(periodoVendaExistente);

            return PeriodoVendaDTO.of(periodoVendaExistente);
        }
      throw new IllegalArgumentException(String.format("Não foi possível atualizar o período de venda do fornecedor {}.", id));
    }

    public PeriodoVendaDTO findById(Long id){
        Optional<PeriodoVenda> periodoVendaOptional = this.iPeriodoVendaRepository.findById(id);

        if(periodoVendaOptional.isPresent()){
            return PeriodoVendaDTO.of(periodoVendaOptional.get());
        }
        throw new  IllegalArgumentException(String.format("Periodo de vendas de ID {} não encontrado.", id));
    }

    public void delete(Long id){

        LOGGER.info("Excluindo periodo de vendas de ID: [{}]", id);

        this.iPeriodoVendaRepository.deleteById(id);
    }

    public boolean periodoVendaAtivo(FornecedorDTO fornecedorDTO){
        LocalDate diaHoje = LocalDate.now();

        Optional<PeriodoVenda> periodoVendaOptional = this.iPeriodoVendaRepository.findByFornecedor_Id(fornecedorDTO.getId());

        if(periodoVendaOptional.isPresent()){
            PeriodoVenda periodoVenda = periodoVendaOptional.get();

            if(diaHoje.compareTo(periodoVenda.getDataInicio()) >= 1  && diaHoje.compareTo(periodoVenda.getDataFim()) <= 0){
                return true;
            }
            else{
                return false;
            }

        }
        throw new IllegalArgumentException(String.format("Não foi possível verificar o período de compras."));
    }


}
