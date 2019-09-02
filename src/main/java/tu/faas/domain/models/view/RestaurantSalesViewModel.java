package tu.faas.domain.models.view;

import java.math.BigDecimal;
import java.util.List;

public class RestaurantSalesViewModel {
    private Long id;
    private String name;
    private BigDecimal totalIncome;
    private List<ProductOrderRestaurantSalesViewModel> productSaleViewModes;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getTotalIncome() {
        return totalIncome;
    }

    public void setTotalIncome(BigDecimal totalIncome) {
        this.totalIncome = totalIncome;
    }

    public List<ProductOrderRestaurantSalesViewModel> getProductSaleViewModes() {
        return productSaleViewModes;
    }

    public void setProductSaleViewModes(List<ProductOrderRestaurantSalesViewModel> productSaleViewModes) {
        this.productSaleViewModes = productSaleViewModes;
    }
}
