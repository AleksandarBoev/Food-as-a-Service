package tu.faas.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tu.faas.domain.entities.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

}
