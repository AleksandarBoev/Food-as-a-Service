package tu.faas.services;

import tu.faas.domain.models.binding.RestaurantCreateBindingModel;
import tu.faas.domain.models.view.ManagerRestaurantsViewModel;

import java.util.List;

public interface ManagerService {
    List<ManagerRestaurantsViewModel> getRestaurantsByManager(Long managerId);
    void createRestaurant(RestaurantCreateBindingModel bindingModel, Long managerId);
}
