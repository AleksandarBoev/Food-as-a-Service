package tu.faas.domain.entities;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "orders")
public class Order extends BaseEntity {
    private User customer;
    private LocalDateTime dateTimeOfOrder;
    private List<ProductOrder> productOrders;
    private BigDecimal totalPrice;
    private String billingType;
    private String address;

    @ManyToOne
    @JoinColumn(name = "customer_id", referencedColumnName = "id")
    public User getCustomer() {
        return customer;
    }

    public void setCustomer(User customer) {
        this.customer = customer;
    }

    @Column(name = "date_time_of_order")
    public LocalDateTime getDateTimeOfOrder() {
        return dateTimeOfOrder;
    }

    public void setDateTimeOfOrder(LocalDateTime dateTimeOfOrder) {
        this.dateTimeOfOrder = dateTimeOfOrder;
    }

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    public List<ProductOrder> getProductOrders() {
        return productOrders;
    }

    public void setProductOrders(List<ProductOrder> productOrders) {
        this.productOrders = productOrders;
    }

    @Column(name = "total_price")
    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    @Column(name = "billing_type")
    public String getBillingType() {
        return billingType;
    }

    public void setBillingType(String billingType) {
        this.billingType = billingType;
    }

    @Column
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
