package br.com.hbsis.produtos;

import br.com.hbsis.linha.categoria.ILinhaCategoriaRepository;
import br.com.hbsis.linha.categoria.LinhaCategoria;
import com.google.common.net.HttpHeaders;
import com.opencsv.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
public class ProdutoService {

    private final Logger LOGGER = LoggerFactory.getLogger(ProdutoService.class);

    private final IProdutoRepository iProdutoRepository;
    private final ILinhaCategoriaRepository iLinhaCategoriaRepository;

    public ProdutoService(IProdutoRepository iProdutoRepository, ILinhaCategoriaRepository iLinhaCategoriaRepository) {
        this.iProdutoRepository = iProdutoRepository;
        this.iLinhaCategoriaRepository = iLinhaCategoriaRepository;
    }

    public List<Produto> findaAll() {
        return this.iProdutoRepository.findAll();
    }

    public void exportCSV(HttpServletResponse response) throws Exception {
        try {
            String nomearquivo = "produtos.csv";
            response.setContentType("text/csv");
            response.setHeader(HttpHeaders.CONTENT_DISPOSITION,
                    "attachment; nomearquivo=\"" + nomearquivo + "\"");

            PrintWriter writer = response.getWriter();

            ICSVWriter csvWriter = new CSVWriterBuilder(writer)
                    .withSeparator(';')
                    .withEscapeChar(CSVWriter.DEFAULT_ESCAPE_CHARACTER)
                    .withLineEnd(CSVWriter.DEFAULT_LINE_END)
                    .build();

            String headerCSV[] = {"id", "codigo", "nome", "preco", "id_linha_categoria", "unidades_caixa", "peso_unidade", "dataValidade"};
            csvWriter.writeNext(headerCSV);

            for (Produto produto : iProdutoRepository.findAll()) {
                csvWriter.writeNext(new String[]{
                        produto.getId().toString(),
                        produto.getCodigoProduto().toString(),
                        produto.getNomeProduto(),
                        produto.getPrecoProduto().toString(),
                        produto.getLinhaCategoria().getIdLinhaCategoria().toString(),
                        produto.getUnidadesCaixa().toString(),
                        produto.getPesoUnidade().toString(),
                        produto.getDataValidade()
                });
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Produto> readAll(MultipartFile file) throws Exception {
        InputStreamReader inputStreamReader = new InputStreamReader(file.getInputStream());

        CSVReader csvReader = new CSVReaderBuilder(inputStreamReader)
                .withSkipLines(1)
                .build();

        List<String[]> linhaString = csvReader.readAll();

        List<Produto> resultadoLeitura = new ArrayList<>();

        for (String[] linha : linhaString) {
            try{
                String[] bean = linha[0].replaceAll("\"", "").split(";");

                Produto produto = new Produto();

                //produto.setId(Long.parseLong(bean[0]));
                produto.setCodigoProduto(Long.parseLong(bean[1]));
                produto.setNomeProduto(bean[2]);
                produto.setPrecoProduto(Double.parseDouble(bean[3]));
                produto.setLinhaCategoria(iLinhaCategoriaRepository.findById(Long.parseLong(bean[4])).get());
                produto.setUnidadesCaixa(Double.parseDouble(bean[5]));
                produto.setPesoUnidade(Double.parseDouble(bean[6]));
                produto.setDataValidade(bean[7]);

                resultadoLeitura.add(produto);
            }  catch (Exception e){
                e.printStackTrace();
            }
        }
        return iProdutoRepository.saveAll(resultadoLeitura);
    }

    public ProdutoDTO save (ProdutoDTO produtoDTO){

        LOGGER.debug("Produto: {}", produtoDTO);

        this.validate(produtoDTO);

        LOGGER.info("Salvando Produto");
        LOGGER.debug("Produto: {}", produtoDTO);

        Produto produto = new Produto();

        produto.setId(produtoDTO.getId());
        produto.setCodigoProduto(produtoDTO.getCodigoProduto());
        produto.setNomeProduto(produtoDTO.getNomeProduto());
        produto.setPrecoProduto(produtoDTO.getPrecoProduto());
        produto.setLinhaCategoria(iLinhaCategoriaRepository.findById(produtoDTO.getLinhaCategoriaId()).get());
        produto.setUnidadesCaixa(produtoDTO.getUnidadesCaixa());
        produto.setPesoUnidade(produtoDTO.getPesoUnidade());
        produto.setDataValidade(produtoDTO.getDataValidade());

        produto = this.iProdutoRepository.save(produto);

        return ProdutoDTO.of(produto);
    }

    public void validate(ProdutoDTO produtoDTO){

        LOGGER.info("Validando Produto");

        if(produtoDTO == null){
            throw new IllegalArgumentException("Produto não pode ser nulo.");
        }
    }

    public ProdutoDTO findById(Long id){
        Optional<Produto> produtoOptional = this.iProdutoRepository.findById(id);

        if(produtoOptional.isPresent()){
            return ProdutoDTO.of(produtoOptional.get());
        }
        throw new IllegalArgumentException(String.format("ID %s não existe.", id));
    }

    public ProdutoDTO update(ProdutoDTO produtoDTO, Long id){

        Optional<Produto> produtoOptional = this.iProdutoRepository.findById(id);

        if(produtoOptional.isPresent()){
            Produto produtoExistente = produtoOptional.get();

            LOGGER.info("Atualizando o produto...id: {}", produtoExistente.getId());
            LOGGER.debug("Payload: {}", produtoDTO);
            LOGGER.debug("Produto existente: {}", produtoExistente);

            produtoExistente.setId(produtoDTO.getId());
            produtoExistente.setCodigoProduto(produtoDTO.getCodigoProduto());
            produtoExistente.setNomeProduto(produtoDTO.getNomeProduto());
            produtoExistente.setPrecoProduto(produtoDTO.getPrecoProduto());
            produtoExistente.setLinhaCategoria(iLinhaCategoriaRepository.findById(produtoDTO.getLinhaCategoriaId()).get());
            produtoExistente.setUnidadesCaixa(produtoDTO.getUnidadesCaixa());
            produtoExistente.setPesoUnidade(produtoDTO.getPesoUnidade());
            produtoExistente.setDataValidade(produtoDTO.getDataValidade());

            produtoExistente = this.iProdutoRepository.save(produtoExistente);

            return ProdutoDTO.of(produtoExistente);

        }

        throw new IllegalArgumentException(String.format("ID %s não existe", id));

    }

    public void delete (Long id){
        LOGGER.info("Executando delete para produto de ID [{}]", id);

        this.iProdutoRepository.deleteById(id);
    }


}
