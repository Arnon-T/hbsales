package br.com.hbsis.produtos;

import br.com.hbsis.fornecedor.FornecedorService;
import br.com.hbsis.fornecedor.IFornecedorRepository;
import br.com.hbsis.linha.categoria.ILinhaCategoriaRepository;
import br.com.hbsis.linha.categoria.LinhaCategoriaService;
import br.com.hbsis.util.UnidadeMedida;
import com.google.common.net.HttpHeaders;
import com.opencsv.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import sun.text.bidi.BidiBase;

import javax.servlet.http.HttpServletResponse;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
public class ProdutoService {

    private final Logger LOGGER = LoggerFactory.getLogger(ProdutoService.class);

    private final IProdutoRepository iProdutoRepository;
    private final FornecedorService fornecedorService;
    private final LinhaCategoriaService linhaCategoriaService;

    private UnidadeMedida unidadeMedida;
    private BigDecimal precoFormat;
    final DateTimeFormatter localDateFormatt = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public ProdutoService(IProdutoRepository iProdutoRepository, FornecedorService fornecedorService, LinhaCategoriaService linhaCategoriaService) {
        this.iProdutoRepository = iProdutoRepository;
        this.fornecedorService = fornecedorService;
        this.linhaCategoriaService = linhaCategoriaService;
    }

