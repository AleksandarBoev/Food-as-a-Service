package tu.faas.services;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tu.faas.domain.entities.Product;
import tu.faas.domain.entities.Restaurant;
import tu.faas.domain.entities.User;
import tu.faas.domain.exceptions.NoSuchProduct;
import tu.faas.domain.exceptions.NoSuchRestaurant;
import tu.faas.domain.models.multipurpose.ProductEditModel;
import tu.faas.domain.models.multipurpose.RestaurantModel;
import tu.faas.domain.models.binding.ProductCreateBindingModel;
import tu.faas.domain.models.binding.RestaurantCreateBindingModel;
import tu.faas.domain.models.view.ManagerRestaurantsViewModel;
import tu.faas.domain.models.view.ProductEditRestaurantsViewModel;
import tu.faas.repositories.ProductRepository;
import tu.faas.repositories.RestaurantRepository;
import tu.faas.repositories.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ManagerServiceImpl implements ManagerService {
    //TODO to many repositories in one Service. Maybe split them up
    //in many services and this service will use the services.
    private RestaurantRepository restaurantRepository;
    private UserRepository userRepository;
    private ProductRepository productRepository;
    private ModelMapper modelMapper;

    @Autowired
    public ManagerServiceImpl(RestaurantRepository restaurantRepository, UserRepository userRepository, ProductRepository productRepository, ModelMapper modelMapper) {
        this.restaurantRepository = restaurantRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
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

    @Override
    public RestaurantModel getRestaurantById(Long id) {
        //TODO test this logic
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
    public void createProduct(
            ProductCreateBindingModel productCreateBindingModel,
            Long restaurantId) {
        Product product = modelMapper.map(productCreateBindingModel, Product.class);
        product.setRestaurant(restaurantRepository.findById(restaurantId).orElseThrow(NoSuchRestaurant::new));
        productRepository.save(product);
    }

    @Override
    public List<ProductEditRestaurantsViewModel> getProductViewModels(Long restaurantId) {
        return productRepository.findAllByRestaurantId(restaurantId)
                .stream()
                .map(p -> {
                    ProductEditRestaurantsViewModel result =
                            modelMapper.map(p, ProductEditRestaurantsViewModel.class);

                    result.setName(cropString(result.getName(), 15, "..."));
                    result.setDescription(cropString(result.getDescription(), 15, "..."));
                    result.setImageUrl(cropString(result.getImageUrl(), 15, "..."));

                    return result;
                })
                .collect(Collectors.toList());
    }

    @Override
    public ProductEditModel getProductEditModel(Long productId) {
        return modelMapper.map(
                productRepository.findById(productId).orElseThrow(NoSuchProduct::new),
                ProductEditModel.class);
    }

    @Override
    public void editProduct(ProductEditModel bindingModel) {
        Product product =
                productRepository.findById(bindingModel.getId()).orElseThrow(NoSuchProduct::new);
        modelMapper.map(bindingModel, product);
        productRepository.save(product);
    }

    private String cropString(String string, int maxLength, String endingReplacement) {
        if (string.length() > maxLength) {
            string = string.substring(0, maxLength + 1);
            string += endingReplacement;
        }

        return string;
    }
}
