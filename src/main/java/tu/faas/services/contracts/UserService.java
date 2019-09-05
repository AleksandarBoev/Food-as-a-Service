package tu.faas.services.contracts;

import tu.faas.domain.models.binding.AdminAction;
import tu.faas.domain.models.binding.UserEditPasswordModel;
import tu.faas.domain.models.binding.UserLoginBindingModel;
import tu.faas.domain.models.binding.UserRegisterBindingModel;
import tu.faas.domain.models.multipurpose.UserEmailModel;
import tu.faas.domain.models.multipurpose.UserNameModel;
import tu.faas.domain.models.view.UserProfileViewModel;
import tu.faas.domain.models.view.UserUsersViewModel;
import tu.faas.web.session.UserData;

import javax.servlet.http.HttpSession;
import java.util.List;

public interface UserService {
    void registerUser(UserRegisterBindingModel userRegisterBindingModel);

    UserData loginUser(UserLoginBindingModel userLoginBindingModel);

    UserProfileViewModel getUserProfileViewModel(Long userId);

    UserNameModel getUserNameModel(Long userId);

    void editUserName(UserNameModel userNameModel, Long userId);

    UserEmailModel getUserEmailModel(Long userId);

    void editUserEmail(UserEmailModel userEmailModel, Long userId);

    void editPassword(UserEditPasswordModel userEditPasswordModel, Long userId);

    List<UserUsersViewModel> getUserViewModelsWithoutAdmin(Long adminId, String search, String option);

    Boolean validCredentials(Long userId, String userPassword);

    void updateUser(AdminAction adminAction, Long adminId);
}
