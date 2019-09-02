package tu.faas.domain.entities;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "product_orders")
public class ProductOrder extends BaseEntity {
    private Product product;
    private Order order;
    private Integer quantity;
    //A product price can be edited by managers. This would be bad for order history and statistics.
    //Also discounts can be added later.
    private BigDecimal productPriceAtCheckout;
    private LocalDate dateOfOrder;

    @ManyToOne
    @JoinColumn(name = "product_id", referencedColumnName = "id")
    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    @ManyToOne
    @JoinColumn(name = "order_id", referencedColumnName = "id")
    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    @Column(name = "product_price_at_checkout")
    public BigDecimal getProductPriceAtCheckout() {
        return productPriceAtCheckout;
    }

    public void setProductPriceAtCheckout(BigDecimal productPriceAtCheckout) {
        this.productPriceAtCheckout = productPriceAtCheckout;
    }

    public LocalDate getDateOfOrder() {
        return dateOfOrder;
    }

    public void setDateOfOrder(LocalDate dateOfOrder) {
        this.dateOfOrder = dateOfOrder;
    }
}
