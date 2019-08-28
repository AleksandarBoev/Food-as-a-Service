package tu.faas.domain.models.view;

public class ProductShoppingCartViewModel extends ProductViewModel {
    private Integer quantity;

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
