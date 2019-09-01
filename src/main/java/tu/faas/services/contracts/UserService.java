package tu.faas.services.contracts;

import tu.faas.domain.entities.User;
import tu.faas.domain.models.binding.UserLoginBindingModel;
import tu.faas.domain.models.binding.UserRegisterBindingModel;
import tu.faas.domain.models.view.UserProfileViewModel;

import javax.servlet.http.HttpSession;

public interface UserService {
    void registerUser(UserRegisterBindingModel userRegisterBindingModel);
    void loginUser(UserLoginBindingModel userLoginBindingModel, HttpSession session);

    UserProfileViewModel getUserProfileViewModel(Long userId);
}
