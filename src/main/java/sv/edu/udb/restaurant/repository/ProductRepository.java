package sv.edu.udb.restaurant.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sv.edu.udb.restaurant.model.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
