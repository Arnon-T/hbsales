package br.com.hbsis.produtos;

import br.com.hbsis.categoria.produto.CategoriaProduto;
import br.com.hbsis.categoria.produto.CategoriaProdutoDTO;
import br.com.hbsis.categoria.produto.CategoriaProdutoService;
import br.com.hbsis.fornecedor.Fornecedor;
import br.com.hbsis.fornecedor.FornecedorService;
import br.com.hbsis.linha.categoria.LinhaCategoria;
import br.com.hbsis.linha.categoria.LinhaCategoriaDTO;
import br.com.hbsis.linha.categoria.LinhaCategoriaService;
import br.com.hbsis.util.UnidadeMedida;
import com.google.common.net.HttpHeaders;
import com.opencsv.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.regex.Pattern;


@Service
public class ProdutoService {

    private final Logger LOGGER = LoggerFactory.getLogger(ProdutoService.class);

    private final IProdutoRepository iProdutoRepository;
    private final FornecedorService fornecedorService;
    private final LinhaCategoriaService linhaCategoriaService;
    private final CategoriaProdutoService categoriaProdutoService;

    private BigDecimal precoFormat;
    final DateTimeFormatter localDateFormatt = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public ProdutoService(IProdutoRepository iProdutoRepository, FornecedorService fornecedorService, LinhaCategoriaService linhaCategoriaService, CategoriaProdutoService categoriaProdutoService) {
        this.iProdutoRepository = iProdutoRepository;
        this.fornecedorService = fornecedorService;
        this.linhaCategoriaService = linhaCategoriaService;
        this.categoriaProdutoService = categoriaProdutoService;
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
        CSVReader csvReader = new CSVReaderBuilder(inputStreamReader)
                .withCSVParser(parser)
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

                produto.setLinhaCategoria(linhaCategoriaService.findByCodigoLinhaCategoria(bean[6]));
                resultadoLeitura.add(produto);
                this.iProdutoRepository.save(produto);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return resultadoLeitura;
    }

