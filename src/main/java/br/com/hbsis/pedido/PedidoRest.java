package br.com.hbsis.pedido;

import br.com.hbsis.item.pedido.PedidoItem;
import br.com.hbsis.item.pedido.PedidoItemService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/funcionarios/pedidos/")
public class PedidoRest {

    private static final Logger LOGGER = LoggerFactory.getLogger(PedidoRest.class);

    private final PedidoService pedidoService;
    private final PedidoItemService pedidoItemService;

    public PedidoRest(PedidoService pedidoService, PedidoItemService pedidoItemService) {
        this.pedidoService = pedidoService;
        this.pedidoItemService = pedidoItemService;
    }

    @GetMapping("/{id}")
    public PedidoDTO findById(@RequestParam("id") Long id) {

        LOGGER.info("Buscando Pedido de ID: [{}]", id);

        return this.pedidoService.findByID(id);
    }

    @PostMapping
    public PedidoDTO save(PedidoDTO pedidoDTO){

        LOGGER.info("Salvando Pedido");

        return this.pedidoService.save(pedidoDTO);
    }


}
