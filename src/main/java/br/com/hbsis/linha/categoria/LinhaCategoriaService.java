package br.com.hbsis.linha.categoria;

import br.com.hbsis.categoria.produto.CategoriaProduto;
import br.com.hbsis.categoria.produto.CategoriaProdutoDTO;
import br.com.hbsis.categoria.produto.CategoriaProdutoService;
import br.com.hbsis.categoria.produto.ICategoriaProdutoRepository;
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
public class LinhaCategoriaService {

    private static final Logger LOGGER = LoggerFactory.getLogger(LinhaCategoriaService.class);

    private final ILinhaCategoriaRepository iLinhaCategoriaRepository;
    private final ICategoriaProdutoRepository iCategoriaProdutoRepository;
    private final CategoriaProdutoService categoriaProdutoService;

    public LinhaCategoriaService(ILinhaCategoriaRepository iLinhaCategoriaRepository, ICategoriaProdutoRepository iCategoriaProdutoRepository, CategoriaProdutoService categoriaProdutoService) {
        this.iLinhaCategoriaRepository = iLinhaCategoriaRepository;
        this.iCategoriaProdutoRepository = iCategoriaProdutoRepository;
        this.categoriaProdutoService = categoriaProdutoService;
    }

    public List<LinhaCategoriaDTO> listar() {

        List<LinhaCategoriaDTO> linhaCategoriaDTOList = new ArrayList<>();

        for (LinhaCategoria linhaCategoria : this.iLinhaCategoriaRepository.findAll()) {
            LinhaCategoriaDTO linhaCategoriaDTO = new LinhaCategoriaDTO();

            linhaCategoriaDTO.setIdLinhaCategoria(linhaCategoria.getIdLinhaCategoria());
            linhaCategoriaDTO.setCategoriaProdutoId(linhaCategoria.getCategoriaProduto().getId());
            linhaCategoriaDTO.setNomeLinhaCategoria(linhaCategoria.getNomeLinhaCategoria());
            linhaCategoriaDTO.setCodigoLinhaCategoria(linhaCategoria.getCodigoLinhaCategoria());

            linhaCategoriaDTOList.add(linhaCategoriaDTO);
        }

        return linhaCategoriaDTOList;
    }

