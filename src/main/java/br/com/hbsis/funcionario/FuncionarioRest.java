package br.com.hbsis.funcionario;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;

@RestController
@RequestMapping("/funcionarios")
public class FuncionarioRest {

    Logger LOGGER = LoggerFactory.getLogger(FuncionarioRest.class);

    private final FuncionarioService funcionarioService;

    @Autowired
    public FuncionarioRest(FuncionarioService funcionarioService){
        this.funcionarioService = funcionarioService;
    }

    @PostMapping
    public FuncionarioDTO save(@RequestBody FuncionarioDTO funcionarioDTO){

        LOGGER.info("Salvando Funcionario.");
        LOGGER.debug("Payload: [{}]", funcionarioDTO);

        return this.funcionarioService.save(funcionarioDTO);
    }

    @GetMapping("/{id}")
    public FuncionarioDTO findById(@PathParam("id") Long id){

        LOGGER.info("Buscando Funcionario de ID...[{}]", id);

        return this.funcionarioService.findById(id);
    }

    @PutMapping("/{id}")
    public FuncionarioDTO update(@PathParam("id") Long id, FuncionarioDTO funcionarioDTO){

        LOGGER.info("Atualizando funcionario com ID...[{}]", id);
        LOGGER.debug("Payload: {}", funcionarioDTO);

        return this.funcionarioService.update(id, funcionarioDTO);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathParam("id") Long id){
        LOGGER.info("Deletando por ID...{}", id);

        this.funcionarioService.delete(id);
    }





}
