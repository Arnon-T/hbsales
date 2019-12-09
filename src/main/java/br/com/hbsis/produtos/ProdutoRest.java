package br.com.hbsis.produtos;

import com.sun.org.apache.xpath.internal.operations.Mult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;


@RestController
@RequestMapping("/produtos")
public class ProdutoRest{

    private static final Logger LOGGER = LoggerFactory.getLogger(ProdutoRest.class);

    private final ProdutoService produtoService;

    @Autowired
    public ProdutoRest(ProdutoService produtoService) {
        this.produtoService = produtoService;
    }

    @PostMapping
    public ProdutoDTO save(@RequestBody ProdutoDTO produtoDTO){

        LOGGER.info("Recebendo solicitação de persistência de produto...");
        LOGGER.debug("Payload: {}", produtoDTO);

        return this.produtoService.save(produtoDTO);
    }

    @GetMapping("/export-csv-produtos")
    public void exportCSV(HttpServletResponse response) throws Exception {

        LOGGER.info("Exportando arquivo produtos.csv");

        this.produtoService.exportCSV(response);
    }

    @PostMapping("/import-csv-produtos")
    public void importCSV(@RequestParam MultipartFile file) throws Exception{

        produtoService.readAll(file);

    }

    @PutMapping("/import-produtos-fornecedor/{id}")
    public void importProdutoFornecedor(@PathVariable("id") Long id, @RequestParam MultipartFile file) throws Exception{

        LOGGER.info("Adicionando Produtos do Fornecedor de ID... [{}]", id);

        produtoService.importProdutoFornecedor(id, file);
    }

    @GetMapping("/{id}")
    public ProdutoDTO find(@PathVariable("id") Long id) {

        LOGGER.info("Recebendo find by ID... [{}]", id);

        return this.produtoService.findById(id);
    }

    @PutMapping("/{id}")
    public ProdutoDTO update(@PathVariable("id") Long id, @RequestBody ProdutoDTO produtoDTO){
        LOGGER.info("Recebendo update para produto de ID: {}", id);
        LOGGER.debug("Payload: {}", produtoDTO);

        return this.produtoService.update(produtoDTO, id);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Long id){
        LOGGER.info("Recebendo Delete para produto de ID: {}", id);

        this.produtoService.delete(id);
    }

    @GetMapping("/listar")
    public List<ProdutoDTO> listar(){
        return this.produtoService.listar();
    }

}
