package tu.faas.domain.models.view;

import java.util.List;

public class RestaurantViewModel {
    private Long id;
    private String name;
    private String imageUrl;
    private String description;
    private Boolean isActive;
    private List<ProductListViewModel> productListViewModels;

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

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public List<ProductListViewModel> getProductListViewModels() {
        return productListViewModels;
    }

    public void setProductListViewModels(List<ProductListViewModel> productListViewModels) {
        this.productListViewModels = productListViewModels;
    }
}
