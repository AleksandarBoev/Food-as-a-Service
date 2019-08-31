package tu.faas.services.contracts;


import tu.faas.domain.models.view.RestaurantHomeViewModel;

import java.util.List;

public interface RestaurantService {
    List<RestaurantHomeViewModel> getNewestRestaurantHomeViewModels(Integer count);
}
