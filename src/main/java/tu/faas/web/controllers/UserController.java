package tu.faas.web.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import tu.faas.domain.exceptions.*;
import tu.faas.domain.models.binding.UserEditPasswordModel;
import tu.faas.domain.models.multipurpose.UserEmailModel;
import tu.faas.domain.models.multipurpose.UserNameModel;
import tu.faas.domain.models.view.OrderHistoryViewModel;
import tu.faas.domain.models.view.UserProfileViewModel;
import tu.faas.services.contracts.OrderService;
import tu.faas.services.contracts.UserService;
import tu.faas.web.session.UserData;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/user")
public class UserController {
    private UserService userService;
    private OrderService orderService;

    @Autowired
    public UserController(UserService userService, OrderService orderService) {
        this.userService = userService;
        this.orderService = orderService;
    }

    @GetMapping("/profile")
    public ModelAndView getProfilePage(ModelAndView modelAndView, HttpSession session) {
        Long userId = ((UserData) session.getAttribute(UserData.NAME)).getId();
        UserProfileViewModel userProfileViewModel = userService.getUserProfileViewModel(userId);
        modelAndView.addObject("userProfileViewModel", userProfileViewModel);

        modelAndView.setViewName("user/profile.html");
        return modelAndView;
    }

    @GetMapping("/profile/edit-name")
    public ModelAndView getEditNamePage(ModelAndView modelAndView,
                                        HttpSession session) {
        UserNameModel userNameModel =
                userService.getUserNameModel(((UserData) session.getAttribute(UserData.NAME)).getId());
        modelAndView.addObject(userNameModel);

        modelAndView.setViewName("user/edit-name.html");
        return modelAndView;
    }

    @PutMapping("/profile/edit-name")
    public ModelAndView putEditNamePage(ModelAndView modelAndView,
                                        HttpSession session,
                                        @Valid @ModelAttribute("userNameModel") UserNameModel bindingModel,
                                        BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            modelAndView.setViewName("user/edit-name.html");
            modelAndView.addObject("userNameModel", bindingModel);
            return modelAndView;
        }

        try {
            userService.editUserName(bindingModel,
                    ((UserData) session.getAttribute(UserData.NAME)).getId());

            modelAndView.setViewName("redirect:/user/profile");
        } catch (WrongPassword wp) {
            modelAndView.setViewName("user/edit-name.html");
            FieldError fieldError =
                    new FieldError("userNameModel", "password", wp.getMessage());
            bindingResult.addError(fieldError);
        } catch (UsernameAlreadyExists | SameName err) {
            modelAndView.setViewName("user/edit-name.html");
            FieldError fieldError =
                    new FieldError("userNameModel", "name", err.getMessage());
            bindingResult.addError(fieldError);
        }

        return modelAndView;
    }


    @GetMapping("/profile/edit-email")
    public ModelAndView getEditEmailPage(ModelAndView modelAndView,
                                         HttpSession session) {
        UserEmailModel userEmailModel =
                userService.getUserEmailModel(((UserData) session.getAttribute(UserData.NAME)).getId());
        modelAndView.addObject(userEmailModel);

        modelAndView.setViewName("user/edit-email.html");
        return modelAndView;
    }

    @PutMapping("/profile/edit-email")
    public ModelAndView putEditEmailPage(ModelAndView modelAndView,
                                         HttpSession session,
                                         @Valid @ModelAttribute("userEmailModel") UserEmailModel bindingModel,
                                         BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            modelAndView.setViewName("user/edit-email.html");
            modelAndView.addObject("userEmailModel", bindingModel);
            return modelAndView;
        }

        try {
            userService.editUserEmail(bindingModel,
                    ((UserData) session.getAttribute(UserData.NAME)).getId());

            modelAndView.setViewName("redirect:/user/profile");
        } catch (WrongPassword wp) {
            modelAndView.setViewName("user/edit-email.html");
            FieldError fieldError =
                    new FieldError("userEmailModel", "password", wp.getMessage());
            bindingResult.addError(fieldError);
        } catch (EmailAlreadyExists | SameEmail err) {
            modelAndView.setViewName("user/edit-email.html");
            FieldError fieldError =
                    new FieldError("userEmailModel", "email", err.getMessage());
            bindingResult.addError(fieldError);
        }

        return modelAndView;
    }

    @GetMapping("/profile/edit-password")
    public String getEditPasswordPage() {
        return "user/edit-password.html";
    }

    @PutMapping("/profile/edit-password")
    public ModelAndView putEditPasswordPage(ModelAndView modelAndView,
                                            HttpSession session,
                                            @Valid @ModelAttribute("userEditPasswordModel") UserEditPasswordModel bindingModel,
                                            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            modelAndView.setViewName("user/edit-password.html");
            modelAndView.addObject("userEditPasswordModel", bindingModel);
            return modelAndView;
        }

        try {
            userService.editPassword(bindingModel,
                    ((UserData) session.getAttribute(UserData.NAME)).getId());

            modelAndView.setViewName("redirect:/user/profile");

        } catch (PasswordsNotSame pns) {
            modelAndView.setViewName("user/edit-password.html");

            FieldError fieldError =
                    new FieldError("userEditPasswordModel", "rePassword", pns.getMessage());
            bindingResult.addError(fieldError);
        } catch (SamePassword sp) {
            modelAndView.setViewName("user/edit-password.html");

            FieldError fieldError =
                    new FieldError("userEditPasswordModel", "newPassword", sp.getMessage());
            bindingResult.addError(fieldError);
        } catch (WrongPassword wp) {
            modelAndView.setViewName("user/edit-password.html");

            FieldError fieldError =
                    new FieldError("userEditPasswordModel", "oldPassword", wp.getMessage());
            bindingResult.addError(fieldError);
        }

        return modelAndView;
    }

    @GetMapping("order-history")
    public ModelAndView getOrderHistoryPage(ModelAndView modelAndView,
                                            HttpSession session) {
        Long userId = ((UserData) session.getAttribute(UserData.NAME)).getId();
        List<OrderHistoryViewModel> orderHistoryViewModels =
                orderService.getOrderHistoryViewModelsByUserIdOrderedByDateNewest(userId);

        modelAndView.addObject("orderHistoryViewModels", orderHistoryViewModels);

        modelAndView.setViewName("user/order-history.html");
        return modelAndView;
    }

    @ModelAttribute("userNameModel")
    public UserNameModel getUserNameModel() {
        return new UserNameModel();
    }

    @ModelAttribute("userEmailModel")
    public UserEmailModel getUserEmailModel() {
        return new UserEmailModel();
    }

    @ModelAttribute("userEditPasswordModel")
    public UserEditPasswordModel getUserEditPasswordModel() {
        return new UserEditPasswordModel();
    }
}
