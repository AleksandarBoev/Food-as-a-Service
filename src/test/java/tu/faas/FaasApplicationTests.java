package tu.faas;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.hibernate.validator.internal.engine.ConstraintViolationImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import tu.faas.domain.entities.User;
import tu.faas.domain.models.binding.UserRegisterBindingModel;
import tu.faas.domain.models.view.ProductShoppingCartViewModel;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.math.BigDecimal;
import java.util.*;

@RunWith(SpringRunner.class)
public class FaasApplicationTests {

    class SomeClass {
        private Long productId;

        public Long getProductId() {
            return productId;
        }

        public void setProductId(Long productId) {
            this.productId = productId;
        }
    }

    @Test
    public void printStuff() {
        List<ProductShoppingCartViewModel> productShoppingCartViewModels = new ArrayList<>();

        ProductShoppingCartViewModel wat1 = new ProductShoppingCartViewModel();
        wat1.setQuantity(2);
        wat1.setPrice(new BigDecimal("10"));
        productShoppingCartViewModels.add(wat1);

        ProductShoppingCartViewModel wat2 = new ProductShoppingCartViewModel();
        wat2.setQuantity(3);
        wat2.setPrice(new BigDecimal("3"));
        productShoppingCartViewModels.add(wat2);

        BigDecimal totalPrice = new BigDecimal("0");
        for (ProductShoppingCartViewModel productViewModel : productShoppingCartViewModels) {
            BigDecimal totalPriceCurrentProduct = productViewModel.getPrice().multiply(new BigDecimal(productViewModel.getQuantity()));
            totalPrice = totalPrice.add(totalPriceCurrentProduct);
        }
        System.out.println(totalPrice);
    }

}
