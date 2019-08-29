package tu.faas.domain.entities;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

//@Entity
//@Table(name = "orders")
public class Order extends BaseEntity {
    private User user;
    private Product product;
    private Integer quantity;

}
