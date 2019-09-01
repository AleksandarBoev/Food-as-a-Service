package tu.faas.domain.models.multipurpose;

import tu.faas.domain.constants.UserValidationConstants;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class UserNameModel {
    private String name;
    private String password;

    @Size(min = UserValidationConstants.NAME_MIN_LENGTH,
            max = UserValidationConstants.NAME_MAX_LENGTH,
            message = UserValidationConstants.NAME_LENGTH_ERROR_MESSAGE)
    @Pattern(regexp = UserValidationConstants.USER_NAME_REGEX,
            message = UserValidationConstants.NAME_FORMAT_ERROR_MESSAGE)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
