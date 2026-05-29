package sv.edu.udb.restaurant.pedidos.controller;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import sv.edu.udb.restaurant.pedidos.dto.DetallePedidoRequest;
import sv.edu.udb.restaurant.pedidos.model.DetallePedido;
import sv.edu.udb.restaurant.pedidos.service.DetallePedidoService;

@RestController
@RequestMapping("/api/detalles")
public class DetallePedidoController {

    private final DetallePedidoService detallePedidoService;

    public DetallePedidoController(
            DetallePedidoService detallePedidoService) {

        this.detallePedidoService = detallePedidoService;
    }

    @GetMapping
    public List<DetallePedido> findAll() {
        return detallePedidoService.findAll();
    }

    @GetMapping("/pedido/{pedidoId}")
    public List<DetallePedido> findByPedido(
            @PathVariable Long pedidoId) {

        return detallePedidoService.findByPedido(pedidoId);
    }

    @PostMapping
    public DetallePedido create(
            @RequestBody DetallePedidoRequest request) {

        return detallePedidoService.agregarProducto(
                request.pedidoId(),
                request.productoId(),
                request.cantidad());
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        detallePedidoService.delete(id);
    }
}