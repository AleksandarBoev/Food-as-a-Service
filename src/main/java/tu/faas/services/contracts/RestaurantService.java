package tu.faas.services.contracts;


import tu.faas.domain.models.binding.RestaurantCreateBindingModel;
import tu.faas.domain.models.multipurpose.RestaurantModel;
import tu.faas.domain.models.view.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public interface RestaurantService {
    RestaurantModel getRestaurantModelById(Long id);

    Long createRestaurant(RestaurantCreateBindingModel bindingModel, Long managerId);

    void editRestaurant(RestaurantModel restaurantModel);

    void deleteRestaurant(Long restaurantId);

    RestaurantViewModel getRestaurantViewModel(Long restaurantId);

    List<RestaurantHomeViewModel> getNewestRestaurantHomeViewModels(Integer count);

    List<RestaurantListViewModel> getRestaurantsByManager(Long managerId);

    List<RestaurantAllViewModel> getRestaurantAllViewModels(String search, String sortBy);

    List<RestaurantAllViewModel> getRestaurantsByManager2(Long managerId);

    RestaurantSalesViewModel getRestaurantSalesViewModel(Long restaurantId, LocalDate date1, LocalDate date2);

    Set<String> getRestaurantIdsByManagerId(Long managerId);
}