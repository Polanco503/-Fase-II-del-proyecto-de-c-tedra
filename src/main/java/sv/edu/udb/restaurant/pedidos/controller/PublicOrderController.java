package sv.edu.udb.restaurant.pedidos.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import sv.edu.udb.restaurant.pedidos.dto.PublicOrderRequest;
import sv.edu.udb.restaurant.pedidos.dto.PublicOrderResponse;
import sv.edu.udb.restaurant.pedidos.service.PublicOrderService;

@RestController
@RequestMapping("/api/public/orders")
public class PublicOrderController {

    private final PublicOrderService publicOrderService;

    public PublicOrderController(
            PublicOrderService publicOrderService) {

        this.publicOrderService = publicOrderService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PublicOrderResponse create(
            @Valid @RequestBody PublicOrderRequest request) {

        return publicOrderService.createPublicOrder(
                request);
    }
}
