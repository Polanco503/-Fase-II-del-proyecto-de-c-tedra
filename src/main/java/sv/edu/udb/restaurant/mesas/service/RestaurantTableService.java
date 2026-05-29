package sv.edu.udb.restaurant.mesas.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import sv.edu.udb.restaurant.mesas.model.RestaurantTable;
import sv.edu.udb.restaurant.mesas.repository.RestaurantTableRepository;

@Service
public class RestaurantTableService {

    private final RestaurantTableRepository restaurantTableRepository;

    public RestaurantTableService(
            RestaurantTableRepository restaurantTableRepository) {

        this.restaurantTableRepository = restaurantTableRepository;
    }

    public List<RestaurantTable> findAll() {
        return restaurantTableRepository.findAll();
    }

    public Optional<RestaurantTable> findById(Long id) {
        return restaurantTableRepository.findById(id);
    }

    public RestaurantTable save(RestaurantTable table) {
        return restaurantTableRepository.save(table);
    }

    public boolean existsById(Long id) {
        return restaurantTableRepository.existsById(id);
    }

    public void deleteById(Long id) {
        restaurantTableRepository.deleteById(id);
    }
}