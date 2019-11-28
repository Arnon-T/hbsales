package br.com.hbsis.linha.categoria;

import br.com.hbsis.categoria.produto.CategoriaProduto;
import br.com.hbsis.categoria.produto.CategoriaProdutoDTO;
import br.com.hbsis.categoria.produto.CategoriaProdutoService;
import br.com.hbsis.fornecedor.Fornecedor;
import br.com.hbsis.fornecedor.FornecedorDTO;
import br.com.hbsis.fornecedor.FornecedorService;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import org.hibernate.boot.model.naming.IllegalIdentifierException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class LinhaCategoriaService {

    private static final Logger LOGGER = LoggerFactory.getLogger(LinhaCategoriaService.class);

    private final ILinhaCategoriaRepository iLinhaCategoriaRepository;
    private final CategoriaProdutoService categoriaProdutoService;
    private final FornecedorService fornecedorService;

    public LinhaCategoriaService(ILinhaCategoriaRepository iLinhaCategoriaRepository, CategoriaProdutoService categoriaProdutoService, FornecedorService fornecedorService){
        this.iLinhaCategoriaRepository = iLinhaCategoriaRepository;
        this.categoriaProdutoService = categoriaProdutoService;
        this.fornecedorService = fornecedorService;
    }

    public List<LinhaCategoria> findAll(){ return iLinhaCategoriaRepository.findAll(); }

    public List<LinhaCategoria> readAll(MultipartFile file) throws Exception{
        InputStreamReader reader = new InputStreamReader(file.getInputStream());

        CSVReader csvReader = new CSVReaderBuilder(reader)
                .withSkipLines(1)
                .build();

        List<String[]> linhaString = csvReader.readAll();

        List<LinhaCategoria> resultadoLeitura = new ArrayList<>();

        for(String[] linha : linhaString){
            try{
                String[] bean = linha[0].replaceAll("\"","").split(";");

                //REVISAR TUDO, USAR APENAS ID 

                LinhaCategoria linhaCategoria = new LinhaCategoria();
                CategoriaProduto categoriaProduto = new CategoriaProduto();
                Fornecedor fornecedor = new Fornecedor();

                CategoriaProdutoDTO categoriaProdutoDTO = categoriaProdutoService.findById(Long.parseLong(bean[1]));
                FornecedorDTO fornecedorDTO = fornecedorService.findById(categoriaProdutoDTO.getFornecedor().getId());

                linhaCategoria.setNomeLinhaCategoria(bean[2]);
                categoriaProduto.setFornecedorId(Long.parseLong(bean[3]));

                linhaCategoria.setCategoriaProduto(categoriaProduto);

                resultadoLeitura.add(linhaCategoria);
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
        return iLinhaCategoriaRepository.saveAll(resultadoLeitura);
    }

    public List<LinhaCategoria> saveAll(List<LinhaCategoria> linhaCategoria){
        return iLinhaCategoriaRepository.saveAll(linhaCategoria);
    }

    public LinhaCategoriaDTO save(LinhaCategoriaDTO linhaCategoriaDTO){

        LOGGER.debug("Linha Categoria: {}", linhaCategoriaDTO);

        this.validate(linhaCategoriaDTO);

        LOGGER.info("Salvando Linha Categoria");
        LOGGER.debug("Linha Categoria: {}", linhaCategoriaDTO);

        LinhaCategoria linhaCategoria = new LinhaCategoria();

        linhaCategoria.setNomeLinhaCategoria(linhaCategoriaDTO.getNomeLinhaCategoria());
        linhaCategoria.setCategoriaProduto(linhaCategoriaDTO.getCategoriaProduto());

        linhaCategoria = this.iLinhaCategoriaRepository.save(linhaCategoria);

        return LinhaCategoriaDTO.of(linhaCategoria);
    }

    public void validate(LinhaCategoriaDTO linhaCategoriaDTO){

        LOGGER.info("Validando Linha Categoria");

        if (linhaCategoriaDTO == null){
            throw new IllegalArgumentException("Linha Categoria não pode ser nulo.");
        }
        if (StringUtils.isEmpty(linhaCategoriaDTO.getCategoriaProduto().getId())){
            throw new IllegalArgumentException("ID Categoria Produto não pode ser nulo/vazio.");
        }
        if (StringUtils.isEmpty(linhaCategoriaDTO.getNomeLinhaCategoria())){
            throw new IllegalArgumentException("Nome de Linha Categoria não poder ser nulo/vazio.");
        }
    }

    public LinhaCategoriaDTO findById(Long id){
        Optional<LinhaCategoria> linhaCategoriaOptional = this.iLinhaCategoriaRepository.findById(id);

        if(linhaCategoriaOptional.isPresent()){
            return LinhaCategoriaDTO.of(linhaCategoriaOptional.get());
        }

        throw new IllegalArgumentException(String.format("ID %s não existe.", id));
    }

    public LinhaCategoriaDTO update(LinhaCategoriaDTO linhaCategoriaDTO, Long id){

        Optional<LinhaCategoria> linhaCategoriaOptional = this.iLinhaCategoriaRepository.findById(id);

        if (linhaCategoriaOptional.isPresent()){
            LinhaCategoria linhaCategoriaExistente = linhaCategoriaOptional.get();

            LOGGER.info("Atualizando a linha categoria... id:{}", linhaCategoriaExistente.getIdLinhaCategoria());
            LOGGER.debug("Payload: {}", linhaCategoriaDTO);
            LOGGER.debug("Linha categoria existente: {}", linhaCategoriaExistente);

            linhaCategoriaExistente.setCategoriaProduto(linhaCategoriaDTO.getCategoriaProduto());
            linhaCategoriaExistente.setNomeLinhaCategoria(linhaCategoriaDTO.getNomeLinhaCategoria());

            linhaCategoriaExistente = this.iLinhaCategoriaRepository.save(linhaCategoriaExistente);

            return linhaCategoriaDTO.of(linhaCategoriaExistente);
        }

        throw new IllegalArgumentException((String.format("ID %s não existe", id)));

    }

    public void delete(Long id){
        LOGGER.info("Executando delete para linha categoria de ID [{}]", id);

        this.iLinhaCategoriaRepository.deleteById(id);
    }

}
