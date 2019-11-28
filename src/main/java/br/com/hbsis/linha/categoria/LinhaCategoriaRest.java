package br.com.hbsis.linha.categoria;

import com.google.common.net.HttpHeaders;
import com.opencsv.CSVWriter;
import com.opencsv.CSVWriterBuilder;
import com.opencsv.ICSVWriter;
import org.apache.http.client.methods.HttpHead;
import org.hibernate.boot.spi.InFlightMetadataCollector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.websocket.server.PathParam;
import java.io.PrintWriter;

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
    public void exportCSV(HttpServletResponse response) throws Exception {

        String nomearquivo = "linhas.csv";
        response.setContentType("text/csv");
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; nomearquivo=\"" + nomearquivo + "\"");

        PrintWriter writer = response.getWriter();

        ICSVWriter csvWriter = new CSVWriterBuilder(writer)
                .withSeparator(';')
                .withEscapeChar(CSVWriter.DEFAULT_ESCAPE_CHARACTER)
                .withLineEnd(CSVWriter.DEFAULT_LINE_END)
                .build();

        String headerCSV[] = {"id_linha_categoria", "id_categoria_produto", "nome_linha_categoria"};
        csvWriter.writeNext(headerCSV);

        for (LinhaCategoria linha : linhaCategoriaService.findAll()) {
            csvWriter.writeNext(new String[] {linha.getIdLinhaCategoria().toString(), linha.getCategoriaProduto().getId().toString(), linha.getNomeLinhaCategoria()});
        }

    }

    @PostMapping("/import-csv-linhas")
    public void importCSV(@RequestParam MultipartFile file) throws Exception {
        linhaCategoriaService.readAll(file);
    }

    @GetMapping("/{id}")
    public LinhaCategoriaDTO find(@PathParam("id") Long id){

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


}
