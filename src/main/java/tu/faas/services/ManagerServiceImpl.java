package tu.faas.services;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tu.faas.domain.beans.StringManipulation;
import tu.faas.domain.entities.Product;
import tu.faas.domain.entities.Restaurant;
import tu.faas.domain.entities.User;
import tu.faas.domain.exceptions.NoSuchProduct;
import tu.faas.domain.exceptions.NoSuchRestaurant;
import tu.faas.domain.models.multipurpose.ProductModel;
import tu.faas.domain.models.multipurpose.RestaurantModel;
import tu.faas.domain.models.binding.ProductCreateBindingModel;
import tu.faas.domain.models.binding.RestaurantCreateBindingModel;
import tu.faas.domain.models.view.RestaurantListViewModel;
import tu.faas.domain.models.view.ProductListViewModel;
import tu.faas.domain.models.view.ProductViewModel;
import tu.faas.domain.models.view.RestaurantViewModel;
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
    private StringManipulation stringManipulation;

    @Autowired
    public ManagerServiceImpl(RestaurantRepository restaurantRepository, UserRepository userRepository, ProductRepository productRepository, ModelMapper modelMapper, StringManipulation stringManipulation) {
        this.restaurantRepository = restaurantRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.modelMapper = modelMapper;
        this.stringManipulation = stringManipulation;
    }

    @Override
    public List<RestaurantListViewModel> getRestaurantsByManager(Long managerId) {
        return restaurantRepository.findAllByManagerId(managerId)
                .stream()
                .map(r -> modelMapper.map(r, RestaurantListViewModel.class))
                .collect(Collectors.toList());
    }

    @Override
    public Long createRestaurant(RestaurantCreateBindingModel bindingModel, Long managerId) {
        Restaurant restaurant = modelMapper.map(bindingModel, Restaurant.class);

        restaurant.setActive(false);
        User manager = userRepository.findById(managerId).get();
        restaurant.setManager(manager);

        restaurantRepository.save(restaurant);
        return restaurant.getId();
    }

    @Override
    public RestaurantModel getRestaurantModelById(Long id) {
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
    public List<ProductListViewModel> getProductViewModelsByRestaurantId(Long restaurantId) {
        return productRepository.findAllByRestaurantId(restaurantId)
                .stream()
                .map(p -> {
                    ProductListViewModel result =
                            modelMapper.map(p, ProductListViewModel.class);

                    result.setName(stringManipulation.cropString(result.getName(), 15, "..."));
                    result.setDescription(stringManipulation.cropString(result.getDescription(), 15, "..."));
                    result.setImageUrl(stringManipulation.cropString(result.getImageUrl(), 15, "..."));

                    return result;
                })
                .collect(Collectors.toList());
    }

    @Override
    public ProductModel getProductModel(Long productId) {
        return modelMapper.map(
                productRepository.findById(productId).orElseThrow(NoSuchProduct::new),
                ProductModel.class);
    }

    @Override
    public Long editProduct(ProductModel bindingModel) {
        Product product =
                productRepository.findById(bindingModel.getId()).orElseThrow(NoSuchProduct::new);
        modelMapper.map(bindingModel, product);
        productRepository.save(product);

        Long restaurantId = product.getRestaurant().getId();
        return restaurantId;
    }

    @Override
    public Long deleteProduct(Long productId) {
        Product productToBeDeleted =
                productRepository.findById(productId).orElseThrow(NoSuchProduct::new);
        Long restaurantId = productToBeDeleted.getRestaurant().getId();
        productRepository.delete(productToBeDeleted);
        return restaurantId;
    }

    @Override
    public ProductViewModel getProductViewModel(Long id) {
        Product productFound =
                productRepository.findById(id).orElseThrow(NoSuchProduct::new);

        ProductViewModel result = modelMapper.map(productFound, ProductViewModel.class);
        result.setRestaurantName(productFound.getRestaurant().getName());

        return result;
    }

    @Override
    public RestaurantViewModel getRestaurantViewModel(Long restaurantId) {
        Restaurant restaurant =
                restaurantRepository.findById(restaurantId).orElseThrow(NoSuchRestaurant::new);

        RestaurantViewModel result = modelMapper.map(restaurant, RestaurantViewModel.class);
        List<ProductListViewModel> productListViewModels = restaurant.getProducts()
                .stream()
                .map(p -> {
                    ProductListViewModel productListViewModel = modelMapper.map(p, ProductListViewModel.class);
                    return productListViewModel;
                }).collect(Collectors.toList());

        result.setProductListViewModels(productListViewModels);
        return result;
    }
}
