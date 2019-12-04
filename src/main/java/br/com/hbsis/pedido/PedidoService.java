package br.com.hbsis.pedido;

import br.com.hbsis.categoria.produto.CategoriaProdutoService;
import br.com.hbsis.fornecedor.FornecedorService;
import br.com.hbsis.funcionario.FuncionarioService;
import br.com.hbsis.item.pedido.IPedidoItemRepository;
import br.com.hbsis.item.pedido.PedidoItemService;
import br.com.hbsis.periodo.venda.PeriodoVendaService;
import br.com.hbsis.produtos.ProdutoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class PedidoService {
    /*
    Verificar periodo de vendas para save;
    Termo de aceite, boolean;
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(PedidoService.class);

    private final IPedidoRepository pedidoRepository;
    private final PedidoItemService pedidoItemService;
    private final PeriodoVendaService periodoVendaService;
    private final ProdutoService produtoService;
    private final FornecedorService fornecedorService;
    private final CategoriaProdutoService categoriaProdutoService;
    private final IPedidoItemRepository iPedidoItemRepository;
    private final FuncionarioService funcionarioService;


    public PedidoService(IPedidoRepository pedidoRepository, PedidoItemService pedidoItemService, PeriodoVendaService periodoVendaService, ProdutoService produtoService, FornecedorService fornecedorService, CategoriaProdutoService categoriaProdutoService, IPedidoItemRepository iPedidoItemRepository, FuncionarioService funcionarioService) {
        this.pedidoRepository = pedidoRepository;
        this.pedidoItemService = pedidoItemService;
        this.periodoVendaService = periodoVendaService;
        this.produtoService = produtoService;
        this.fornecedorService = fornecedorService;
        this.categoriaProdutoService = categoriaProdutoService;
        this.iPedidoItemRepository = iPedidoItemRepository;
        this.funcionarioService = funcionarioService;
    }

    private Pedido save(PedidoDTO pedidoDTO) {

        LOGGER.info("Salvando Pedido");
        LOGGER.debug("Payload: {}", pedidoDTO);

        Pedido pedido = new Pedido();

        pedido.setFuncionario(funcionarioService.findByIdObjeto(pedidoDTO.getFuncionarioId()));
        pedido.setValorTotal(pedidoDTO.getValorTotal());
        pedido.setPedidoItemList(pedidoItemService.verificaAll(pedidoItemService.findAllById(pedidoDTO)));
        pedido.setData(pedidoDTO.getData());


        pedido = pedidoRepository.save(pedido);

        return pedido;
    }




//    save
//            update
//    update itens
//    delete
//    delete itens
//    validate periodo


}
