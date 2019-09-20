package tu.faas.domain.models.multipurpose;

import tu.faas.domain.constants.UserValidationConstants;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

public class UserEmailModel {
    private String email;
    private String password;

    @Pattern(regexp = UserValidationConstants.EMAIL_REGEX,
            message = UserValidationConstants.EMAIL_FORMAT_ERROR_MESSAGE)
    @NotNull(message = UserValidationConstants.EMAIL_NULL_MESSAGE)
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
