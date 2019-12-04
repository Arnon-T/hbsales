package br.com.hbsis.item.pedido;

import br.com.hbsis.categoria.produto.CategoriaProdutoDTO;
import br.com.hbsis.categoria.produto.CategoriaProdutoService;
import br.com.hbsis.fornecedor.FornecedorDTO;
import br.com.hbsis.fornecedor.FornecedorService;
import br.com.hbsis.pedido.IPedidoRepository;
import br.com.hbsis.pedido.Pedido;
import br.com.hbsis.pedido.PedidoDTO;
import br.com.hbsis.periodo.venda.PeriodoVendaService;
import br.com.hbsis.produtos.IProdutoRepository;
import br.com.hbsis.produtos.Produto;
import br.com.hbsis.produtos.ProdutoDTO;
import br.com.hbsis.produtos.ProdutoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PedidoItemService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PedidoItemService.class);

    private final IPedidoItemRepository iPedidoItemRepository;
    private final IPedidoRepository iPedidoRepository;
    private final IProdutoRepository iProdutoRepository;
    private final ProdutoService produtoService;
    private final CategoriaProdutoService categoriaProdutoService;
    private final FornecedorService fornecedorService;
    private final PeriodoVendaService periodoVendaService;


    public PedidoItemService(IPedidoItemRepository iPedidoItemRepository, IPedidoRepository iPedidoRepository, IProdutoRepository iProdutoRepository, ProdutoService produtoService, CategoriaProdutoService categoriaProdutoService, FornecedorService fornecedorService, PeriodoVendaService periodoVendaService) {
        this.iPedidoItemRepository = iPedidoItemRepository;
        this.iPedidoRepository = iPedidoRepository;
        this.iProdutoRepository = iProdutoRepository;
        this.produtoService = produtoService;
        this.categoriaProdutoService = categoriaProdutoService;
        this.fornecedorService = fornecedorService;
        this.periodoVendaService = periodoVendaService;
    }

    public List<PedidoItem> saveAll(List<PedidoItem> pedidoItemList) {

        LOGGER.info("Salvando itens do pedido validados.");

        return this.iPedidoItemRepository.saveAll(pedidoItemList);
    }

    public List<PedidoItem> verificaAll(List<PedidoItem> pedidoItemList){

        List<PedidoItem> itemValidado = new ArrayList<>();

        for (PedidoItem pedidoItem : pedidoItemList) {
            if(periodoVendaService.periodoVendaAtivo(fornecedorService.findById(pedidoItem.getProduto().getLinhaCategoria().getCategoriaProduto().getFornecedor().getId()))) {
                itemValidado.add(pedidoItem);
            }
            else{
                LOGGER.info("Item de ID[{}] fora do período de vendas.", pedidoItem);
            }
        }
        LOGGER.info("Salvando itens do pedido validados.");

        return itemValidado;
    }

    public List<PedidoItem> findAllById(PedidoDTO pedidoDTO){
        List<PedidoItem> pedidoItems = iPedidoItemRepository.findAllById(pedidoDTO.getPedidoItemIdList());
        return pedidoItems;
    }


    public void deleteInBatch(List<PedidoItem> pedidoItemsList) {

        LOGGER.info("Deletando lista de itens do pedido.");

        iPedidoItemRepository.deleteInBatch(pedidoItemsList);
    }

    public PedidoItemDTO update(Long id, PedidoItemDTO pedidoItemDTO) {

        Optional<PedidoItem> pedidoItemOptional = this.iPedidoItemRepository.findById(id);

        if (pedidoItemOptional.isPresent()) {
            PedidoItem pedidoItemExistente = pedidoItemOptional.get();

            LOGGER.info("Atualizando Item {} do Pedido.", id);
            LOGGER.debug("Payload: {}", pedidoItemDTO);
            LOGGER.info("Item existente no pedido: {}", pedidoItemExistente);

            pedidoItemExistente.setPedido(iPedidoRepository.findById(pedidoItemDTO.getPedidoId()).get());
            pedidoItemExistente.setProduto(iProdutoRepository.findById(pedidoItemDTO.getProdutoId()).get());
            pedidoItemExistente.setValorUnitario(pedidoItemDTO.getValorUnitario());
            pedidoItemExistente.setQuantidade(pedidoItemDTO.getQuantidade());

            pedidoItemExistente = this.iPedidoItemRepository.save(pedidoItemExistente);

            return pedidoItemDTO.of(pedidoItemExistente);


        }

        throw new IllegalArgumentException(String.format("ID %s não existe", id));
    }

    public List<PedidoItemDTO> updateAll(List<PedidoItemDTO> pedidoItemDTOList) {

        for (PedidoItemDTO pedidoItemDTO : pedidoItemDTOList) {
            this.update(pedidoItemDTO.getId(), pedidoItemDTO);
        }
        throw new IllegalArgumentException("Update de itens não realizado.");
    }

    public PedidoItemDTO findById(Long id){
        LOGGER.info("Procurando Pedido Item pelo ID {}", id);
        return PedidoItemDTO.of(this.iPedidoItemRepository.findById(id).get());
    }

    public FornecedorDTO buscaFornecedor(PedidoItemDTO pedidoItemDTO) {

        ProdutoDTO produtoDTO = produtoService.findById(pedidoItemDTO.getProdutoId());
        CategoriaProdutoDTO categoriaProdutoDTO = categoriaProdutoService.findById(produtoDTO.getLinhaCategoriaId());
        FornecedorDTO fornecedorDTO = fornecedorService.findById(categoriaProdutoDTO.getFornecedorId());

        return fornecedorDTO;
    }

}
