package tu.faas.validations;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;
import tu.faas.domain.constants.UserValidationConstants;
import tu.faas.domain.models.binding.UserRegisterBindingModel;

@RunWith(SpringRunner.class)
public class UserValidation extends BaseValidation {
    private static final String VALID_USER_NAME = "Aleksandar -95"; //14 symbols
    private static final String VALID_PASSWORD= "blabla123";
    private static final String VALID_EMAIL = "aleksandarboev95@gmail.com";

    private UserRegisterBindingModel userRegisterBindingModel;

    public UserValidation() {
        super();
    }

    @Before
    public void init() {
        userRegisterBindingModel = new UserRegisterBindingModel();
        userRegisterBindingModel.setName(VALID_USER_NAME);
        userRegisterBindingModel.setEmail(VALID_EMAIL);
        userRegisterBindingModel.setPassword(VALID_PASSWORD);
        userRegisterBindingModel.setRepassword(VALID_PASSWORD);
    }

    @Test
    public void nameField_whenEnteredShortName_correctErrorMessage() {
        userRegisterBindingModel.setName("A");
        super.assertOnlyOneCorrectErrorMessageThrown(
                userRegisterBindingModel, UserValidationConstants.NAME_LENGTH_ERROR_MESSAGE);
    }

    @Test
    public void nameField_whenEnteredLongName_correctErrorMEssage() {

    }
}
