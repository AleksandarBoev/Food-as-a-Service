package tu.faas.domain.models.binding;

import tu.faas.domain.constants.UserValidationConstants;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class UserEditPasswordModel {
    private String oldPassword;
    private String newPassword;
    private String rePassword;

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    @NotNull(message = "Password can\'t be null!")
    @Size(min = UserValidationConstants.PASSWORD_MIN_LENGTH,
            message = UserValidationConstants.PASSWORD_LENGTH_MESSAGE)
    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getRePassword() {
        return rePassword;
    }

    public void setRePassword(String rePassword) {
        this.rePassword = rePassword;
    }
}