    public void importProdutoFornecedor(Long id, MultipartFile file) throws Exception {
        InputStreamReader inputStreamReader = new InputStreamReader(file.getInputStream());

        CSVParser parser = new CSVParserBuilder().withSeparator(';').build();
        CSVReader csvReader = new CSVReaderBuilder(inputStreamReader)
                .withCSVParser(parser)
                .withSkipLines(1)
                .build();

        List<String[]> linhaString = csvReader.readAll();

        for (String[] bean : linhaString) {


            ProdutoDTO produtoDTO = new ProdutoDTO();
            CategoriaProduto categoriaProduto = new CategoriaProduto();
            LinhaCategoria linhaCategoria = new LinhaCategoria();
            LinhaCategoriaDTO linhaCategoriaDTO = new LinhaCategoriaDTO();
            LocalDate dataValidade = LocalDate.parse(bean[6], localDateFormatt);

            String precoFormatt = bean[2].replaceAll("R\\$", "");
            precoFormatt = precoFormatt.replaceAll(",", ".");

            String codigo = String.format("%1$10s", bean[7].replaceAll("[^a-zA-Z0-9]+", ""));
            codigo = codigo.replaceAll(" ", "0").toUpperCase();

            produtoDTO.setCodigoProduto(bean[0]);
            produtoDTO.setNomeProduto(bean[1]);
            produtoDTO.setPrecoProduto(Double.parseDouble(precoFormatt));
            produtoDTO.setUnidadesCaixa(Integer.parseInt(bean[3]));
            produtoDTO.setPesoUnidade(Double.parseDouble(bean[4].replaceAll(",", ".")));
            produtoDTO.setUnidadeMedida(UnidadeMedida.valueOf(bean[5]));
            produtoDTO.setDataValidade(dataValidade);

            if (fornecedorService.existsById(id)) {

                try {
                    if (!(categoriaProdutoService.existsCategoriaProdutoByCodigoCategoriaProduto(bean[9]))
                            && !(categoriaProdutoService.existsCategoriaProdutoByCodigoCategoriaProduto(categoriaProdutoService.construtorCodigo(bean[9], id)))) {
                        Fornecedor fornecedor = FornecedorService.fromDto(fornecedorService.findById(id), new Fornecedor());
                        categoriaProduto.setFornecedor(fornecedor);
                        categoriaProduto.setNomeCategoriaProduto(bean[10]);
                        categoriaProduto.setCodigoCategoriaProduto(bean[9]);
                        categoriaProduto.setId(categoriaProdutoService.save(CategoriaProdutoDTO.of(categoriaProduto)).getId());
                        linhaCategoria.setCategoriaProduto(categoriaProduto);
                        LOGGER.info(String.format("Categoria Produto criada. ID %d", categoriaProduto.getId()));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    if (!(linhaCategoriaService.existsByCodigoLinhaCategoria(bean[7])) && !(linhaCategoriaService.existsByCodigoLinhaCategoria(codigo))) {
                        categoriaProduto = categoriaProdutoService.findByCodigoCategoriaProduto(bean[9]);
                        if (categoriaProduto == null) {
                            categoriaProduto = categoriaProdutoService.findByCodigoCategoriaProduto(categoriaProdutoService.construtorCodigo(bean[9], id));
                        }
                        linhaCategoria.setCategoriaProduto(categoriaProduto);
                        linhaCategoria.setNomeLinhaCategoria(bean[8]);
                        linhaCategoria.setCodigoLinhaCategoria(codigo);
                        linhaCategoriaDTO = linhaCategoriaService.save(LinhaCategoriaDTO.of(linhaCategoria));
                        linhaCategoria.setIdLinhaCategoria(linhaCategoriaDTO.getIdLinhaCategoria());
                        linhaCategoria.setCodigoLinhaCategoria(linhaCategoriaDTO.getCodigoLinhaCategoria());
                        produtoDTO.setLinhaCategoriaId(linhaCategoria.getIdLinhaCategoria());
                        LOGGER.info(String.format("Linha Categoria criada. ID %d", linhaCategoria.getIdLinhaCategoria()));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    if (linhaCategoriaService.existsByCodigoLinhaCategoria(bean[7]) || linhaCategoriaService.existsByCodigoLinhaCategoria(codigo)) {
                        categoriaProduto = categoriaProdutoService.findByCodigoCategoriaProduto(bean[9]);
                        if (categoriaProduto == null) {
                            categoriaProduto = categoriaProdutoService.findByCodigoCategoriaProduto(categoriaProdutoService.construtorCodigo(bean[9], id));
                        }

                        linhaCategoria.setCategoriaProduto(categoriaProduto);
                        linhaCategoria.setNomeLinhaCategoria(bean[8]);
                        linhaCategoria.setCodigoLinhaCategoria(codigo);
                        linhaCategoria.setIdLinhaCategoria(linhaCategoriaService.findByCodigoLinhaCategoria(codigo).getIdLinhaCategoria());

                        produtoDTO.setLinhaCategoriaId(linhaCategoria.getIdLinhaCategoria());

                        linhaCategoriaDTO = linhaCategoriaService.update(LinhaCategoriaDTO.of(linhaCategoria), linhaCategoria.getIdLinhaCategoria());
                        linhaCategoria.setIdLinhaCategoria(linhaCategoriaDTO.getIdLinhaCategoria());
                        linhaCategoria.setCodigoLinhaCategoria(linhaCategoriaDTO.getCodigoLinhaCategoria());
                        LOGGER.info(String.format("Linha Categoria atualizada. ID %d", linhaCategoria.getIdLinhaCategoria()));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    if (existsByCodigoProduto(bean[0])) {
                        produtoDTO.setLinhaCategoriaId(linhaCategoria.getIdLinhaCategoria());
                        produtoDTO.setId(findByCodigoProduto(bean[0]).getId());
                        update(produtoDTO, produtoDTO.getId());
                        LOGGER.info(String.format("Produto atualizado. ID %d", produtoDTO.getId()));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    if (!(existsByCodigoProduto(bean[0]))) {
                        produtoDTO.setLinhaCategoriaId(linhaCategoriaService.findByCodigoLinhaCategoria(linhaCategoria.getCodigoLinhaCategoria()).getIdLinhaCategoria());
                        produtoDTO = save(produtoDTO);
                        LOGGER.info(String.format("Produto criado. ID %d", produtoDTO.getId()));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }
    }

    public Produto findByCodigoProduto(String codigoProduto) {
        Optional<Produto> produtoOptional = this.iProdutoRepository.findByCodigoProduto(codigoProduto);

        if (produtoOptional.isPresent()) {
            return produtoOptional.get();
        }
        throw new IllegalArgumentException(String.format("Codigo %s não existe.", codigoProduto));
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
            produtoExistente.setLinhaCategoria(linhaCategoriaService.findByIdObject(produtoDTO.getLinhaCategoriaId()));
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

    public boolean existsByCodigoProduto(String codigoProduto) {
        Optional<Produto> produtoOptional = this.iProdutoRepository.findByCodigoProduto(codigoProduto);

        return produtoOptional.isPresent();
    }

}
