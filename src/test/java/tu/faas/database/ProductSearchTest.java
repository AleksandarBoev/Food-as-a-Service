package tu.faas.database;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;
import tu.faas.domain.entities.Product;
import tu.faas.repositories.ProductRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class ProductSearchTest {
    private static final String NAME1 = "AAA";
    private static final String NAME2 = "BBB";
    private static final String NAME3 = "CCC";
    private static final String NAME4 = "CCC";
    private static final String NAME5 = "CCC";

    private static final BigDecimal PRICE1 = new BigDecimal("1.0");
    private static final BigDecimal PRICE2 = new BigDecimal("2.0");
    private static final BigDecimal PRICE3 = new BigDecimal("3.0");
    private static final BigDecimal PRICE4 = new BigDecimal("4.0");
    private static final BigDecimal PRICE5 = new BigDecimal("5.0");

    @Autowired
    private ProductRepository productRepository;

    @Before
    public void init() {
//        createAndSaveProduct(NAME1, PRICE5);
//        createAndSaveProduct(NAME2, PRICE4);
//        createAndSaveProduct(NAME3, PRICE3);
//        createAndSaveProduct(NAME4, PRICE2);
//        createAndSaveProduct(NAME5, PRICE1);
    }

    @Test
    public void sortByNameAsc() {
//        List<Product> actual = productRepository.findAllByOrderByNameAsc();

    }

    private void createAndSaveProduct(String name, BigDecimal price) {
        Product product = new Product();
        product.setName(name);
        product.setPrice(price);
        productRepository.save(product);
    }

    private List<String> getNames(List<Product> products) {
       return products.stream().map(product -> product.getName()).collect(Collectors.toList());
    }

    private List<BigDecimal> getPrices(List<Product> products) {
        return products.stream().map(product -> product.getPrice()).collect(Collectors.toList());
    }

    private boolean allValuesAreEqual(List<Object> collection1, List<Object> collection2) {
        boolean result = true;
        for (int i = 0; i < collection1.size(); i++) {
            if (!collection1.get(i).equals(collection2.get(i))) {
                result = false;
            }
        }

        return result;
    }
}
