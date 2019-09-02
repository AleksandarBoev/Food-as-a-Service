package tu.faas.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tu.faas.domain.entities.ProductOrder;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ProductOrderRepository extends JpaRepository<ProductOrder, Long> {
    List<ProductOrder> findAllByProductRestaurantIdAndDateOfOrderBetweenOrderByDateOfOrderDesc(
            Long restaurantId, LocalDate date1, LocalDate date2);

    List<ProductOrder> findAllByProductRestaurantIdOrderByDateOfOrderDesc(Long restaurantId);

    List<ProductOrder> findAllByProductRestaurantIdAndDateOfOrderAfterOrderByDateOfOrderDesc(Long restaurantId, LocalDate date);

    List<ProductOrder> findAllByProductRestaurantIdAndDateOfOrderBeforeOrderByDateOfOrderDesc(Long restaurantId, LocalDate date);
}
