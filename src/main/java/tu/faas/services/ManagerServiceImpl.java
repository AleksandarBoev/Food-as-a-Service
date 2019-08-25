package tu.faas.services;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tu.faas.domain.entities.Restaurant;
import tu.faas.domain.entities.User;
import tu.faas.domain.models.binding.RestaurantCreateBindingModel;
import tu.faas.domain.models.view.ManagerRestaurantsViewModel;
import tu.faas.repositories.RestaurantRepository;
import tu.faas.repositories.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ManagerServiceImpl implements ManagerService {
    private RestaurantRepository restaurantRepository;
    private UserRepository userRepository;
    private ModelMapper modelMapper;

    @Autowired
    public ManagerServiceImpl(RestaurantRepository restaurantRepository, UserRepository userRepository, ModelMapper modelMapper) {
        this.restaurantRepository = restaurantRepository;
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<ManagerRestaurantsViewModel> getRestaurantsByManager(Long managerId) {
        return restaurantRepository.findAllByManagerId(managerId)
                .stream()
                .map(r -> modelMapper.map(r, ManagerRestaurantsViewModel.class))
                .collect(Collectors.toList());
    }

    @Override
    public void createRestaurant(RestaurantCreateBindingModel bindingModel, Long managerId) {
        Restaurant restaurant = modelMapper.map(bindingModel, Restaurant.class);

        restaurant.setActive(false);
        User manager = userRepository.findById(managerId).get();
        restaurant.setManager(manager);

        restaurantRepository.save(restaurant);
    }
}
