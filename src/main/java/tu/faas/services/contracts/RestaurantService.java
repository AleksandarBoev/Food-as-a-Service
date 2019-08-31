package tu.faas.services.contracts;


import tu.faas.domain.models.binding.RestaurantCreateBindingModel;
import tu.faas.domain.models.multipurpose.RestaurantModel;
import tu.faas.domain.models.view.RestaurantHomeViewModel;
import tu.faas.domain.models.view.RestaurantListViewModel;
import tu.faas.domain.models.view.RestaurantViewModel;

import java.util.List;

public interface RestaurantService {
    RestaurantModel getRestaurantModelById(Long id);

    Long createRestaurant(RestaurantCreateBindingModel bindingModel, Long managerId);

    void editRestaurant(RestaurantModel restaurantModel);

    void deleteRestaurant(Long restaurantId);

    RestaurantViewModel getRestaurantViewModel(Long restaurantId);

    List<RestaurantHomeViewModel> getNewestRestaurantHomeViewModels(Integer count);

    List<RestaurantListViewModel> getRestaurantsByManager(Long managerId);
}