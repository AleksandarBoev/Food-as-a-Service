package tu.faas.domain.models.binding;

import tu.faas.domain.constants.UserValidationConstants;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class UserRegisterBindingModel {
    private String name;
    private String password;
    private String repassword;
    private String email;

    @Size(min = UserValidationConstants.NAME_MIN_LENGTH,
            max = UserValidationConstants.NAME_MAX_LENGTH,
            message = UserValidationConstants.NAME_LENGTH_ERROR_MESSAGE)
    @Pattern(regexp = UserValidationConstants.USER_NAME_REGEX,
            message = UserValidationConstants.NAME_FORMAT_ERROR_MESSAGE)
    @NotNull(message = UserValidationConstants.NAME_NULL_MESSAGE)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @NotNull(message = "Password can\'t be null!")
    @Size(min = UserValidationConstants.PASSWORD_MIN_LENGTH,
            message = UserValidationConstants.PASSWORD_LENGTH_MESSAGE)
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRepassword() {
        return repassword;
    }

    public void setRepassword(String repassword) {
        this.repassword = repassword;
    }

    @Pattern(regexp = UserValidationConstants.EMAIL_REGEX,
            message = UserValidationConstants.EMAIL_FORMAT_ERROR_MESSAGE)
    @NotNull(message = UserValidationConstants.EMAIL_NULL_MESSAGE)
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "UserRegisterBindingModel{" +
                "name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", repassword='" + repassword + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
