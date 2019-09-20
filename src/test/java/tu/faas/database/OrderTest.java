package tu.faas.database;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import tu.faas.domain.entities.*;
import tu.faas.repositories.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class OrderTest {
    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductOrderRepository productOrderRepository;

    @Autowired
    private OrderRepository orderRepository;

    /*
    @OneToMany collections are not working properly in the in-memory database testing.
    So creating the collections, filling them and setting them to the respective
    records is needed.

    This init method shows the database workflow.
     */
    @Before
    public void init() {
        //Creating roles (project startup)
        Role role = new Role();
        role.setName("ROLE_USER");
        roleRepository.save(role);

        //Creating user and adding a role to it (registering)
        User user = new User();
        Set<Role> roles = new HashSet<>();
        roles.add(role);
        user.setRoles(roles);
        user.setName("test");
        user.setEmail("test");
        user.setPassword("test");
        userRepository.save(user);

        //Creating restaurant and adding a manager to it (manager creates a restaurant)
        Restaurant restaurant = new Restaurant();
        restaurant.setName("Restaurant");
        restaurant.setManager(user);
        restaurantRepository.save(restaurant);

        //Creating a product and setting a restaurant to it (manager creates product)
        Product product = new Product();
        product.setName("product");
        product.setPrice(new BigDecimal("5.00"));
        product.setWeightInGrams(10);
        product.setRestaurant(restaurant);
        productRepository.save(product);

        //Adding product to restaurant (a product comes from a restaurant)
        List<Product> products = new ArrayList<>();
        products.add(product);
        restaurant.setProducts(products);

        //Creating a product order and setting a product to it (a productOrder is made
        //for each product a customer has in shopping cart when doing checkout)
        ProductOrder productOrder = new ProductOrder();
        productOrder.setProduct(product);
        productOrder.setQuantity(2);
        productOrderRepository.save(productOrder);

        //Creating an order. Setting a customer to it and adding a product order.
        //(an order contains all information about a single order. A single order has many orderProducts)
        Order order = new Order();
        order.setDateTimeOfOrder(LocalDateTime.of(2019, 8, 30, 12, 50));
        order.setCustomer(user);
        orderRepository.save(order);

        //Setting order of productOrder (a productOrder is always a part of an order)
        productOrder.setOrder(order);

        List<ProductOrder> productOrders = new ArrayList<>();
        productOrders.add(productOrder);
        order.setProductOrders(productOrders);
    }

    @Test
    public void testing() {

    }
}
