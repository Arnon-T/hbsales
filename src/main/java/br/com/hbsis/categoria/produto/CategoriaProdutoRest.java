package br.com.hbsis.categoria.produto;

import com.google.common.net.HttpHeaders;
import com.opencsv.CSVWriterBuilder;
import com.opencsv.ICSVWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.opencsv.CSVWriter;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;

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

        String nomearquivo = "categorias.csv";
        response.setContentType("text/csv");
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + nomearquivo + "\"");

       PrintWriter writer = response.getWriter();

       ICSVWriter csvWriter = new CSVWriterBuilder(writer)
                .withSeparator(';')
                .withEscapeChar(CSVWriter.DEFAULT_ESCAPE_CHARACTER)
                .withLineEnd(CSVWriter.DEFAULT_LINE_END)
                .build();

       String headerCSV[] = {"id_categoria_produto", "codigo_categoria_produto", "nome_categoria_produto", "id_fornecedor"};
        csvWriter.writeNext(headerCSV);

        for (CategoriaProduto linha : categoriaProdutoService.findAll()) {
            csvWriter.writeNext(new String[] {linha.getId().toString(), linha.getCodigoCategoriaProduto().toString(),  linha.getNomeCategoriaProduto(), linha.getFornecedor().getId().toString()});
        }

    }

    @PostMapping("/import-csv-categorias")
    public void importCSV(@RequestParam("file") MultipartFile file) throws Exception {
        Reader reader = Files.newBufferedReader(Paths.get("C:/hbsales/categorias.csv"));
        categoriaProdutoService.saveAll(categoriaProdutoService.readAll(reader));
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