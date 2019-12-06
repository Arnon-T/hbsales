package br.com.hbsis.categoria.produto;

import br.com.hbsis.fornecedor.Fornecedor;
import br.com.hbsis.fornecedor.FornecedorDTO;
import br.com.hbsis.fornecedor.FornecedorService;
import br.com.hbsis.fornecedor.IFornecedorRepository;
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

    public List<CategoriaProduto> readAll(MultipartFile file) throws Exception {

        InputStreamReader reader = new InputStreamReader(file.getInputStream());

        CSVReader csvReader = new CSVReaderBuilder(reader)
                .withSkipLines(1)
                .build();


        List<String[]> linhaString = csvReader.readAll();

        List<CategoriaProduto> resultadoLeitura = new ArrayList<>();

        for (String[] linha : linhaString) {
            try {

                String[] bean = linha[0].replaceAll("\"", "").split(";");

                CategoriaProduto categoriaProduto = new CategoriaProduto();

                categoriaProduto.setCodigoCategoriaProduto(bean[1]);
                categoriaProduto.setNomeCategoriaProduto(bean[2]);
                categoriaProduto.setFornecedor(fornecedorService.fromDto(fornecedorService.findById(Long.parseLong(bean[3])), new Fornecedor()));

                resultadoLeitura.add(categoriaProduto);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return iCategoriaProdutoRepository.saveAll(resultadoLeitura);
    }

    public void exportCSV(HttpServletResponse response)  {
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

            String headerCSV[] = {"id_categoria_produto", "codigo_categoria_produto", "nome_categoria_produto", "id_fornecedor"};
            csvWriter.writeNext(headerCSV);

            for (CategoriaProduto linha : iCategoriaProdutoRepository.findAll()) {
                csvWriter.writeNext(new String[]{linha.getId().toString(), linha.getCodigoCategoriaProduto().toString(), linha.getNomeCategoriaProduto(), linha.getFornecedor().getId().toString()});
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public CategoriaProdutoDTO save(CategoriaProdutoDTO categoriaProdutoDTO) {

        this.validate(categoriaProdutoDTO);

        LOGGER.info("Salvando Categoria Produto");
        LOGGER.debug("Categoria Produto: {}", categoriaProdutoDTO);

        CategoriaProduto categoriaProduto = new CategoriaProduto();

        categoriaProduto.setNomeCategoriaProduto(categoriaProdutoDTO.getNomeCategoriaProduto());
        categoriaProduto.setFornecedor(fornecedorService.fromDto(fornecedorService.findById(categoriaProdutoDTO.getFornecedorId()), new Fornecedor()));
        categoriaProduto.setCodigoCategoriaProduto(construtorCodigo(categoriaProdutoDTO.getCodigoCategoriaProduto(), categoriaProduto.getFornecedor().getId()));

        categoriaProduto = this.iCategoriaProdutoRepository.save(categoriaProduto);

        return CategoriaProdutoDTO.of(categoriaProduto);
    }


    public String construtorCodigo(String codigoInformado, Long idFornecedor){
        String codigoGerado = null;
        FornecedorDTO fornecedorDto = fornecedorService.findById(idFornecedor);
        if(codigoInformado.length() < 3){
            codigoInformado = String.format("%1$3s", codigoInformado);
            codigoInformado = codigoInformado.replaceAll(" ", "0");
        }
        codigoGerado = "CAT" +  fornecedorDto.getCnpj().substring(10,14) + codigoInformado;

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

    public CategoriaProdutoDTO update(CategoriaProdutoDTO categoriaProdutoDTO, Long id) {

        Optional<CategoriaProduto> categoriaProdutoExistenteOptional = this.iCategoriaProdutoRepository.findById(id);

        if (categoriaProdutoExistenteOptional.isPresent()) {
            CategoriaProduto categoriaProdutoExistente = categoriaProdutoExistenteOptional.get();

            LOGGER.info("Atualizando o fornecedor... id:{}", categoriaProdutoExistente.getId());
            LOGGER.debug("Payload: {}", categoriaProdutoDTO);
            LOGGER.debug("Fornecedor Existente: {}", categoriaProdutoExistente);

            categoriaProdutoExistente.setNomeCategoriaProduto(categoriaProdutoDTO.getNomeCategoriaProduto());
            categoriaProdutoExistente.setFornecedor(fornecedorService.fromDto(fornecedorService.findById(categoriaProdutoDTO.getFornecedorId()), new Fornecedor()));
            categoriaProdutoExistente.setCodigoCategoriaProduto(construtorCodigo(categoriaProdutoDTO.getCodigoCategoriaProduto(), categoriaProdutoExistente.getFornecedor().getId()));

            categoriaProdutoExistente = this.iCategoriaProdutoRepository.save(categoriaProdutoExistente);

            return categoriaProdutoDTO.of(categoriaProdutoExistente);
        }

        throw new IllegalArgumentException(String.format("ID %s não existe", id));

    }

    public void delete(Long id) {
        LOGGER.info("Executando delete para categoria produto de ID> [{}]", id);

        this.iCategoriaProdutoRepository.deleteById(id);
    }

}
