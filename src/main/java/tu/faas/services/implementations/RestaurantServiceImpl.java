package tu.faas.services.implementations;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import tu.faas.domain.entities.Restaurant;
import tu.faas.domain.models.view.RestaurantHomeViewModel;
import tu.faas.repositories.RestaurantRepository;
import tu.faas.services.contracts.RestaurantService;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class RestaurantServiceImpl implements RestaurantService {
    private RestaurantRepository restaurantRepository;
    private ModelMapper modelMapper;

    @Autowired
    public RestaurantServiceImpl(RestaurantRepository restaurantRepository, ModelMapper modelMapper) {
        this.restaurantRepository = restaurantRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<RestaurantHomeViewModel> getNewestRestaurantHomeViewModels(Integer count) {
        List<Restaurant> restaurants = restaurantRepository.findAllByOrderByIdDesc(new PageRequest(0, count));
        return restaurants
                .stream()
                .map(restaurant -> {
                    RestaurantHomeViewModel result = modelMapper.map(restaurant, RestaurantHomeViewModel.class);
                    result.setProductsCount(restaurant.getProducts().size());
                    return result;
                }).collect(Collectors.toList());
    }
}
