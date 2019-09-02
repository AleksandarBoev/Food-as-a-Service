package tu.faas.services.implementations;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tu.faas.domain.entities.ProductOrder;
import tu.faas.domain.models.view.ProductOrderRestaurantSalesViewModel;
import tu.faas.repositories.ProductOrderRepository;
import tu.faas.services.contracts.ProductOrderService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductOrderServiceImpl implements ProductOrderService {
    private ProductOrderRepository productOrderRepository;
    private ModelMapper modelMapper;

    @Autowired
    public ProductOrderServiceImpl(ProductOrderRepository productOrderRepository, ModelMapper modelMapper) {
        this.productOrderRepository = productOrderRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<ProductOrderRestaurantSalesViewModel> getProductOrdersByRestaurantIdOrderByNewest(Long restaurantId) {
        List<ProductOrder> productOrders = productOrderRepository
                .findAllByProductRestaurantIdOrderByDateOfOrderDesc(restaurantId);
        return mapToSalesViewModels(productOrders);
    }

    @Override
    public List<ProductOrderRestaurantSalesViewModel> getProductOrdersByRestaurantIdAfterDateOrderByNewest(Long restaurantId, LocalDate afterDate) {
        return mapToSalesViewModels(productOrderRepository
                .findAllByProductRestaurantIdAndDateOfOrderAfterOrderByDateOfOrderDesc(restaurantId, afterDate));
    }

    @Override
    public List<ProductOrderRestaurantSalesViewModel> getProductOrdersByRestaurantIdBeforeDateOrderByNewest(Long restaurantId, LocalDate date) {
        return mapToSalesViewModels(productOrderRepository
                .findAllByProductRestaurantIdAndDateOfOrderBeforeOrderByDateOfOrderDesc(restaurantId, date));
    }

    @Override
    public List<ProductOrderRestaurantSalesViewModel> getProductOrdersByRestaurantIdBetweenDatesOrderByNewest(Long restaurantId, LocalDate date1, LocalDate date2) {
        return mapToSalesViewModels(productOrderRepository
                .findAllByProductRestaurantIdAndDateOfOrderBetweenOrderByDateOfOrderDesc(restaurantId, date1, date2));
    }

    private ProductOrderRestaurantSalesViewModel mapToSalesViewModel(ProductOrder productOrder) {
        ProductOrderRestaurantSalesViewModel result =
                modelMapper.map(productOrder, ProductOrderRestaurantSalesViewModel.class);

        result.setProductName(productOrder.getProduct().getName());
        result.setTotalPrice(result.getProductPriceAtCheckout().multiply(new BigDecimal(result.getQuantity())));

        return result;
    }

    private List<ProductOrderRestaurantSalesViewModel> mapToSalesViewModels(Collection<ProductOrder> productOrders) {
        return productOrders
                .stream()
                .map(productOrder -> mapToSalesViewModel(productOrder))
                .collect(Collectors.toList());
    }
}
