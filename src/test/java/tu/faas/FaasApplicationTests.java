package tu.faas;

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



    @Test
    public void printStuff() {
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        UserRegisterBindingModel userRegisterBindingModel = new UserRegisterBindingModel();
        userRegisterBindingModel.setName("aa");
        Set<ConstraintViolation<UserRegisterBindingModel>> errors = validator.validate(userRegisterBindingModel);

        System.out.println("LOOK HERE");
        System.out.println(errors);
    }

}
