package br.com.hbsis.funcionario;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class FuncionarioService {

    private static final Logger LOGGER = LoggerFactory.getLogger(FuncionarioService.class);

    private final IFuncionarioRepository iFuncionarioRepository;

    public FuncionarioService(IFuncionarioRepository iFuncionarioRepository) {
        this.iFuncionarioRepository = iFuncionarioRepository;
    }

    public FuncionarioDTO save(FuncionarioDTO funcionarioDTO){

        this.validate(funcionarioDTO);

        Funcionario funcionario = new Funcionario();

        funcionario.setNome(funcionarioDTO.getNome());
        funcionario.setEmail(funcionarioDTO.getEmail());

        funcionario = this.iFuncionarioRepository.save(funcionario);

        return FuncionarioDTO.of(funcionario);

    }

    public FuncionarioDTO update(Long id, FuncionarioDTO funcionarioDTO){

        Optional<Funcionario> funcionarioOptional = this.iFuncionarioRepository.findById(id);

        if(funcionarioOptional.isPresent()){
            Funcionario funcionarioExistente = funcionarioOptional.get();

            funcionarioExistente.setNome(funcionarioDTO.getNome());
            funcionarioExistente.setEmail(funcionarioDTO.getEmail());

            funcionarioExistente = this.iFuncionarioRepository.save(funcionarioExistente);

            return FuncionarioDTO.of(funcionarioExistente);
        }
        throw new IllegalArgumentException(String.format("ID %s não existe.", id));
    }

    public FuncionarioDTO findById(Long id){

        Optional<Funcionario> funcionarioOptional = this.iFuncionarioRepository.findById(id);

        LOGGER.info("Buscando Funcionario de ID...[{}]", id);

        if(funcionarioOptional.isPresent()){
            return FuncionarioDTO.of(funcionarioOptional.get());
        }

        throw new IllegalArgumentException(String.format("ID %s não localizado.", id));

    }
    public Funcionario findByIdObjeto(Long id){
        Optional<Funcionario> funcionarioOptional = this.iFuncionarioRepository.findById(id);

        if(funcionarioOptional.isPresent()){
            return funcionarioOptional.get();
        }
        throw new IllegalArgumentException(String.format("ID %s não localizado.", id));
    }




    public void delete(Long id){

        LOGGER.info("Executando delete no ID: {}", id);

        this.iFuncionarioRepository.deleteById(id);
    }



    public void validate(FuncionarioDTO funcionarioDTO){

        if(funcionarioDTO == null){
            LOGGER.debug("Payload: {}", funcionarioDTO);
            throw new IllegalArgumentException(String.format("Funcionário não pode ser nulo."));
        }
    }

}
