package tu.faas.services.implementations;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tu.faas.domain.lib.Calculation;
import tu.faas.domain.lib.StringManipulation;
import tu.faas.domain.entities.Order;
import tu.faas.domain.entities.Product;
import tu.faas.domain.entities.ProductOrder;
import tu.faas.domain.entities.User;
import tu.faas.domain.exceptions.NoSuchProduct;
import tu.faas.domain.exceptions.NoSuchUser;
import tu.faas.domain.models.binding.OrderBindingModel;
import tu.faas.domain.models.view.OrderHistoryViewModel;
import tu.faas.domain.models.view.ProductOrderUserHistoryViewModel;
import tu.faas.domain.models.view.ProductShoppingCartViewModel;
import tu.faas.domain.models.view.ShoppingCartViewModel;
import tu.faas.repositories.OrderRepository;
import tu.faas.repositories.ProductOrderRepository;
import tu.faas.repositories.ProductRepository;
import tu.faas.repositories.UserRepository;
import tu.faas.services.contracts.OrderService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {
    private static final ZoneId BULGARIA_ZONE_ID = ZoneId.of("Europe/Helsinki");

    private ProductRepository productRepository;
    private ProductOrderRepository productOrderRepository;
    private OrderRepository orderRepository;
    private UserRepository userRepository;
    private ModelMapper modelMapper;

    @Autowired
    public OrderServiceImpl(ProductRepository productRepository, ProductOrderRepository productOrderRepository, OrderRepository orderRepository, UserRepository userRepository, ModelMapper modelMapper) {
        this.productRepository = productRepository;
        this.productOrderRepository = productOrderRepository;
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }


    @Override
    public ShoppingCartViewModel getShoppingCartViewModel(Map<Long, Integer> productIdCount) {
        ShoppingCartViewModel shoppingCartViewModel = new ShoppingCartViewModel();

        List<Product> products = productRepository.findAllByIdIn(productIdCount.keySet());
        List<ProductShoppingCartViewModel> productShoppingCartViewModels = products
                .stream()
                .map(product -> mapProductToShoppingCartViewModel(product, productIdCount.get(product.getId()), modelMapper))
                .collect(Collectors.toList());

        shoppingCartViewModel.setProducts(productShoppingCartViewModels);

        List<BigDecimal> productTotalPrices = productShoppingCartViewModels
                .stream()
                .map(productCartViewModel -> productCartViewModel.getTotalPrice())
                .collect(Collectors.toList());

        shoppingCartViewModel.setTotalSum(Calculation.getSum(productTotalPrices));

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

        order.setDateTimeOfOrder(LocalDateTime.now(BULGARIA_ZONE_ID));

        User customer = userRepository.findById(orderBindingModel.getCustomerId()).orElseThrow(NoSuchUser::new);
        order.setCustomer(customer);

        orderRepository.save(order);

        List<Product> products = productRepository.findAllByIdIn(orderBindingModel.getProductIdQuantities().keySet());
        if (products.size() != orderBindingModel.getProductIdQuantities().size()) {
            throw new NoSuchProduct(NoSuchProduct.PRODUCT_DOES_NOT_EXIST_CHECKOUT);
        }

        List<ProductOrder> productOrders = new ArrayList<>();
        List<BigDecimal> prices = new ArrayList<>();
        for (Product product : products) {
            ProductOrder productOrder = new ProductOrder();
            productOrder.setOrder(order);
            productOrder.setProduct(product);
            Integer quantity = orderBindingModel.getProductIdQuantities().get(product.getId());
            productOrder.setQuantity(quantity);
            productOrder.setProductPriceAtCheckout(product.getPrice());
            productOrder.setDateOfOrder(LocalDate.now(BULGARIA_ZONE_ID));

            productOrders.add(productOrder);

            prices.add(product.getPrice().multiply(new BigDecimal(productOrder.getQuantity())));
        }

        productOrderRepository.saveAll(productOrders);
        BigDecimal totalPrice = Calculation.getSum(prices);
        order.setTotalPrice(totalPrice);
        orderRepository.save(order);
    }

    @Override
    public List<OrderHistoryViewModel> getOrderHistoryViewModelsByUserIdOrderedByDateNewest(Long userId) {
        List<Order> ordersByUser = orderRepository.findAllByCustomerIdOrderByDateTimeOfOrderDesc(userId);
        List<OrderHistoryViewModel> orderHistoryViewModels = mapOrdersToOrderHistoryViewModels(ordersByUser, modelMapper);

        return orderHistoryViewModels;
    }

    private ProductShoppingCartViewModel mapProductToShoppingCartViewModel(Product product, Integer quantity, ModelMapper modelMapper) {
        ProductShoppingCartViewModel productViewModel =
                modelMapper.map(product, ProductShoppingCartViewModel.class);

        String croppedName = StringManipulation.cropString(productViewModel.getName(), 15, "...");
        productViewModel.setName(croppedName);

        String croppedDescription = StringManipulation.cropString(productViewModel.getDescription(), 15, "...");
        productViewModel.setDescription(croppedDescription);

        productViewModel.setQuantity(quantity);
        productViewModel.setTotalPrice(product.getPrice().multiply(BigDecimal.valueOf(productViewModel.getQuantity())));
        return productViewModel;
    }

    //TODO maybe this logic belongs in a ProductOrderService class
    private ProductOrderUserHistoryViewModel mapProductOrderToProductOrderUserHistoryViewModel(
            ProductOrder productOrder,
            ModelMapper modelMapper) {
        ProductOrderUserHistoryViewModel productViewModel =
                modelMapper.map(productOrder, ProductOrderUserHistoryViewModel.class);

        productViewModel.setRestaurantName(productOrder.getProduct().getRestaurant().getName());
        productViewModel.setProductName(productOrder.getProduct().getName());

        return productViewModel;
    }

    private List<ProductOrderUserHistoryViewModel> mapProductOrdersToProductOrderUserHistoryViewModels(
            List<ProductOrder> productOrders,
            ModelMapper modelMapper) {
        return productOrders
                .stream()
                .map(productOrder -> mapProductOrderToProductOrderUserHistoryViewModel(productOrder, modelMapper))
                .collect(Collectors.toList());
    }

    private OrderHistoryViewModel mapOrderToOrderHistoryViewModel(Order order, ModelMapper modelMapper) {
        OrderHistoryViewModel orderHistoryViewModel = modelMapper.map(order, OrderHistoryViewModel.class);

        List<ProductOrderUserHistoryViewModel> productViewModels =
                mapProductOrdersToProductOrderUserHistoryViewModels(order.getProductOrders(), modelMapper);
        orderHistoryViewModel.setProductOrders(productViewModels);

        return orderHistoryViewModel;
    }

    private List<OrderHistoryViewModel> mapOrdersToOrderHistoryViewModels(List<Order> orders, ModelMapper modelMapper) {
        return orders
                .stream()
                .map(order -> mapOrderToOrderHistoryViewModel(order, modelMapper))
                .collect(Collectors.toList());
    }

}
