package tu.faas.web.controllers;

import org.dom4j.rule.Mode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import tu.faas.domain.exceptions.EmailAlreadyExists;
import tu.faas.domain.exceptions.NoSuchUser;
import tu.faas.domain.exceptions.PasswordsNotSame;
import tu.faas.domain.exceptions.UsernameAlreadyExists;
import tu.faas.domain.models.binding.UserLoginBindingModel;
import tu.faas.domain.models.binding.UserRegisterBindingModel;
import tu.faas.domain.models.view.UserProfileViewModel;
import tu.faas.services.contracts.UserService;

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

    @GetMapping("/profile")
    public ModelAndView getProfilePage(ModelAndView modelAndView, HttpSession session) {
        Long userId = (Long)session.getAttribute("userId");
        UserProfileViewModel userProfileViewModel = userService.getUserProfileViewModel(userId);
        modelAndView.addObject("userProfileViewModel", userProfileViewModel);

        modelAndView.setViewName("user/edit-profile.html");
        return modelAndView;
    }

    @GetMapping("/profile/edit")
    public ModelAndView getProfileEditPage(ModelAndView modelAndView,
                                           HttpSession session) {
//        Long userId = (Long)session.getAttribute("userId");
//        UserProfileViewModel userProfileViewModel = userService.getUserProfileViewModel(userId);
//        modelAndView.addObject("userProfileViewModel", userProfileViewModel);
//
//        modelAndView.setViewName("user/edit-profile.html");
//        return modelAndView;
        return null;
    }
}
