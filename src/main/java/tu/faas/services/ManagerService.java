package tu.faas.services;

import tu.faas.domain.models.multipurpose.ProductModel;
import tu.faas.domain.models.multipurpose.RestaurantModel;
import tu.faas.domain.models.binding.ProductCreateBindingModel;
import tu.faas.domain.models.binding.RestaurantCreateBindingModel;
import tu.faas.domain.models.view.RestaurantListViewModel;
import tu.faas.domain.models.view.ProductListViewModel;
import tu.faas.domain.models.view.ProductViewModel;
import tu.faas.domain.models.view.RestaurantViewModel;

import java.util.List;

public interface ManagerService {
    List<RestaurantListViewModel> getRestaurantsByManager(Long managerId);

    /**
     *
     * @param bindingModel
     * @param managerId
     * @return generated id of newly created restaurant
     */
    Long createRestaurant(RestaurantCreateBindingModel bindingModel, Long managerId);
    RestaurantModel getRestaurantModelById(Long id);
    void editRestaurant(RestaurantModel restaurantModel);
    void deleteRestaurant(Long restaurantId);
    void createProduct(ProductCreateBindingModel productCreateBindingModel, Long restaurantId);
    List<ProductListViewModel> getProductViewModelsByRestaurantId(Long restaurantId);
    ProductModel getProductModel(Long productId);

    Long editProduct(ProductModel bindingModel);

    /**
     *
     * @param productId
     * @return id of restaurant to which product is...
     */
    Long deleteProduct(Long productId);

    ProductViewModel getProductViewModel(Long id);

    RestaurantViewModel getRestaurantViewModel(Long restaurantId);
}
