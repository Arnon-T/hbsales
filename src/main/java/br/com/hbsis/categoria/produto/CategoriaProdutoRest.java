package br.com.hbsis.categoria.produto;

import br.com.hbsis.usuario.UsuarioDTO;
import com.google.common.net.HttpHeaders;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.opencsv.CSVWriter;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping("/categoriaprodutos")
public class CategoriaProdutoRest{
    private static final Logger LOGGER = LoggerFactory.getLogger(CategoriaProdutoRest.class);

    private final CategoriaProdutoService categoriaProdutoService;

    @Autowired
    public CategoriaProdutoRest(CategoriaProdutoService categoriaProdutoService) {this.categoriaProdutoService = categoriaProdutoService;}

    @PostMapping
    public CategoriaProdutoDTO save(@RequestBody CategoriaProdutoDTO categoriaProdutoDTO){

        LOGGER.info("Recebendo solicitação de persistência de categoria produto...");
        LOGGER.debug("Payload: {}", categoriaProdutoDTO);

        return this.categoriaProdutoService.save(categoriaProdutoDTO);
    }

    @GetMapping("/export-csv-categorias")
    public void exportCSV(HttpServletResponse response) throws Exception {
        // nome do arquivo e tipo de conteúdo
        String nomearquivo = "categorias.csv";
        response.setContentType("text/csv");
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + nomearquivo + "\"");

        StatefulBeanToCsv<String> writer = new StatefulBeanToCsvBuilder<String>(response.getWriter())
                .withQuotechar(CSVWriter.NO_QUOTE_CHARACTER)
                .withSeparator(';')
                .withOrderedResults(false)
                .build();

        writer.write(categoriaProdutoService.listToPrint());
    }

    @GetMapping("/{id}")
    public CategoriaProdutoDTO find(@PathVariable("id") Long id) {

        LOGGER.info("Recebendo find by ID... id: [{}]", id);

        return this.categoriaProdutoService.findById(id);
    }

    @PutMapping("/{id}")
    public CategoriaProdutoDTO update(@PathVariable("id") Long id, @RequestBody CategoriaProdutoDTO categoriaProdutoDTO){
        LOGGER.info("Recebendo Update para Categoria Produto de ID: {}", id);
        LOGGER.debug("Payload: {}", categoriaProdutoDTO);

        return this.categoriaProdutoService.update(categoriaProdutoDTO, id);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Long id) {
        LOGGER.info("Recebendo Delete para Categoria Produtos de ID: {}", id);

        this.categoriaProdutoService.delete(id);
    }



}