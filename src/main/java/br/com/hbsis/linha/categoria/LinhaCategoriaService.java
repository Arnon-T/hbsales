package br.com.hbsis.linha.categoria;

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

    public LinhaCategoriaService(ILinhaCategoriaRepository iLinhaCategoriaRepository, ICategoriaProdutoRepository iCategoriaProdutoRepository) {
        this.iLinhaCategoriaRepository = iLinhaCategoriaRepository;
        this.iCategoriaProdutoRepository = iCategoriaProdutoRepository;

    }

    public List<LinhaCategoria> findAll() {
        return iLinhaCategoriaRepository.findAll();
    }

    public void exportCSV(HttpServletResponse response) throws Exception{
        try {
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

            for (LinhaCategoria linha : iLinhaCategoriaRepository.findAll()) {
                csvWriter.writeNext(new String[] {linha.getIdLinhaCategoria().toString(), linha.getCategoriaProduto().getId().toString(), linha.getNomeLinhaCategoria()});
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

                linhaCategoria.setIdLinhaCategoria(Long.parseLong(bean[0]));
                linhaCategoria.setCategoriaProduto(iCategoriaProdutoRepository.findById(Long.parseLong(bean[1])).get());
                linhaCategoria.setNomeLinhaCategoria(bean[2]);

                resultadoLeitura.add(linhaCategoria);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return iLinhaCategoriaRepository.saveAll(resultadoLeitura);
    }

    public LinhaCategoriaDTO save(LinhaCategoriaDTO linhaCategoriaDTO) {

        LOGGER.debug("Linha Categoria: {}", linhaCategoriaDTO);

        this.validate(linhaCategoriaDTO);

        LOGGER.info("Salvando Linha Categoria");
        LOGGER.debug("Linha Categoria: {}", linhaCategoriaDTO);

        LinhaCategoria linhaCategoria = new LinhaCategoria();

        linhaCategoria.setNomeLinhaCategoria(linhaCategoriaDTO.getNomeLinhaCategoria());
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

            linhaCategoriaExistente.setCategoriaProduto(iCategoriaProdutoRepository.findById(linhaCategoriaDTO.getCategoriaProdutoId()).get());
            linhaCategoriaExistente.setNomeLinhaCategoria(linhaCategoriaDTO.getNomeLinhaCategoria());

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
