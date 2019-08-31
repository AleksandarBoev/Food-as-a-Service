package tu.faas.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tu.faas.domain.entities.Restaurant;

import org.springframework.data.domain.Pageable;
import java.util.List;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {
    List<Restaurant> findAllByManagerId(Long id);
    List<Restaurant> findAllByOrderByIdDesc(Pageable pageable);
}
