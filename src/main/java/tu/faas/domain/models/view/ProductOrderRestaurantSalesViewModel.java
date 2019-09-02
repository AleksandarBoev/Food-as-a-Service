package tu.faas.domain.models.view;

import java.math.BigDecimal;
import java.time.LocalDate;

public class ProductOrderRestaurantSalesViewModel {
    private String productName;
    private Integer quantity;
    private BigDecimal productPriceAtCheckout;
    private BigDecimal totalPrice;
    private LocalDate dateOfOrder;

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getProductPriceAtCheckout() {
        return productPriceAtCheckout;
    }

    public void setProductPriceAtCheckout(BigDecimal productPriceAtCheckout) {
        this.productPriceAtCheckout = productPriceAtCheckout;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public LocalDate getDateOfOrder() {
        return dateOfOrder;
    }

    public void setDateOfOrder(LocalDate dateOfOrder) {
        this.dateOfOrder = dateOfOrder;
    }
}
