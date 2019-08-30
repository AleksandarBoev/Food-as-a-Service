package tu.faas.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tu.faas.domain.entities.ProductOrder;

@Repository
public interface ProductOrderRepository extends JpaRepository<ProductOrder, Long> {
}
