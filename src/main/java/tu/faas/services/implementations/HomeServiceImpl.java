package tu.faas.services.implementations;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tu.faas.domain.models.binding.UserLoginBindingModel;
import tu.faas.domain.models.binding.UserRegisterBindingModel;
import tu.faas.domain.models.view.ProductHomeViewModel;
import tu.faas.domain.models.view.RestaurantHomeViewModel;
import tu.faas.services.contracts.HomeService;
import tu.faas.services.contracts.ProductService;
import tu.faas.services.contracts.RestaurantService;
import tu.faas.services.contracts.UserService;

import javax.servlet.http.HttpSession;
import java.util.List;

@Service
public class HomeServiceImpl implements HomeService {
    private ProductService productService;
    private RestaurantService restaurantService;
    private UserService userService;
    private ModelMapper modelMapper;

    @Autowired
    public HomeServiceImpl(ProductService productService, RestaurantService restaurantService, UserService userService, ModelMapper modelMapper) {
        this.productService = productService;
        this.restaurantService = restaurantService;
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<ProductHomeViewModel> getNewestProductHomeViewModels(Integer count) {
        return productService.getNewestProductHomeViewModels(count);
    }

    @Override
    public List<RestaurantHomeViewModel> getNewestRestaurantHomeViewModels(Integer count) {
        return restaurantService.getNewestRestaurantHomeViewModels(count);
    }

    @Override
    public void registerUser(UserRegisterBindingModel userRegisterBindingModel) {
        userService.registerUser(userRegisterBindingModel);
    }

    @Override
    public void loginUser(UserLoginBindingModel userLoginBindingModel, HttpSession session) {
        userService.loginUser(userLoginBindingModel, session);
    }
}
