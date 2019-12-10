package br.com.hbsis.periodo.venda;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

       return periodoVendaService.save(periodoVendaDTO);
    }

    @PutMapping("/{id}")
    public PeriodoVendaDTO update(@PathVariable("id") Long id, @RequestBody PeriodoVendaDTO periodoVendaDTO){
        return this.periodoVendaService.update(periodoVendaDTO, id);
    }

    @GetMapping("/{id}")
    public PeriodoVendaDTO findById(@PathVariable("id") Long id){
        LOGGER.info("Procurando periodo de vendas de ID: [{}]", id);

        return this.periodoVendaService.findById(id);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Long id){
        this.periodoVendaService.delete(id);
    }

}
