package tu.faas.domain.models.view;

import java.math.BigDecimal;

public class ProductShoppingCartViewModel extends ProductViewModel {
    private Integer quantity;
    private BigDecimal totalPrice;

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }
}
