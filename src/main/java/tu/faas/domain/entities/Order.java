package tu.faas.domain.entities;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

//@Entity
//@Table(name = "orders")
public class Order extends BaseEntity {
//    private LocalDateTime dateTime;
//    private User user;
//    private List<Product> products;
//
//    @Column(name = "date_time", nullable = false)
//    public LocalDateTime getDateTime() {
//        return dateTime;
//    }
//
//    public void setDateTime(LocalDateTime dateTime) {
//        this.dateTime = dateTime;
//    }
//
//    @Column(nullable = false)
//    public User getUser() {
//        return user;
//    }
//
//    public void setUser(User user) {
//        this.user = user;
//    }
//  //TODO have a @ManyToMany on the product side to. Will be easier to make statistics... I think.
//    @ManyToMany
//    @JoinTable(
//            name = "products_orders",
//            joinColumns = @JoinColumn(name = "order_id"),
//            inverseJoinColumns = @JoinColumn(name = "product_id"))
//    public List<Product> getProducts() {
//        return products;
//    }
//
//    public void setProducts(List<Product> products) {
//        this.products = products;
//    }
}
