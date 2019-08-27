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

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;

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
        Gson gson = new GsonBuilder().create();
        SomeClass wat = gson.fromJson("{'productId':'10'}", SomeClass.class);
        System.out.println(wat.getProductId());
    }

}
