package tu.faas.services;

import tu.faas.domain.models.multipurpose.ProductEditModel;
import tu.faas.domain.models.multipurpose.RestaurantModel;
import tu.faas.domain.models.binding.ProductCreateBindingModel;
import tu.faas.domain.models.binding.RestaurantCreateBindingModel;
import tu.faas.domain.models.view.ManagerRestaurantsViewModel;
import tu.faas.domain.models.view.ProductEditRestaurantsViewModel;

import java.util.List;

public interface ManagerService {
    List<ManagerRestaurantsViewModel> getRestaurantsByManager(Long managerId);
    void createRestaurant(RestaurantCreateBindingModel bindingModel, Long managerId);
    RestaurantModel getRestaurantById(Long id);
    void editRestaurant(RestaurantModel restaurantModel);
    void deleteRestaurant(Long restaurantId);
    void createProduct(ProductCreateBindingModel productCreateBindingModel, Long restaurantId);
    List<ProductEditRestaurantsViewModel> getProductViewModels(Long restaurantId);
    ProductEditModel getProductEditModel(Long productId);

    void editProduct(ProductEditModel bindingModel);
}
