package tu.faas.services.contracts;

import tu.faas.domain.models.binding.ProductCreateBindingModel;
import tu.faas.domain.models.multipurpose.ProductModel;
import tu.faas.domain.models.view.ProductAllViewModel;
import tu.faas.domain.models.view.ProductHomeViewModel;
import tu.faas.domain.models.view.ProductListViewModel;
import tu.faas.domain.models.view.ProductViewModel;

import java.math.BigDecimal;
import java.util.List;

public interface ProductService {
    ProductModel getProductModel(Long productId);

    void createProduct(ProductCreateBindingModel productCreateBindingModel, Long restaurantId);

    Long editProduct(ProductModel bindingModel);

    Long deleteProduct(Long productId);

    ProductViewModel getProductViewModel(Long id);

    List<ProductListViewModel> getProductViewModelsByRestaurantId(Long restaurantId);

    List<ProductHomeViewModel> getNewestProductHomeViewModels(Integer count);

    List<ProductAllViewModel> getProductAllViewModels(String search, String sortBy);

    List<ProductAllViewModel> getProductAllViewModelsByRestaurantId(Long restaurantId);

    }
