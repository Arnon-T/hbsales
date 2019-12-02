package br.com.hbsis.periodo.venda;

import br.com.hbsis.produtos.ProdutoDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/periodovendas")
public class PeriodoVendaRest {

    private static final Logger LOGGER = LoggerFactory.getLogger(PeriodoVendaRest.class);

    private final PeriodoVendaService periodoVendaService;

    @Autowired
    public PeriodoVendaRest (PeriodoVendaService periodoVendaService){
        this.periodoVendaService = periodoVendaService;
    }


    @PostMapping
    public PeriodoVendaDTO save(@RequestBody PeriodoVendaDTO periodoVendaDTO){

        periodoVendaService.save(periodoVendaDTO);
    }


}
