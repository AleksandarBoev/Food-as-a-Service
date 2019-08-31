package tu.faas.repositories;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tu.faas.domain.entities.Product;

import java.util.Collection;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findAllByRestaurantId(Long restaurantId);
    List<Product> findAllByIdIn(Collection<Long> productIds);

    List<Product> findAllByOrderByIdDesc(Pageable pageable);
}
