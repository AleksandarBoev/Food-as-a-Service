package tu.faas.services;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tu.faas.domain.entities.Product;
import tu.faas.domain.models.view.ProductShoppingCartViewModel;
import tu.faas.domain.models.view.ShoppingCartViewModel;
import tu.faas.repositories.ProductRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {
    private ProductRepository productRepository;
    private ModelMapper modelMapper;

    @Autowired
    public OrderServiceImpl(ProductRepository productRepository, ModelMapper modelMapper) {
        this.productRepository = productRepository;
        this.modelMapper = modelMapper;
    }


    @Override
    public ShoppingCartViewModel getShoppingCartViewModel(Map<Long, Integer> productIdCount) {
        ShoppingCartViewModel shoppingCartViewModel = new ShoppingCartViewModel();

        List<Product> products = productRepository.findAllByIdIn(productIdCount.keySet());
        List<ProductShoppingCartViewModel> productShoppingCartViewModels = products
                .stream()
                .map(product -> {
                    ProductShoppingCartViewModel productViewModel =
                            modelMapper.map(product, ProductShoppingCartViewModel.class);

                    productViewModel.setQuantity(productIdCount.get(product.getId()));

                    return productViewModel;
                }).collect(Collectors.toList());

        shoppingCartViewModel.setProducts(productShoppingCartViewModels);

        BigDecimal totalPrice = new BigDecimal("0");
        for (ProductShoppingCartViewModel productViewModel : productShoppingCartViewModels) {
            BigDecimal totalPriceCurrentProduct = productViewModel.getPrice().multiply(new BigDecimal(productViewModel.getQuantity()));
            totalPrice = totalPrice.add(totalPriceCurrentProduct);
        }
        shoppingCartViewModel.setTotalPrice(totalPrice);

        return shoppingCartViewModel;
    }
}
