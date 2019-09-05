package tu.faas.web.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import tu.faas.domain.exceptions.EmailAlreadyExists;
import tu.faas.domain.exceptions.NoSuchUser;
import tu.faas.domain.exceptions.PasswordsNotSame;
import tu.faas.domain.exceptions.UsernameAlreadyExists;
import tu.faas.domain.models.binding.UserLoginBindingModel;
import tu.faas.domain.models.binding.UserRegisterBindingModel;
import tu.faas.domain.models.view.ProductHomeViewModel;
import tu.faas.domain.models.view.RestaurantHomeViewModel;
import tu.faas.services.contracts.HomeService;
import tu.faas.web.session.UserData;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/")
public class HomeController {
    private HomeService homeService;

    @Autowired
    public HomeController(HomeService homeService) {
        this.homeService = homeService;
    }

    @GetMapping
    public ModelAndView getHomePage(ModelAndView modelAndView) {
        List<ProductHomeViewModel> productHomeViewModels =
                homeService.getNewestProductHomeViewModels(5);

        modelAndView.addObject("productHomeViewModels", productHomeViewModels);

        List<RestaurantHomeViewModel> restaurantHomeViewModels =
                homeService.getNewestRestaurantHomeViewModels(3);
        modelAndView.addObject("restaurantHomeViewModels", restaurantHomeViewModels);

        modelAndView.setViewName("home/index.html");
        return modelAndView;
    }

    @GetMapping("/register")
    public ModelAndView getRegisterPage(ModelAndView modelAndView, @ModelAttribute("errorMessage") String errorMessage) {
        modelAndView.setViewName("home/register.html");
        modelAndView.addObject("errorMessage", errorMessage);
        return modelAndView;
    }

    @PostMapping("/register")
    public ModelAndView postRegisterPage(ModelAndView modelAndView,
                                         @Valid @ModelAttribute("userRegisterBindingModel") UserRegisterBindingModel userRegisterBindingModel,
                                         BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            modelAndView.setViewName("home/register.html");
            modelAndView.addObject("userRegisterBindingModel", userRegisterBindingModel);
            return modelAndView;
        }

        try {
            homeService.registerUser(userRegisterBindingModel);
            modelAndView.setViewName("redirect:/login");
        } catch (PasswordsNotSame pns) {
            modelAndView.setViewName("home/register.html");
            FieldError fieldError =
                    new FieldError("userRegisterBindingModel", "repassword", pns.getMessage());
            bindingResult.addError(fieldError);
        } catch (UsernameAlreadyExists uae) {
            modelAndView.setViewName("home/register.html");
            FieldError fieldError =
                    new FieldError("userRegisterBindingModel", "name", uae.getMessage());
            bindingResult.addError(fieldError);
        } catch (EmailAlreadyExists eae) {
            modelAndView.setViewName("home/register.html");
            FieldError fieldError =
                    new FieldError("userRegisterBindingModel", "email", eae.getMessage());
            bindingResult.addError(fieldError);
        }

        return modelAndView;
    }

    @GetMapping("/login")
    public String getLoginPage() {
        return "home/login.html";
    }

    @PostMapping("/login")
    public ModelAndView postLoginPage(
            ModelAndView modelAndView,
            @ModelAttribute("userLoginBindingModel") UserLoginBindingModel userLoginBindingModel,
            HttpSession session) {
        System.out.println(userLoginBindingModel);
        try {
            UserData userData = homeService.loginUser(userLoginBindingModel);
            session.setAttribute(UserData.NAME, userData);
            modelAndView.setViewName("redirect:/");
            return modelAndView;
        } catch (NoSuchUser nsu) {
            modelAndView.setViewName("home/login.html");
            modelAndView.addObject("errorMessage", nsu.getMessage());
            return modelAndView;
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        UserData userData = new UserData(); //empty data
        session.setAttribute(UserData.NAME, userData);
        return "redirect:/";
    }

    @ModelAttribute("userRegisterBindingModel")
    public UserRegisterBindingModel getUserRegisterBindingModel() {
        return new UserRegisterBindingModel();
    }

    @ModelAttribute("userLoginBindingModel")
    public UserLoginBindingModel getUserLoginBindingModel() {
        return new UserLoginBindingModel();
    }
}
