package tu.faas.domain.models.view;

import java.math.BigDecimal;
import java.util.List;

public class ShoppingCartViewModel {
    private List<ProductShoppingCartViewModel> products;
    private BigDecimal totalPrice;

    public List<ProductShoppingCartViewModel> getProducts() {
        return products;
    }

    public void setProducts(List<ProductShoppingCartViewModel> products) {
        this.products = products;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }
}
