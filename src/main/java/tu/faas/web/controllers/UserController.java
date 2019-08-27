package tu.faas.web.controllers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import tu.faas.domain.beans.Person;
import tu.faas.domain.beans.SomeClass;
import tu.faas.domain.exceptions.NoSuchUser;
import tu.faas.domain.exceptions.UserAlreadyExists;
import tu.faas.domain.models.binding.UserLoginBindingModel;
import tu.faas.domain.models.binding.UserRegisterBindingModel;
import tu.faas.services.UserService;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Controller
@RequestMapping("/user")
public class UserController {
    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/register")
    public ModelAndView getRegisterPage(ModelAndView modelAndView, @ModelAttribute("errorMessage") String errorMessage) {
        modelAndView.setViewName("register.html");
        modelAndView.addObject("errorMessage", errorMessage);
        return modelAndView;
    }

    @PostMapping("/register")
    public ModelAndView postRegisterPage(ModelAndView modelAndView,
                                         @Valid @ModelAttribute("userRegisterBindingModel") UserRegisterBindingModel userRegisterBindingModel,
                                         BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            modelAndView.setViewName("register.html");
            modelAndView.addObject("userRegisterBindingModel", userRegisterBindingModel);
            return modelAndView;
        }

        try {
            userService.registerUser(userRegisterBindingModel);
            modelAndView.setViewName("redirect:/user/login");
        } catch (UserAlreadyExists uae) {
            modelAndView.setViewName("register.html");
            modelAndView.addObject("errorMessage", uae.getMessage());
        }

        return modelAndView;
    }

    @GetMapping("/login")
    public String getLoginPage() {
        return "login.html";
    }

    @PostMapping("/login")
    public ModelAndView postLoginPage(
            ModelAndView modelAndView,
            @ModelAttribute("userLoginBindingModel") UserLoginBindingModel userLoginBindingModel,
            HttpSession session) {
        System.out.println(userLoginBindingModel);
        try {
            userService.loginUser(userLoginBindingModel, session);
            modelAndView.setViewName("redirect:/");
            return modelAndView;
        } catch (NoSuchUser nsu) {
            modelAndView.setViewName("login.html");
            modelAndView.addObject("errorMessage", nsu.getMessage());
            return modelAndView;
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }

    @GetMapping("/profile")
    public String getProfilePage() {
        return "profile.html";
    }

    //    @PostMapping("/order-product")
//    public String orderProduct(@RequestParam(name = "id") Long productId) {
//        System.out.println("AAAAAA Product you want to purchase: " + productId);
//        return "redirect:/";
//    }


    @PostMapping("/order-product")
    @ResponseBody
    public void orderProduct(@RequestBody Person person) {
        //TODO just take the id, get info of the product from db and
        //put that info in the Shopping Cart in the form of some model.
        //"ProductShoppingCartModel". Sounds nice.
        System.out.println(person.getName());
        System.out.println(person.getAge());
        System.out.println(person.getTown());
        System.out.println(person.getNicknames());
        System.out.println(person.getLanguages());
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
