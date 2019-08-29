package tu.faas.services;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tu.faas.domain.beans.StringManipulation;
import tu.faas.domain.entities.Product;
import tu.faas.domain.models.view.ProductShoppingCartViewModel;
import tu.faas.domain.models.view.ShoppingCartViewModel;
import tu.faas.repositories.ProductRepository;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {
    private ProductRepository productRepository;
    private ModelMapper modelMapper;
    private StringManipulation stringManipulation;

    @Autowired
    public OrderServiceImpl(ProductRepository productRepository, ModelMapper modelMapper, StringManipulation stringManipulation) {
        this.productRepository = productRepository;
        this.modelMapper = modelMapper;
        this.stringManipulation = stringManipulation;
    }


    @Override
    public ShoppingCartViewModel getShoppingCartViewModel(Map<Long, Integer> productIdCount) {
        ShoppingCartViewModel shoppingCartViewModel = new ShoppingCartViewModel();

        List<Product> products = productRepository.findAllByIdIn(productIdCount.keySet());
        List<ProductShoppingCartViewModel> productShoppingCartViewModels = products
                .stream()
                .map(product -> convert(product, productIdCount.get(product.getId()), modelMapper))
                .collect(Collectors.toList());

        shoppingCartViewModel.setProducts(productShoppingCartViewModels);

        List<BigDecimal> productTotalPrices = productShoppingCartViewModels
                .stream()
                .map(productCartViewModel -> productCartViewModel.getTotalPrice())
                .collect(Collectors.toList());

        shoppingCartViewModel.setTotalSum(getSum(productTotalPrices));

        return shoppingCartViewModel;
    }

    private ProductShoppingCartViewModel convert(Product product, Integer quantity, ModelMapper modelMapper) {
        ProductShoppingCartViewModel productViewModel =
                modelMapper.map(product, ProductShoppingCartViewModel.class);

        String croppedName = stringManipulation.cropString(productViewModel.getName(), 15, "...");
        productViewModel.setName(croppedName);

        String croppedDescription = stringManipulation.cropString(productViewModel.getDescription(), 15, "...");
        productViewModel.setDescription(croppedDescription);

        productViewModel.setQuantity(quantity);
        productViewModel.setTotalPrice(product.getPrice().multiply(BigDecimal.valueOf(productViewModel.getQuantity())));
        return productViewModel;
    }

    private BigDecimal getSum(Collection<BigDecimal> bigDecimalCollection) {
        BigDecimal totalSum = new BigDecimal("0");
        for (BigDecimal current : bigDecimalCollection) {
            totalSum = totalSum.add(current);
        }

        return totalSum;
    }
}
