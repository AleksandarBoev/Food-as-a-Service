package tu.faas.domain.models.view;

import java.math.BigDecimal;
import java.util.List;

public class ShoppingCartViewModel {
    private List<ProductShoppingCartViewModel> products;
    private BigDecimal totalSum;

    public List<ProductShoppingCartViewModel> getProducts() {
        return products;
    }

    public void setProducts(List<ProductShoppingCartViewModel> products) {
        this.products = products;
    }

    public BigDecimal getTotalSum() {
        return totalSum;
    }

    public void setTotalSum(BigDecimal totalSum) {
        this.totalSum = totalSum;
    }
}
