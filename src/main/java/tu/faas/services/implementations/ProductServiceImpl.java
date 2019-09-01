package tu.faas.services.implementations;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import tu.faas.domain.beans.StringManipulation;
import tu.faas.domain.entities.Product;
import tu.faas.domain.exceptions.NoSuchProduct;
import tu.faas.domain.exceptions.NoSuchRestaurant;
import tu.faas.domain.models.binding.ProductCreateBindingModel;
import tu.faas.domain.models.multipurpose.ProductModel;
import tu.faas.domain.models.view.ProductAllViewModel;
import tu.faas.domain.models.view.ProductHomeViewModel;
import tu.faas.domain.models.view.ProductListViewModel;
import tu.faas.domain.models.view.ProductViewModel;
import tu.faas.repositories.ProductRepository;
import tu.faas.repositories.RestaurantRepository;
import tu.faas.services.contracts.ProductService;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {
    private ProductRepository productRepository;
    private ModelMapper modelMapper;
    private RestaurantRepository restaurantRepository;
    private StringManipulation stringManipulation;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository, ModelMapper modelMapper, RestaurantRepository restaurantRepository, StringManipulation stringManipulation) {
        this.productRepository = productRepository;
        this.modelMapper = modelMapper;
        this.restaurantRepository = restaurantRepository;
        this.stringManipulation = stringManipulation;
    }

    @Override
    public List<ProductHomeViewModel> getNewestProductHomeViewModels(Integer count) {
        List<Product> products = productRepository.findAllByOrderByIdDesc(new PageRequest(0, count));
        return products
                .stream()
                .map(product -> {
                    ProductHomeViewModel result = modelMapper.map(product, ProductHomeViewModel.class);
                    result.setRestaurantName(product.getRestaurant().getName());
                    return result;
                }).collect(Collectors.toList());
    }

    @Override
    public List<ProductAllViewModel> getProductAllViewModels(String search, String sortBy) {
        List<Product> products = null;

        if (search == null || "".equals(search)) {
            if (sortBy == null) {
                products = productRepository.findAllByOrderByIdDesc();
            } else if ("nameAsc".equals(sortBy)) {
                products = productRepository.findAllByOrderByNameAsc();
            } else if ("nameDesc".equals(sortBy)) {
                products = productRepository.findAllByOrderByNameDesc();
            } else if ("priceAsc".equals(sortBy)) {
                products = productRepository.findAllByOrderByPriceAsc();
            } else if ("priceDesc".equals(sortBy)) {
                products = productRepository.findAllByOrderByPriceDesc();
            } else if ("newest".equals(sortBy)) {
                products = productRepository.findAllByOrderByIdDesc();
            } else if ("oldest".equals(sortBy)) {
                products = productRepository.findAllByOrderByIdAsc();
            }
        } else {
            if (sortBy == null) {
                products = productRepository.findAllByNameContainsIgnoreCase(search);
            } else if ("nameAsc".equals(sortBy)) {
                products = productRepository.findAllByNameContainsIgnoreCaseOrderByNameAsc(search);
            } else if ("nameDesc".equals(sortBy)) {
                products = productRepository.findAllByNameContainsIgnoreCaseOrderByNameDesc(search);
            } else if ("priceAsc".equals(sortBy)) {
                products = productRepository.findAllByNameContainsIgnoreCaseOrderByPriceAsc(search);
            } else if ("priceDesc".equals(sortBy)) {
                products = productRepository.findAllByNameContainsIgnoreCaseOrderByPriceDesc(search);
            } else if ("newest".equals(sortBy)) {
                products = productRepository.findAllByNameContainsIgnoreCaseOrderByIdDesc(search);
            } else if ("oldest".equals(sortBy)) {
                products = productRepository.findAllByNameContainsIgnoreCaseOrderByIdAsc(search);
            }
        }

        return products.stream().map(product -> {
            ProductAllViewModel productAllViewModel =
                    modelMapper.map(product, ProductAllViewModel.class);
            productAllViewModel.setOwnerId(product.getRestaurant().getManager().getId());
            return productAllViewModel;
        }).collect(Collectors.toList());
    }

    @Override
    public void createProduct(ProductCreateBindingModel productCreateBindingModel,
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
}
