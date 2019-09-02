package tu.faas.services.contracts;

import tu.faas.domain.models.binding.UserEditPasswordModel;
import tu.faas.domain.models.binding.UserLoginBindingModel;
import tu.faas.domain.models.binding.UserRegisterBindingModel;
import tu.faas.domain.models.multipurpose.UserEmailModel;
import tu.faas.domain.models.multipurpose.UserNameModel;
import tu.faas.domain.models.view.UserProfileViewModel;

import javax.servlet.http.HttpSession;

public interface UserService {
    void registerUser(UserRegisterBindingModel userRegisterBindingModel);

    void loginUser(UserLoginBindingModel userLoginBindingModel, HttpSession session);

    UserProfileViewModel getUserProfileViewModel(Long userId);

    UserNameModel getUserNameModel(Long userId);

    void editUserName(UserNameModel userNameModel, Long userId);

    UserEmailModel getUserEmailModel(Long userId);

    void editUserEmail(UserEmailModel userEmailModel, Long userId);

    void editPassword(UserEditPasswordModel userEditPasswordModel, Long userId);
}
