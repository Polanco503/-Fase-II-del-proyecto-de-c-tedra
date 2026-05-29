package sv.edu.udb.restaurant.mesas.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sv.edu.udb.restaurant.mesas.model.RestaurantTable;

public interface RestaurantTableRepository extends JpaRepository<RestaurantTable, Long> {

}