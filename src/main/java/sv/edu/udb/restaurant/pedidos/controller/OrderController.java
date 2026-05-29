package sv.edu.udb.restaurant.pedidos.controller;

import java.util.List;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import sv.edu.udb.restaurant.dto.MessageResponse;
import sv.edu.udb.restaurant.pedidos.dto.OrderSummaryResponse;
import sv.edu.udb.restaurant.pedidos.dto.UpdateOrderStatusRequest;
import sv.edu.udb.restaurant.pedidos.model.Order;
import sv.edu.udb.restaurant.pedidos.service.OrderService;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(
            OrderService orderService) {

        this.orderService = orderService;
    }

    @PostMapping
    public Order create(@RequestBody Order order) {

        order.setId(null);

        return orderService.save(order);
    }

    @GetMapping
    public List<OrderSummaryResponse> findAll() {

        return orderService.findAllSummaries();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id) {

        return orderService.findSummaryById(id)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() ->
                        ResponseEntity.status(404)
                                .body(new MessageResponse("Pedido no encontrado")));
    }

    @PutMapping("/{id}/status")
    public OrderSummaryResponse updateStatus(
            @PathVariable Long id,
            @Valid @RequestBody UpdateOrderStatusRequest request) {

        return orderService.updateStatus(
                id,
                request.status());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {

        if (!orderService.existsById(id)) {

            return ResponseEntity.status(404)
                    .body(new MessageResponse("Pedido no encontrado"));
        }

        orderService.deleteById(id);

        return ResponseEntity.noContent().build();
    }
}