    public void exportCSV(HttpServletResponse response) {
        try {
            String nomearquivo = "produtos.csv";
            response.setContentType("text/csv");
            response.setHeader(HttpHeaders.CONTENT_DISPOSITION,
                    "attachment; filename=\"" + nomearquivo + "\"");

            PrintWriter writer = response.getWriter();

            ICSVWriter csvWriter = new CSVWriterBuilder(writer)
                    .withSeparator(';')
                    .withEscapeChar(CSVWriter.DEFAULT_ESCAPE_CHARACTER)
                    .withLineEnd(CSVWriter.DEFAULT_LINE_END)
                    .build();

            String headerCSV[] = {"codigo", "nome", "preco", "quantidade_por_caixa", "peso_por_unidade",
                    "data_validade", "codigo_linha_categoria", "linha_categoria", "codigo_categoria", "categoria", "cnpj_fornecedor", "razao_social_fornecedor"};
            csvWriter.writeNext(headerCSV);

            for (Produto produto : iProdutoRepository.findAll()) {
                precoFormat = new BigDecimal(produto.getPrecoProduto()).setScale(2, BigDecimal.ROUND_HALF_UP);
                String dataFormatada = produto.getDataValidade().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
                String precoFinal = "R$" + precoFormat;
                String formatCnpj = produto
                        .getLinhaCategoria()
                        .getCategoriaProduto()
                        .getFornecedor().getCnpj()
                        .replaceAll("(\\d{2})(\\d{3})(\\d{3})(\\d{4})(\\d{2})", "$1.$2.$3/$4-$5");

                csvWriter.writeNext(new String[]{
                        produto.getCodigoProduto(),
                        produto.getNomeProduto(),
                        precoFinal.replaceAll("\\.", ","),
                        Integer.toString(produto.getUnidadesCaixa()),
                        produto.getPesoUnidade().toString().replaceAll("\\.", ",") + produto.getUnidadeMedida(),
                        dataFormatada,
                        produto.getLinhaCategoria().getCodigoLinhaCategoria(),
                        produto.getLinhaCategoria().getNomeLinhaCategoria(),
                        produto.getLinhaCategoria().getCategoriaProduto().getCodigoCategoriaProduto(),
                        produto.getLinhaCategoria().getCategoriaProduto().getNomeCategoriaProduto(),
                        formatCnpj,
                        produto.getLinhaCategoria().getCategoriaProduto().getFornecedor().getRazaoSocial()

                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Produto> readAll(MultipartFile file) throws Exception {

        InputStreamReader inputStreamReader = new InputStreamReader(file.getInputStream());
        CSVParser parser = new CSVParserBuilder().withSeparator(';').build();
        CSVReader csvReader = new CSVReaderBuilder(inputStreamReader).withCSVParser(parser)
                .withSkipLines(1)
                .build();

        List<String[]> linhaString = csvReader.readAll();

        List<Produto> resultadoLeitura = new ArrayList<>();

        for (String[] bean : linhaString) {
            try {

                Produto produto = new Produto();

                LocalDate dataValidade = LocalDate.parse(bean[5], localDateFormatt);
                String precoFormatt = bean[2].replaceAll("R\\$", "");
                precoFormatt = precoFormatt.replaceAll(",", ".");

                String pesoFormatt = bean[4].replaceAll(",", ".");
                pesoFormatt = pesoFormatt.replaceAll("[^0-9 .]+", "");

                String unidadeMedidaFormatt = bean[4].replaceAll("[^a-zA-Z]+", "");

                produto.setCodigoProduto(bean[0]);
                produto.setNomeProduto(bean[1]);
                produto.setPrecoProduto(Double.parseDouble(precoFormatt));
                produto.setUnidadesCaixa(Integer.parseInt(bean[3]));
                produto.setPesoUnidade(Double.parseDouble(pesoFormatt));
                produto.setUnidadeMedida(UnidadeMedida.valueOf(unidadeMedidaFormatt));
                produto.setDataValidade(dataValidade);

                if(linhaCategoriaService.existsByCodigoLinhaCategoria(bean[6]) && !(this.existsByCodigoProduto(bean[0]))){
                    produto.setLinhaCategoria(linhaCategoriaService.findByCodigoLinhaCategoria(bean[6]));
                    resultadoLeitura.add(produto);
                    this.iProdutoRepository.save(produto);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return resultadoLeitura;
    }

    public void importProdutoFornecedor(Long id, MultipartFile file) throws Exception {
        InputStreamReader inputStreamReader = new InputStreamReader(file.getInputStream());

        CSVParser parser = new CSVParserBuilder().withSeparator(';').build();
        CSVReader csvReader = new CSVReaderBuilder(inputStreamReader).withCSVParser(parser)
                .withSkipLines(1)
                .build();

        List<String[]> linhaString = csvReader.readAll();

        List<Produto> resultadoLeitura = new ArrayList<>();

        for (String[] bean : linhaString) {
            try {

                Produto produto = new Produto();
                if (fornecedorService.existsById(id)) {
                    produto.setCodigoProduto(bean[1]);
                    produto.setNomeProduto(bean[2]);
                    produto.setPrecoProduto(Double.parseDouble(bean[3]));
                    produto.setLinhaCategoria(linhaCategoriaService.findByIdObject(Long.parseLong(bean[4])));
                    produto.setUnidadesCaixa(Integer.parseInt(bean[5]));
                    produto.setPesoUnidade(Double.parseDouble(bean[6]));
                    produto.setDataValidade(LocalDate.parse(bean[7]));

                    if (iProdutoRepository.existsByCodigoProduto(produto.getCodigoProduto()) &&
                            id == produto.getLinhaCategoria().getCategoriaProduto().getFornecedor().getId()) {

                        produto.setId(iProdutoRepository.findByCodigoProduto(produto.getCodigoProduto()).get().getId());
                        update(ProdutoDTO.of(produto), produto.getId());

                    } else if (id == produto.getLinhaCategoria().getCategoriaProduto().getFornecedor().getId()) {
                        iProdutoRepository.save(produto);
                    } else {

                        LOGGER.info("Produto {} ... pertence a outro fornecedor.", produto.getCodigoProduto());

                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public ProdutoDTO save(ProdutoDTO produtoDTO) {

        LOGGER.debug("Produto: {}", produtoDTO);

        this.validate(produtoDTO);

        LOGGER.info("Salvando Produto");
        LOGGER.debug("Produto: {}", produtoDTO);

        Produto produto = new Produto();

        String codigo = String.format("%1$10s", produtoDTO.getCodigoProduto().replaceAll("[^a-zA-Z0-9]+", ""));
        codigo = codigo.replaceAll(" ", "0").toUpperCase();

        produto.setId(produtoDTO.getId());
        produto.setCodigoProduto(codigo);
        produto.setNomeProduto(produtoDTO.getNomeProduto());
        produto.setPrecoProduto(produtoDTO.getPrecoProduto());
        produto.setLinhaCategoria(linhaCategoriaService.findByIdObject(produtoDTO.getLinhaCategoriaId()));
        produto.setUnidadesCaixa(produtoDTO.getUnidadesCaixa());
        produto.setPesoUnidade(produtoDTO.getPesoUnidade());
        produto.setDataValidade(produtoDTO.getDataValidade());
        produto.setUnidadeMedida(produtoDTO.getUnidadeMedida());

        produto = this.iProdutoRepository.save(produto);

        return ProdutoDTO.of(produto);
    }

    public void validate(ProdutoDTO produtoDTO) {

        LOGGER.info("Validando Produto");

        if (produtoDTO == null) {
            throw new IllegalArgumentException("Produto não pode ser nulo.");
        }
    }

    public ProdutoDTO findById(Long id) {
        Optional<Produto> produtoOptional = this.iProdutoRepository.findById(id);

        if (produtoOptional.isPresent()) {
            return ProdutoDTO.of(produtoOptional.get());
        }
        throw new IllegalArgumentException(String.format("ID %d não existe.", id));
    }

    public Produto findByIdObjeto(Long id){
        Optional<Produto> produtoOptional = this.iProdutoRepository.findById(id);

        if(produtoOptional.isPresent()){
            return produtoOptional.get();
        }
        throw new IllegalArgumentException(String.format("ID %d não existe.", id));
    }


    public ProdutoDTO update(ProdutoDTO produtoDTO, Long id) {

        Optional<Produto> produtoOptional = this.iProdutoRepository.findById(id);

        if (produtoOptional.isPresent()) {
            Produto produtoExistente = produtoOptional.get();

            LOGGER.info("Atualizando o produto...id: {}", produtoExistente.getId());
            LOGGER.debug("Payload: {}", produtoDTO);
            LOGGER.debug("Produto existente: {}", produtoExistente);

            String codigo = String.format("%01$10s", produtoDTO.getCodigoProduto().replaceAll("[^a-zA-Z0-9]+", ""));
            codigo = codigo.replaceAll(" ", "0");

            produtoExistente.setId(produtoDTO.getId());
            produtoExistente.setCodigoProduto(codigo);
            produtoExistente.setNomeProduto(produtoDTO.getNomeProduto());
            produtoExistente.setPrecoProduto(produtoDTO.getPrecoProduto());
            produtoExistente.setLinhaCategoria( linhaCategoriaService.findByIdObject(produtoDTO.getLinhaCategoriaId()));
            produtoExistente.setUnidadesCaixa(produtoDTO.getUnidadesCaixa());
            produtoExistente.setPesoUnidade(produtoDTO.getPesoUnidade());
            produtoExistente.setDataValidade(produtoDTO.getDataValidade());

            produtoExistente = this.iProdutoRepository.save(produtoExistente);

            return ProdutoDTO.of(produtoExistente);

        }

        throw new IllegalArgumentException(String.format("ID %s não existe", id));

    }

    public List<ProdutoDTO> listar() {

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


    public void delete(Long id) {
        LOGGER.info("Executando delete para produto de ID [{}]", id);

        this.iProdutoRepository.deleteById(id);
    }

    public boolean existsByCodigoProduto(String codigoProduto){
        Optional<Produto> produtoOptional = this.iProdutoRepository.findByCodigoProduto(codigoProduto);

        return produtoOptional.isPresent();
    }

}
