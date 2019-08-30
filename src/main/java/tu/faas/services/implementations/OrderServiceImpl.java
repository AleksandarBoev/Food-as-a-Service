package tu.faas.services.implementations;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tu.faas.domain.beans.StringManipulation;
import tu.faas.domain.entities.Order;
import tu.faas.domain.entities.Product;
import tu.faas.domain.entities.ProductOrder;
import tu.faas.domain.entities.User;
import tu.faas.domain.exceptions.NoSuchProduct;
import tu.faas.domain.exceptions.NoSuchUser;
import tu.faas.domain.models.binding.OrderBindingModel;
import tu.faas.domain.models.view.ProductShoppingCartViewModel;
import tu.faas.domain.models.view.ShoppingCartViewModel;
import tu.faas.repositories.OrderRepository;
import tu.faas.repositories.ProductOrderRepository;
import tu.faas.repositories.ProductRepository;
import tu.faas.repositories.UserRepository;
import tu.faas.services.contracts.OrderService;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {
    private ProductRepository productRepository;
    private ProductOrderRepository productOrderRepository;
    private OrderRepository orderRepository;
    private UserRepository userRepository;
    private ModelMapper modelMapper;
    private StringManipulation stringManipulation;

    @Autowired
    public OrderServiceImpl(ProductRepository productRepository, ProductOrderRepository productOrderRepository, OrderRepository orderRepository, UserRepository userRepository, ModelMapper modelMapper, StringManipulation stringManipulation) {
        this.productRepository = productRepository;
        this.productOrderRepository = productOrderRepository;
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
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

    @Override
    public void makeOrder(OrderBindingModel orderBindingModel) {
        /*
        Weird bug: when using modelMapper to create an instance of "Order", it maps
        "customerId" from binding model to "id" from entity. And so instead of creating a new order
        an old one gets updated.
         */
        Order order = new Order();
        order.setBillingType(orderBindingModel.getBillingType());
        order.setAddress(orderBindingModel.getAddress());

        order.setDateTimeOfOrder(LocalDateTime.now());

        User customer = userRepository.findById(orderBindingModel.getCustomerId()).orElseThrow(NoSuchUser::new);
        order.setCustomer(customer);

        orderRepository.save(order);

        List<Product> products = productRepository.findAllByIdIn(orderBindingModel.getProductIdQuantities().keySet());
        if (products.size() != orderBindingModel.getProductIdQuantities().size()) {
            throw new NoSuchProduct("One of the products does not exist anymore! We apologise for the inconvenience.");
        }

        List<ProductOrder> productOrders = new ArrayList<>();
        List<BigDecimal> prices = new ArrayList<>();
        for (Product product : products) {
            ProductOrder productOrder = new ProductOrder();
            productOrder.setOrder(order);
            productOrder.setProduct(product);
            Integer quantity = orderBindingModel.getProductIdQuantities().get(product.getId());
            productOrder.setQuantity(quantity);
            productOrders.add(productOrder);

            prices.add(product.getPrice().multiply(new BigDecimal(productOrder.getQuantity())));
        }

        productOrderRepository.saveAll(productOrders);
        BigDecimal totalPrice = getSum(prices);
        order.setTotalPrice(totalPrice);
        orderRepository.save(order);
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
