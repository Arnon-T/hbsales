package br.com.hbsis.categoria.produto;

import br.com.hbsis.fornecedor.Fornecedor;
import br.com.hbsis.fornecedor.FornecedorDTO;
import br.com.hbsis.fornecedor.FornecedorService;
import br.com.hbsis.fornecedor.IFornecedorRepository;
import br.com.hbsis.usuario.IUsuarioRepository;
import com.opencsv.CSVParser;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.ICSVParser;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import rx.BackpressureOverflow;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
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


    public List<CategoriaProduto> findAll(){
        return iCategoriaProdutoRepository.findAll();
    }

    public List<String> listToPrint(){
        List<String> lista = new ArrayList<>();
        for(CategoriaProduto linhaCSV : iCategoriaProdutoRepository.findAll()){
            String construtor = linhaCSV.getId() + ";" + linhaCSV.getCodigoCategoriaProduto() + ";" + linhaCSV.getNomeCategoriaProduto() + ";" + linhaCSV.getFornecedor().getId()+ ";";
            lista.add(construtor);
        }
        return lista;
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

            String[] bean = linha[0].replaceAll("\"","").split(";");

            CategoriaProduto categoriaProduto = new CategoriaProduto();
            Fornecedor fornecedor = new Fornecedor();
            FornecedorDTO fornecedorDTO = new FornecedorDTO();

            categoriaProduto.setCodigoCategoriaProduto(Long.parseLong(bean[1]));
            categoriaProduto.setNomeCategoriaProduto(bean[2]);

            fornecedorDTO = fornecedorService.findById(Long.parseLong(bean[3]));

            fornecedor.setId(fornecedorDTO.getId());
            fornecedor.setRazaoSocial(fornecedorDTO.getRazaoSocial());
            fornecedor.setCnpj(fornecedorDTO.getCnpj());
            fornecedor.setNomeFantasia(fornecedorDTO.getNomeFantasia());
            fornecedor.setEndereco(fornecedorDTO.getEndereco());
            fornecedor.setTelefone(fornecedorDTO.getTelefone());
            fornecedor.setEmail(fornecedorDTO.getEmail());

            categoriaProduto.setFornecedor(fornecedor);

            resultadoLeitura.add(categoriaProduto);
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }

        return iCategoriaProdutoRepository.saveAll(resultadoLeitura);
    }

    public List<CategoriaProduto> saveAll(List<CategoriaProduto> categoriaProdutos) throws Exception {

        return iCategoriaProdutoRepository.saveAll(categoriaProdutos);
    }

    public CategoriaProdutoDTO save(CategoriaProdutoDTO categoriaProdutoDTO){

        this.validate(categoriaProdutoDTO);

        LOGGER.info("Salvando Categoria Produto");
        LOGGER.debug("Categoria Produto: {}", categoriaProdutoDTO);

        CategoriaProduto categoriaProduto = new CategoriaProduto();

        categoriaProduto.setNomeCategoriaProduto(categoriaProdutoDTO.getNomeCategoriaProduto());
        categoriaProduto.setCodigoCategoriaProduto(categoriaProdutoDTO.getCodigoCategoriaProduto());
        categoriaProduto.setFornecedor(categoriaProdutoDTO.getFornecedor());

        categoriaProduto = this.iCategoriaProdutoRepository.save(categoriaProduto);

        return CategoriaProdutoDTO.of(categoriaProduto);

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
        if (StringUtils.isEmpty(categoriaProdutoDTO.getFornecedor().getId())) {
            throw new IllegalArgumentException("ID Fornecedor não deve ser nulo/vazio");

        }
    }

    public CategoriaProdutoDTO findById(Long id) {
        Optional<CategoriaProduto> categoriaProdutoOptional = this.iCategoriaProdutoRepository.findById(id);

        if(categoriaProdutoOptional.isPresent()){
            return CategoriaProdutoDTO.of(categoriaProdutoOptional.get());
        }

        throw new IllegalArgumentException(String.format("ID %s não existe", id));
    }

    public CategoriaProdutoDTO update(CategoriaProdutoDTO categoriaProdutoDTO, Long id) {

        Optional<CategoriaProduto> categoriaProdutoExistenteOptional = this.iCategoriaProdutoRepository.findById(id);

        if (categoriaProdutoExistenteOptional.isPresent()){
            CategoriaProduto categoriaProdutoExistente = categoriaProdutoExistenteOptional.get();

            LOGGER.info("Atualizando o fornecedor... id:{}", categoriaProdutoExistente.getId());
            LOGGER.debug("Payload: {}", categoriaProdutoDTO);
            LOGGER.debug("Fornecedor Existente: {}", categoriaProdutoExistente);


            categoriaProdutoDTO.setNomeCategoriaProduto(categoriaProdutoDTO.getNomeCategoriaProduto());
            categoriaProdutoDTO.setCodigoCategoriaProduto(categoriaProdutoDTO.getCodigoCategoriaProduto());
            categoriaProdutoDTO.setFornecedor(categoriaProdutoDTO.getFornecedor());

            categoriaProdutoExistente = this.iCategoriaProdutoRepository.save(categoriaProdutoExistente);

            return categoriaProdutoDTO.of(categoriaProdutoExistente);
        }

        throw new IllegalArgumentException(String.format("ID %s não existe", id));

    }

    public void delete(Long id){
        LOGGER.info("Executando delete para categoria produto de ID> [{}]", id);

        this.iCategoriaProdutoRepository.deleteById(id);
    }

}
