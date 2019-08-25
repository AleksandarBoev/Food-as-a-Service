package tu.faas.domain.models.binding;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class UserRegisterBindingModel {
    public static final int NAME_MIN_LENGTH = 3;
    public static final int NAME_MAX_LENGTH = 15;
    public static final String NAME_LENGTH_ERROR_MESSAGE =
            "Name length should be between " + NAME_MIN_LENGTH + " and " + NAME_MAX_LENGTH + " letters long!";
    public static final String NAME_FORMAT_ERROR_MESSAGE = "Name should contain only letters and numbers!";

    private String name;
    private String password;
    private String repassword;
    private String email;

    @Size(min = NAME_MIN_LENGTH, max = NAME_MAX_LENGTH, message = NAME_LENGTH_ERROR_MESSAGE)
    @Pattern(regexp = "[A-z0-9]+", message = NAME_FORMAT_ERROR_MESSAGE)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @NotNull(message = "Password can\'t be null!")
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
