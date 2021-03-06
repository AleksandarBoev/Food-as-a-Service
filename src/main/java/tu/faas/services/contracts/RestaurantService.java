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

    List<RestaurantAllViewModel> getRestaurantsByManager(Long managerId, String search, String option);

    List<RestaurantAllViewModel> getRestaurantAllViewModels(String search, String sortBy);

    RestaurantSalesViewModel getRestaurantSalesViewModel(Long restaurantId, LocalDate date1, LocalDate date2);

    Set<Long> getRestaurantIdsByManagerId(Long managerId);

    void deleteRestaurantsByManagerId(Long managerId);
}