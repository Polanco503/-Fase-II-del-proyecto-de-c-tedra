package sv.edu.udb.restaurant.menu.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import sv.edu.udb.restaurant.menu.model.Product;
import sv.edu.udb.restaurant.menu.repository.ProductRepository;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(
            ProductRepository productRepository) {

        this.productRepository = productRepository;
    }

    public List<Product> findAll() {
        return productRepository.findAll();
    }

    public Optional<Product> findById(Long id) {
        return productRepository.findById(id);
    }

    public Product save(Product product) {
        return productRepository.save(product);
    }

    public boolean existsById(Long id) {
        return productRepository.existsById(id);
    }

    public void deleteById(Long id) {
        productRepository.deleteById(id);
    }
}