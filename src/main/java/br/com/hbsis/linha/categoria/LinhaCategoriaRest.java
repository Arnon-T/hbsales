package br.com.hbsis.linha.categoria;

import br.com.hbsis.categoria.produto.CategoriaProdutoDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.websocket.server.PathParam;
import java.util.List;

@RestController
@RequestMapping("/linhacategorias")
public class LinhaCategoriaRest {

    private static final Logger LOGGER = LoggerFactory.getLogger(LinhaCategoriaRest.class);

    private final LinhaCategoriaService linhaCategoriaService;

    @Autowired
    public LinhaCategoriaRest(LinhaCategoriaService linhaCategoriaService) { this.linhaCategoriaService = linhaCategoriaService; }

    @PostMapping
    public LinhaCategoriaDTO save(@RequestBody LinhaCategoriaDTO linhaCategoriaDTO){

        LOGGER.info("Recebendo solicitação de persistência de linha categoria...");
        LOGGER.debug("Payload: {}", linhaCategoriaDTO);

        return this.linhaCategoriaService.save(linhaCategoriaDTO);
    }

    @GetMapping("/export-csv-linhas")
    public void exportCSV(HttpServletResponse response) {

        LOGGER.info("Exportando arquivo 'linhas.csv'");

        this.linhaCategoriaService.exportCSV(response);
    }

    @PostMapping("/import-csv-linhas")
    public void importCSV(@RequestParam("file") MultipartFile file) throws Exception {

        linhaCategoriaService.readAll(file);
    }

    @GetMapping("/{id}")
    public LinhaCategoriaDTO find(@PathVariable("id") Long id){

        LOGGER.info("Recebendo find by ID...[{}]", id);

        return this.linhaCategoriaService.findById(id);
    }

    @PutMapping("/{id}")
    public LinhaCategoriaDTO update(@PathVariable("id") Long id, @RequestBody LinhaCategoriaDTO linhaCategoriaDTO){
        LOGGER.info("Recebendo update para Linha Categoria de ID: {}", id);
        LOGGER.debug("Payload: {}", linhaCategoriaDTO);

        return this.linhaCategoriaService.update(linhaCategoriaDTO, id);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Long id){
        LOGGER.info("Recebendo Delete para Linha Categoria de ID: {}", id);

        this.linhaCategoriaService.delete(id);
    }

    @GetMapping("/listar")
    public List<LinhaCategoriaDTO> listar(){

        LOGGER.info("Gerando lista de Linhas.");

        return this.linhaCategoriaService.listar();
    }


}
