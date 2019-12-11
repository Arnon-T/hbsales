package br.com.hbsis.categoria.produto;

import br.com.hbsis.fornecedor.Fornecedor;
import br.com.hbsis.fornecedor.FornecedorDTO;
import br.com.hbsis.fornecedor.FornecedorService;
import com.google.common.net.HttpHeaders;
import com.opencsv.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CategoriaProdutoService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CategoriaProdutoService.class);

    private final ICategoriaProdutoRepository iCategoriaProdutoRepository;

    private final FornecedorService fornecedorService;

    public CategoriaProdutoService(ICategoriaProdutoRepository iCategoriaProdutoRepository, FornecedorService fornecedorService) {
        this.iCategoriaProdutoRepository = iCategoriaProdutoRepository;
        this.fornecedorService = fornecedorService;
    }

    public List<CategoriaProdutoDTO> readAll(MultipartFile file) throws Exception {

        InputStreamReader reader = new InputStreamReader(file.getInputStream());

        CSVReader csvReader = new CSVReaderBuilder(reader)
                .withSkipLines(1)
                .build();


        List<String[]> linhaString = csvReader.readAll();

        List<CategoriaProdutoDTO> resultadoLeitura = new ArrayList<>();

        for (String[] linha : linhaString) {
            try {

                String[] bean = linha[0].replaceAll("\"", "").split(";");

                CategoriaProdutoDTO categoriaProdutoDTO = new CategoriaProdutoDTO();

                Fornecedor fornecedor = this.fornecedorService.findByCnpj(bean[3].replaceAll("[-/.]", ""));

                categoriaProdutoDTO.setCodigoCategoriaProduto(bean[0].toUpperCase());
                categoriaProdutoDTO.setNomeCategoriaProduto(bean[1]);
                categoriaProdutoDTO.setFornecedorId(fornecedor.getId());

                if (!(iCategoriaProdutoRepository.existsCategoriaProdutoByCodigoCategoriaProduto(categoriaProdutoDTO.getCodigoCategoriaProduto())) ||
                        !(iCategoriaProdutoRepository.existsCategoriaProdutoByCodigoCategoriaProduto(construtorCodigo(categoriaProdutoDTO.getCodigoCategoriaProduto(), categoriaProdutoDTO.getFornecedorId())))) {
                    save(categoriaProdutoDTO);
                    resultadoLeitura.add(categoriaProdutoDTO);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return resultadoLeitura;
    }

    public void exportCSV(HttpServletResponse response) {
        try {
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

            String headerCSV[] = {"codigo_categoria", "nome_categoria", "razao_social_fornecedor", "cnpj_fornecedor"};
            csvWriter.writeNext(headerCSV);

            for (CategoriaProduto linha : iCategoriaProdutoRepository.findAll()) {
                String formatCNPJ = linha.getFornecedor().getCnpj().replaceAll("(\\d{2})(\\d{3})(\\d{3})(\\d{4})(\\d{2})", "$1.$2.$3/$4-$5");

                csvWriter.writeNext(new String[]{
                        linha.getCodigoCategoriaProduto(),
                        linha.getNomeCategoriaProduto(),
                        linha.getFornecedor().getRazaoSocial(),
                        formatCNPJ
                });
            }

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public CategoriaProduto save(CategoriaProdutoDTO categoriaProdutoDTO) {

        this.validate(categoriaProdutoDTO);

        LOGGER.info("Salvando Categoria Produto");
        LOGGER.debug("Categoria Produto: {}", categoriaProdutoDTO);

        CategoriaProduto categoriaProduto = new CategoriaProduto();

        categoriaProduto.setNomeCategoriaProduto(categoriaProdutoDTO.getNomeCategoriaProduto());
        categoriaProduto.setFornecedor(fornecedorService.fromDto(fornecedorService.findById(categoriaProdutoDTO.getFornecedorId()), new Fornecedor()));
        categoriaProduto.setCodigoCategoriaProduto(construtorCodigo(categoriaProdutoDTO.getCodigoCategoriaProduto(), categoriaProdutoDTO.getFornecedorId()).toUpperCase());

        categoriaProduto = this.iCategoriaProdutoRepository.save(categoriaProduto);

        return categoriaProduto;
    }

    public String construtorCodigo(String codigoInformado, Long idFornecedor) {
        String codigoGerado = null;
        FornecedorDTO fornecedorDto = fornecedorService.findById(idFornecedor);
        codigoInformado = codigoInformado.replaceAll("[^a-zA-Z0-9]+", "");
        if (codigoInformado.length() < 3) {
            codigoInformado = String.format("%1$3s", codigoInformado).replaceAll(" ", "0").toUpperCase();

        }
        codigoGerado = "CAT" + fornecedorDto.getCnpj().substring(10, 14) + codigoInformado;

        return codigoGerado;
    }

    private void validate(CategoriaProdutoDTO categoriaProdutoDTO) {

        LOGGER.info("Validando Categoria Produto");

        if (categoriaProdutoDTO == null) {
            throw new IllegalArgumentException("Categoria Produto não deve ser nulo");
        }
        if (StringUtils.isEmpty(categoriaProdutoDTO.getNomeCategoriaProduto())) {
            throw new IllegalArgumentException("Nome não deve ser nulo/vazio");
        }
        if (StringUtils.isEmpty(categoriaProdutoDTO.getCodigoCategoriaProduto())) {
            throw new IllegalArgumentException("Codigo não deve ser nulo/vazio");
        }
        if (categoriaProdutoDTO.getCodigoCategoriaProduto().length() > 3) {
            throw new IllegalArgumentException("Codigo informado não deve ser maior que 3 dígitos");
        }
        if (StringUtils.isEmpty(categoriaProdutoDTO.getFornecedorId())) {
            throw new IllegalArgumentException("ID Fornecedor não deve ser nulo/vazio");

        }
    }

    public CategoriaProdutoDTO findById(Long id) {
        Optional<CategoriaProduto> categoriaProdutoOptional = this.iCategoriaProdutoRepository.findById(id);

        if (categoriaProdutoOptional.isPresent()) {
            return CategoriaProdutoDTO.of(categoriaProdutoOptional.get());
        }

        throw new IllegalArgumentException(String.format("ID %s não existe", id));
    }

    public CategoriaProduto findByCodigoCategoriaProduto(String codigoCategoriaProduto){
        return this.iCategoriaProdutoRepository.findByCodigoCategoriaProduto(codigoCategoriaProduto);
    }

    public CategoriaProdutoDTO update(CategoriaProdutoDTO categoriaProdutoDTO, Long id) {

        Optional<CategoriaProduto> categoriaProdutoExistenteOptional = this.iCategoriaProdutoRepository.findById(id);

        if (categoriaProdutoExistenteOptional.isPresent()) {
            CategoriaProduto categoriaProdutoExistente = categoriaProdutoExistenteOptional.get();

            LOGGER.info("Atualizando o fornecedor... id:{}", categoriaProdutoExistente.getId());
            LOGGER.debug("Payload: {}", categoriaProdutoDTO);
            LOGGER.debug("Fornecedor Existente: {}", categoriaProdutoExistente);

            categoriaProdutoExistente.setNomeCategoriaProduto(categoriaProdutoDTO.getNomeCategoriaProduto());
            categoriaProdutoExistente.setFornecedor(fornecedorService.fromDto(fornecedorService.findById(categoriaProdutoDTO.getFornecedorId()), new Fornecedor()));
            categoriaProdutoExistente.setCodigoCategoriaProduto(construtorCodigo(categoriaProdutoDTO.getCodigoCategoriaProduto(), categoriaProdutoDTO.getFornecedorId()));

            categoriaProdutoExistente = this.iCategoriaProdutoRepository.save(categoriaProdutoExistente);

            return categoriaProdutoDTO.of(categoriaProdutoExistente);
        }

        throw new IllegalArgumentException(String.format("ID %s não existe", id));

    }

    public List<CategoriaProdutoDTO> listar() {

        List<CategoriaProdutoDTO> categoriaProdutoDTOList = new ArrayList<>();

        for (CategoriaProduto categoriaProduto : this.iCategoriaProdutoRepository.findAll()) {
            CategoriaProdutoDTO categoriaProdutoDTO = new CategoriaProdutoDTO();

            categoriaProdutoDTO.setId(categoriaProduto.getId());
            categoriaProdutoDTO.setNomeCategoriaProduto(categoriaProduto.getNomeCategoriaProduto());
            categoriaProdutoDTO.setCodigoCategoriaProduto(categoriaProduto.getCodigoCategoriaProduto());
            categoriaProdutoDTO.setFornecedorId(categoriaProduto.getFornecedor().getId());

            categoriaProdutoDTOList.add(categoriaProdutoDTO);
        }

        return categoriaProdutoDTOList;
    }


    public void delete(Long id) {
        LOGGER.info("Executando delete para categoria produto de ID> [{}]", id);

        this.iCategoriaProdutoRepository.deleteById(id);
    }

    public boolean existsCategoriaProdutoByFornecedorId(Long id){
        return this.iCategoriaProdutoRepository.existsCategoriaProdutoByFornecedorId(id);
    }

    public boolean existsCategoriaProdutoByCodigoCategoriaProduto(String codigoCategoriaProduto){
        return this.iCategoriaProdutoRepository.existsCategoriaProdutoByCodigoCategoriaProduto(codigoCategoriaProduto);
    }

}