    public void exportCSV(HttpServletResponse response) {
        try {
            String nomearquivo = "linhas.csv";
            response.setContentType("text/csv");
            response.setHeader(HttpHeaders.CONTENT_DISPOSITION,
                    "attachment; filename=\"" + nomearquivo + "\"");

            PrintWriter writer = response.getWriter();

            ICSVWriter csvWriter = new CSVWriterBuilder(writer)
                    .withSeparator(';')
                    .withEscapeChar(CSVWriter.DEFAULT_ESCAPE_CHARACTER)
                    .withLineEnd(CSVWriter.DEFAULT_LINE_END)
                    .build();

            String headerCSV[] = {"codigo", "nome", "codigo_categoria", "nome_categoria"};
            csvWriter.writeNext(headerCSV);

            for (LinhaCategoria linha : iLinhaCategoriaRepository.findAll()) {
                csvWriter.writeNext(new String[]{
                        linha.getCodigoLinhaCategoria(),
                        linha.getNomeLinhaCategoria(),
                        linha.getCategoriaProduto().getCodigoCategoriaProduto(),
                        linha.getCategoriaProduto().getNomeCategoriaProduto()
                });
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<LinhaCategoria> readAll(MultipartFile file) throws Exception {
        InputStreamReader reader = new InputStreamReader(file.getInputStream());

        CSVReader csvReader = new CSVReaderBuilder(reader)
                .withSkipLines(1)
                .build();

        List<String[]> linhaString = csvReader.readAll();

        List<LinhaCategoria> resultadoLeitura = new ArrayList<>();

        for (String[] linha : linhaString) {

            try {
                String[] bean = linha[0].replaceAll("\"", "").split(";");

                LinhaCategoria linhaCategoria = new LinhaCategoria();
                CategoriaProduto categoriaProduto = categoriaProdutoService.findByCodigoCategoriaProduto(bean[2]);

                String codigo = String.format("%1$10s", bean[0].replaceAll("[^a-zA-Z0-9]+", ""));
                codigo = codigo.replaceAll(" ", "0").toUpperCase();

                linhaCategoria.setCodigoLinhaCategoria(codigo);
                linhaCategoria.setCategoriaProduto(categoriaProduto);
                linhaCategoria.setNomeLinhaCategoria(bean[1]);

                if (!(iLinhaCategoriaRepository.existsByCodigoLinhaCategoria(linhaCategoria.getCodigoLinhaCategoria()))) {
                    save(LinhaCategoriaDTO.of(linhaCategoria));
                    resultadoLeitura.add(linhaCategoria);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return resultadoLeitura;
    }

    public LinhaCategoriaDTO save(LinhaCategoriaDTO linhaCategoriaDTO) {

        LOGGER.debug("Linha Categoria: {}", linhaCategoriaDTO);

        this.validate(linhaCategoriaDTO);

        LOGGER.info("Salvando Linha Categoria");
        LOGGER.debug("Linha Categoria: {}", linhaCategoriaDTO);

        LinhaCategoria linhaCategoria = new LinhaCategoria();

        String codigo = String.format("%1$10s", linhaCategoriaDTO.getCodigoLinhaCategoria().replaceAll("[^a-zA-Z0-9]+", ""));
        codigo = codigo.replaceAll(" ", "0").toUpperCase();

        linhaCategoria.setNomeLinhaCategoria(linhaCategoriaDTO.getNomeLinhaCategoria());
        linhaCategoria.setCodigoLinhaCategoria(codigo);
        linhaCategoria.setCategoriaProduto(iCategoriaProdutoRepository.findById(linhaCategoriaDTO.getCategoriaProdutoId()).get());

        linhaCategoria = this.iLinhaCategoriaRepository.save(linhaCategoria);

        return LinhaCategoriaDTO.of(linhaCategoria);
    }

    public void validate(LinhaCategoriaDTO linhaCategoriaDTO) {

        LOGGER.info("Validando Linha Categoria");

        if (linhaCategoriaDTO == null) {
            throw new IllegalArgumentException("Linha Categoria não pode ser nulo.");
        }
        if (StringUtils.isEmpty(linhaCategoriaDTO.getCategoriaProdutoId())) {
            throw new IllegalArgumentException("ID Categoria Produto não pode ser nulo/vazio.");
        }
        if (StringUtils.isEmpty(linhaCategoriaDTO.getNomeLinhaCategoria())) {
            throw new IllegalArgumentException("Nome de Linha Categoria não poder ser nulo/vazio.");
        }

        if (linhaCategoriaDTO.getCodigoLinhaCategoria().length() > 10) {
            throw new IllegalArgumentException("Codigo de Linha Categoria não poder ser maior que 10 dígitos.");
        }

    }

    public LinhaCategoriaDTO findById(Long id) {
        Optional<LinhaCategoria> linhaCategoriaOptional = this.iLinhaCategoriaRepository.findById(id);

        if (linhaCategoriaOptional.isPresent()) {
            return LinhaCategoriaDTO.of(linhaCategoriaOptional.get());
        }

        throw new IllegalArgumentException(String.format("ID %s não existe.", id));
    }

    public LinhaCategoriaDTO update(LinhaCategoriaDTO linhaCategoriaDTO, Long id) {

        Optional<LinhaCategoria> linhaCategoriaOptional = this.iLinhaCategoriaRepository.findById(id);

        if (linhaCategoriaOptional.isPresent()) {
            LinhaCategoria linhaCategoriaExistente = linhaCategoriaOptional.get();

            LOGGER.info("Atualizando a linha categoria... id:{}", linhaCategoriaExistente.getIdLinhaCategoria());
            LOGGER.debug("Payload: {}", linhaCategoriaDTO);
            LOGGER.debug("Linha categoria existente: {}", linhaCategoriaExistente);

            String codigo = String.format("%1$10s", linhaCategoriaDTO.getCodigoLinhaCategoria().replaceAll("[^a-zA-Z0-9]+", ""));
            codigo = codigo.replaceAll(" ", "0").toUpperCase();

            linhaCategoriaExistente.setNomeLinhaCategoria(linhaCategoriaDTO.getNomeLinhaCategoria());
            linhaCategoriaExistente.setCodigoLinhaCategoria(codigo);
            linhaCategoriaExistente.setCategoriaProduto(iCategoriaProdutoRepository.findById(linhaCategoriaDTO.getCategoriaProdutoId()).get());

            linhaCategoriaExistente = this.iLinhaCategoriaRepository.save(linhaCategoriaExistente);

            return linhaCategoriaDTO.of(linhaCategoriaExistente);
        }

        throw new IllegalArgumentException((String.format("ID %s não existe", id)));

    }

    public void delete(Long id) {
        LOGGER.info("Executando delete para linha categoria de ID [{}]", id);

        this.iLinhaCategoriaRepository.deleteById(id);
    }

}
