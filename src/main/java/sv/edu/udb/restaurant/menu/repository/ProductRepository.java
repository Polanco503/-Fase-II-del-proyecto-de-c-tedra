package sv.edu.udb.restaurant.menu.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sv.edu.udb.restaurant.menu.model.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {

}