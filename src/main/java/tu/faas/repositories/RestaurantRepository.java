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

    List<Restaurant> findAllByNameContainsIgnoreCase(String name);
    List<Restaurant> findAllByOrderByNameAsc();
    List<Restaurant> findAllByOrderByNameDesc();
    List<Restaurant> findAllByOrderByIdDesc();
    List<Restaurant> findAllByOrderByIdAsc();

    List<Restaurant> findAllByNameContainsIgnoreCaseOrderByNameAsc(String name);
    List<Restaurant> findAllByNameContainsIgnoreCaseOrderByNameDesc(String name);
    List<Restaurant> findAllByNameContainsIgnoreCaseOrderByIdAsc(String name);
    List<Restaurant> findAllByNameContainsIgnoreCaseOrderByIdDesc(String name);
}
