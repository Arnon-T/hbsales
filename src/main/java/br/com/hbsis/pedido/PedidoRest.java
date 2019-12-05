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

    public PedidoRest(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    @GetMapping("/{id}")
    public PedidoDTO findById(@RequestParam("id") Long id) {

        LOGGER.info("Buscando Pedido de ID: [{}]", id);

        return PedidoDTO.of(this.pedidoService.findByIdObjeto(id));
    }

    @PostMapping
    public PedidoDTO save(@RequestBody PedidoDTO pedidoDTO){

        LOGGER.info("Salvando Pedido");

        return this.pedidoService.save(pedidoDTO);
    }


}
