package tu.faas.repositories;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tu.faas.domain.entities.Product;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findAllByRestaurantId(Long restaurantId);
    List<Product> findAllByIdIn(Collection<Long> productIds);

    List<Product> findAllByOrderByIdDesc(Pageable pageable);

    List<Product> findAllByNameContainsIgnoreCase(String name);
    List<Product> findAllByOrderByNameAsc();
    List<Product> findAllByOrderByNameDesc();
    List<Product> findAllByOrderByPriceAsc();
    List<Product> findAllByOrderByPriceDesc();
    List<Product> findAllByOrderByIdDesc();
    List<Product> findAllByOrderByIdAsc();

    List<Product> findAllByNameContainsIgnoreCaseOrderByNameAsc(String name);
    List<Product> findAllByNameContainsIgnoreCaseOrderByNameDesc(String name);
    List<Product> findAllByNameContainsIgnoreCaseOrderByPriceAsc(String name);
    List<Product> findAllByNameContainsIgnoreCaseOrderByPriceDesc(String name);
    List<Product> findAllByNameContainsIgnoreCaseOrderByIdAsc(String name);
    List<Product> findAllByNameContainsIgnoreCaseOrderByIdDesc(String name);
}
