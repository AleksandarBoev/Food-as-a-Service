package tu.faas.services.contracts;

import tu.faas.domain.models.binding.UserLoginBindingModel;
import tu.faas.domain.models.binding.UserRegisterBindingModel;
import tu.faas.domain.models.view.ProductHomeViewModel;
import tu.faas.domain.models.view.RestaurantHomeViewModel;
import tu.faas.web.session.UserData;

import javax.servlet.http.HttpSession;
import java.util.List;

public interface HomeService {
    List<ProductHomeViewModel> getNewestProductHomeViewModels(Integer count);

    List<RestaurantHomeViewModel> getNewestRestaurantHomeViewModels(Integer count);

    void registerUser(UserRegisterBindingModel userRegisterBindingModel);

    UserData loginUser(UserLoginBindingModel userLoginBindingModel);
}
