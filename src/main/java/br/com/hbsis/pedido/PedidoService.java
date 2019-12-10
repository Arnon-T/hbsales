package br.com.hbsis.pedido;

import br.com.hbsis.funcionario.FuncionarioService;
import br.com.hbsis.item.pedido.PedidoItemService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PedidoService {
    /*
    Verificar periodo de vendas para save;
    Termo de aceite, boolean;
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(PedidoService.class);

    private final IPedidoRepository pedidoRepository;
    private final PedidoItemService pedidoItemService;
    private final FuncionarioService funcionarioService;

    public PedidoService(IPedidoRepository pedidoRepository, PedidoItemService pedidoItemService, FuncionarioService funcionarioService) {
        this.pedidoRepository = pedidoRepository;
        this.pedidoItemService = pedidoItemService;
        this.funcionarioService = funcionarioService;
    }

    public PedidoDTO save(PedidoDTO pedidoDTO) {

        LOGGER.info("Salvando Pedido");
        LOGGER.debug("Payload: {}", pedidoDTO);

        Pedido pedido = new Pedido();

        pedido.setFuncionario(funcionarioService.findByIdObjeto(pedidoDTO.getFuncionarioId()));
        pedido.setValorTotal(pedidoDTO.getValorTotal());
        pedido.setPedidoItemList(pedidoItemService.verificaAll(pedidoItemService.findAllById(pedidoDTO)));
        pedido.setData(pedidoDTO.getData());


        pedido = pedidoRepository.save(pedido);

        return PedidoDTO.of(pedido);
    }

    public Pedido findByIdObjeto (Long id){

        Optional<Pedido> pedidoOptional = pedidoRepository.findById(id);

        if(pedidoOptional.isPresent()){
            return pedidoOptional.get();
        }
        throw new IllegalArgumentException(String.format("ID %s não localizado.", id));
    }


//    save
//            update
//    update itens
//    delete
//    delete itens
//    validate periodo


}
