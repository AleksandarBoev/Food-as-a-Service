package tu.faas.services.implementations;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import tu.faas.domain.beans.Calculation;
import tu.faas.domain.entities.Restaurant;
import tu.faas.domain.entities.User;
import tu.faas.domain.exceptions.NoSuchRestaurant;
import tu.faas.domain.exceptions.NoSuchUser;
import tu.faas.domain.models.binding.RestaurantCreateBindingModel;
import tu.faas.domain.models.multipurpose.RestaurantModel;
import tu.faas.domain.models.view.*;
import tu.faas.repositories.RestaurantRepository;
import tu.faas.repositories.UserRepository;
import tu.faas.services.contracts.ProductOrderService;
import tu.faas.services.contracts.RestaurantService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Service
public class RestaurantServiceImpl implements RestaurantService {
    private RestaurantRepository restaurantRepository;
    private ModelMapper modelMapper;
    private UserRepository userRepository;
    private ProductOrderService productOrderService;
    private Calculation calculation;

    @Autowired
    public RestaurantServiceImpl(RestaurantRepository restaurantRepository,
                                 ModelMapper modelMapper,
                                 UserRepository userRepository,
                                 ProductOrderService productOrderService,
                                 Calculation calculation) {
        this.restaurantRepository = restaurantRepository;
        this.modelMapper = modelMapper;
        this.userRepository = userRepository;
        this.productOrderService = productOrderService;
        this.calculation = calculation;
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

    @Override
    public List<RestaurantListViewModel> getRestaurantsByManager(Long managerId) {
        return restaurantRepository.findAllByManagerId(managerId)
                .stream()
                .map(r -> modelMapper.map(r, RestaurantListViewModel.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<RestaurantAllViewModel> getRestaurantsByManager2(Long managerId) {
        List<Restaurant> restaurants = restaurantRepository.findAllByManagerId(managerId);
        return getRestaurantAllViewModels(restaurants);
    }

    @Override
    public List<RestaurantAllViewModel> getRestaurantAllViewModels(String search, String sortBy) {
        List<Restaurant> restaurants = null;

        if (search == null || "".equals(search)) {
            if (sortBy == null) {
                restaurants = restaurantRepository.findAllByOrderByIdDesc();
            } else if ("nameAsc".equals(sortBy)) {
                restaurants = restaurantRepository.findAllByOrderByNameAsc();
            } else if ("nameDesc".equals(sortBy)) {
                restaurants = restaurantRepository.findAllByOrderByNameDesc();
            }  else if ("newest".equals(sortBy)) {
                restaurants = restaurantRepository.findAllByOrderByIdDesc();
            } else if ("oldest".equals(sortBy)) {
                restaurants = restaurantRepository.findAllByOrderByIdAsc();
            }
        } else {
            if (sortBy == null) {
                restaurants = restaurantRepository.findAllByNameContainsIgnoreCaseOrderByIdDesc(search);
            } else if ("nameAsc".equals(sortBy)) {
                restaurants = restaurantRepository.findAllByNameContainsIgnoreCaseOrderByNameAsc(search);
            } else if ("nameDesc".equals(sortBy)) {
                restaurants = restaurantRepository.findAllByNameContainsIgnoreCaseOrderByNameDesc(search);
            }  else if ("newest".equals(sortBy)) {
                restaurants = restaurantRepository.findAllByNameContainsIgnoreCaseOrderByIdDesc(search);
            } else if ("oldest".equals(sortBy)) {
                restaurants = restaurantRepository.findAllByNameContainsIgnoreCaseOrderByIdAsc(search);
            }
        }
        return getRestaurantAllViewModels(restaurants);
    }

    @Override
    public Long createRestaurant(RestaurantCreateBindingModel bindingModel, Long managerId) {
        Restaurant restaurant = modelMapper.map(bindingModel, Restaurant.class);

        restaurant.setActive(false);
        User manager = userRepository.findById(managerId).orElseThrow(NoSuchUser::new);
        restaurant.setManager(manager);

        restaurantRepository.save(restaurant);
        return restaurant.getId();
    }

    @Override
    public RestaurantModel getRestaurantModelById(Long id) {
        return modelMapper.map(
                restaurantRepository.findById(id).orElseThrow(NoSuchRestaurant::new),
                RestaurantModel.class);
    }

    @Override
    public void editRestaurant(RestaurantModel restaurantModel) {
        Restaurant restaurant = restaurantRepository.findById(restaurantModel.getId())
                .orElseThrow(NoSuchRestaurant::new);
        modelMapper.map(restaurantModel, restaurant);
        restaurantRepository.save(restaurant);
    }

    @Override
    public void deleteRestaurant(Long restaurantId) {
        if (!restaurantRepository.existsById(restaurantId)) {
            throw new NoSuchRestaurant();
        }

        restaurantRepository.deleteById(restaurantId);
    }

    @Override
    public RestaurantViewModel getRestaurantViewModel(Long restaurantId) {
        Restaurant restaurant =
                restaurantRepository.findById(restaurantId).orElseThrow(NoSuchRestaurant::new);

        return modelMapper.map(restaurant, RestaurantViewModel.class);
    }

    @Override
    public RestaurantSalesViewModel getRestaurantSalesViewModel(Long restaurantId, LocalDate dateAfter, LocalDate dateBefore) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId).orElseThrow(NoSuchRestaurant::new);
        RestaurantSalesViewModel restaurantSalesViewModel =
                modelMapper.map(restaurant, RestaurantSalesViewModel.class);

        List<ProductOrderRestaurantSalesViewModel> productOrderViewModels;
        if (dateAfter == null && dateBefore == null) {
            productOrderViewModels = productOrderService
                    .getProductOrdersByRestaurantIdOrderByNewest(restaurantId);
        } else if (dateAfter != null && dateBefore == null) {
            dateAfter = dateAfter.minusDays(1); //entered date is exclusive
            productOrderViewModels = productOrderService
                    .getProductOrdersByRestaurantIdAfterDateOrderByNewest(restaurantId, dateAfter);
        } else if (dateAfter == null && dateBefore != null) {
            dateBefore = dateBefore.plusDays(1);
            productOrderViewModels = productOrderService
                    .getProductOrdersByRestaurantIdBeforeDateOrderByNewest(restaurantId, dateBefore);
        } else {
            productOrderViewModels = productOrderService
                    .getProductOrdersByRestaurantIdBetweenDatesOrderByNewest(restaurantId, dateAfter, dateBefore);
        }

        List<BigDecimal> productOrderPrices = productOrderViewModels
                .stream()
                .map(productOrderViewModel -> productOrderViewModel.getTotalPrice())
                .collect(Collectors.toList());
        BigDecimal restaurantTotalIncome = calculation.getSum(productOrderPrices);

        restaurantSalesViewModel.setProductSaleViewModes(productOrderViewModels);
        restaurantSalesViewModel.setTotalIncome(restaurantTotalIncome);

        return restaurantSalesViewModel;
    }

    @Override
    public Set<String> getRestaurantIdsByManagerId(Long managerId) {
        return restaurantRepository.findAllByManagerId(managerId)
                .stream()
                .map(restaurant -> "" + restaurant.getId())
                .collect(Collectors.toSet());
    }

    private List<RestaurantAllViewModel> getRestaurantAllViewModels(List<Restaurant> restaurants) {
        return restaurants
                .stream()
                .map(restaurant -> {
                    RestaurantAllViewModel restaurantAllViewModel =
                            modelMapper.map(restaurant, RestaurantAllViewModel.class);
                    restaurantAllViewModel.setNumberOfProducts(restaurant.getProducts().size());
                    restaurantAllViewModel.setOwnerId(restaurant.getManager().getId());
                    return restaurantAllViewModel;
                }).collect(Collectors.toList());
    }
}
