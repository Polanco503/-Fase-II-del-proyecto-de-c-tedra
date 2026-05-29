package sv.edu.udb.restaurant.menu.controller;

import jakarta.validation.Valid;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import sv.edu.udb.restaurant.dto.MessageResponse;
import sv.edu.udb.restaurant.menu.model.Product;
import sv.edu.udb.restaurant.menu.service.ProductService;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(
            ProductService productService) {

        this.productService = productService;
    }

    @PostMapping
    public Product create(
            @Valid @RequestBody Product product) {

        product.setId(null);

        return productService.save(product);
    }

    @GetMapping
    public List<Product> findAll() {

        return productService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id) {

        return productService.findById(id)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() ->
                        ResponseEntity.status(404)
                                .body(new MessageResponse("Producto no encontrado")));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(
            @PathVariable Long id,
            @Valid @RequestBody Product product) {

        return productService.findById(id)
                .<ResponseEntity<?>>map(existingProduct -> {

                    existingProduct.setName(product.getName());
                    existingProduct.setPrice(product.getPrice());
                    existingProduct.setStock(product.getStock());

                    return ResponseEntity.ok(
                            productService.save(existingProduct));
                })
                .orElseGet(() ->
                        ResponseEntity.status(404)
                                .body(new MessageResponse("Producto no encontrado")));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {

        if (!productService.existsById(id)) {

            return ResponseEntity.status(404)
                    .body(new MessageResponse("Producto no encontrado"));
        }

        productService.deleteById(id);

        return ResponseEntity.noContent().build();
    }
}