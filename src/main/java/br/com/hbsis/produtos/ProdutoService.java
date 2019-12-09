package br.com.hbsis.produtos;

import br.com.hbsis.fornecedor.FornecedorService;
import br.com.hbsis.fornecedor.IFornecedorRepository;
import br.com.hbsis.linha.categoria.ILinhaCategoriaRepository;
import com.google.common.net.HttpHeaders;
import com.opencsv.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
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
    private final IFornecedorRepository iFornecedorRepository;

    public ProdutoService(IProdutoRepository iProdutoRepository, ILinhaCategoriaRepository iLinhaCategoriaRepository, IFornecedorRepository iFornecedorRepository) {
        this.iProdutoRepository = iProdutoRepository;
        this.iLinhaCategoriaRepository = iLinhaCategoriaRepository;
        this.iFornecedorRepository = iFornecedorRepository;
    }

    public void exportCSV(HttpServletResponse response) {
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
                        produto.getCodigoProduto(),
                        produto.getNomeProduto(),
                        produto.getPrecoProduto().toString(),
                        produto.getLinhaCategoria().getIdLinhaCategoria().toString(),
                        Integer.toString(produto.getUnidadesCaixa()),
                        produto.getPesoUnidade().toString(),
                        produto.getDataValidade().toString(),
                });
            }
        } catch (Exception e) {
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

                produto.setId(Long.parseLong(bean[0]));
                produto.setCodigoProduto(bean[1]);
                produto.setNomeProduto(bean[2]);
                produto.setPrecoProduto(Double.parseDouble(bean[3]));
                produto.setLinhaCategoria(iLinhaCategoriaRepository.findById(Long.parseLong(bean[4])).get());
                produto.setUnidadesCaixa(Integer.parseInt(bean[5]));
                produto.setPesoUnidade(Double.parseDouble(bean[6]));
                produto.setDataValidade(LocalDate.parse(bean[7]));

                resultadoLeitura.add(produto);
            }  catch (Exception e){
                e.printStackTrace();
            }
        }
        return iProdutoRepository.saveAll(resultadoLeitura);
    }

    public void importProdutoFornecedor(Long id, MultipartFile file) throws Exception{
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
                if(iFornecedorRepository.existsById(id)){
                    produto.setCodigoProduto(bean[1]);
                    produto.setNomeProduto(bean[2]);
                    produto.setPrecoProduto(Double.parseDouble(bean[3]));
                    produto.setLinhaCategoria(iLinhaCategoriaRepository.findById(Long.parseLong(bean[4])).get());
                    produto.setUnidadesCaixa(Integer.parseInt(bean[5]));
                    produto.setPesoUnidade(Double.parseDouble(bean[6]));
                    produto.setDataValidade(LocalDate.parse(bean[7]));

                    if (iProdutoRepository.existsByCodigoProduto(produto.getCodigoProduto()) &&
                            id == produto.getLinhaCategoria().getCategoriaProduto().getFornecedor().getId()){

                        produto.setId(iProdutoRepository.findByCodigoProduto(produto.getCodigoProduto()).get().getId());
                        update(ProdutoDTO.of(produto), produto.getId());

                    }
                    else if(id == produto.getLinhaCategoria().getCategoriaProduto().getFornecedor().getId()) {
                        iProdutoRepository.save(produto);
                    }
                    else {

                        LOGGER.info("Produto {} ... pertence a outro fornecedor.", produto.getCodigoProduto());

                    }
                }
            }  catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public ProdutoDTO save (ProdutoDTO produtoDTO){

        LOGGER.debug("Produto: {}", produtoDTO);

        this.validate(produtoDTO);

        LOGGER.info("Salvando Produto");
        LOGGER.debug("Produto: {}", produtoDTO);

        Produto produto = new Produto();

        String codigo = String.format("%1$10s", produtoDTO.getCodigoProduto()).replaceAll(" ", "0").toUpperCase();

        produto.setId(produtoDTO.getId());
        produto.setCodigoProduto(codigo);
        produto.setNomeProduto(produtoDTO.getNomeProduto());
        produto.setPrecoProduto(produtoDTO.getPrecoProduto());
        produto.setLinhaCategoria(iLinhaCategoriaRepository.findById(produtoDTO.getLinhaCategoriaId()).get());
        produto.setUnidadesCaixa(produtoDTO.getUnidadesCaixa());
        produto.setPesoUnidade(produtoDTO.getPesoUnidade());
        produto.setDataValidade(produtoDTO.getDataValidade());
        produto.setUnidadeMedida(produtoDTO.getUnidadeMedida());

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

            String codigo = String.format("%01$10s", produtoDTO.getCodigoProduto());
            codigo = codigo.replaceAll(" ", "0");

            produtoExistente.setId(produtoDTO.getId());
            produtoExistente.setCodigoProduto(codigo);
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

    public List<ProdutoDTO> listar(){

        List<ProdutoDTO> produtoDTOList = new ArrayList<>();

        for (Produto produto : this.iProdutoRepository.findAll()) {
            ProdutoDTO produtoDTO = new ProdutoDTO();

            produtoDTO.setId(produto.getId());
            produtoDTO.setCodigoProduto(produto.getCodigoProduto());
            produtoDTO.setNomeProduto(produto.getNomeProduto());
            produtoDTO.setPrecoProduto(produto.getPrecoProduto());
            produtoDTO.setLinhaCategoriaId(produto.getLinhaCategoria().getIdLinhaCategoria());
            produtoDTO.setUnidadesCaixa(produto.getUnidadesCaixa());
            produtoDTO.setPesoUnidade(produto.getPesoUnidade());
            produtoDTO.setDataValidade(produto.getDataValidade());
            produtoDTO.setUnidadeMedida(produto.getUnidadeMedida());


            produtoDTOList.add(produtoDTO);
        }

        return produtoDTOList;
    }


    public void delete (Long id){
        LOGGER.info("Executando delete para produto de ID [{}]", id);

        this.iProdutoRepository.deleteById(id);
    }


}
