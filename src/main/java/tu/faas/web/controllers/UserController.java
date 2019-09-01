package tu.faas.web.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import tu.faas.domain.constants.SessionConstants;
import tu.faas.domain.exceptions.*;
import tu.faas.domain.models.multipurpose.UserEmailModel;
import tu.faas.domain.models.multipurpose.UserNameModel;
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
        Long userId = (Long) session.getAttribute(SessionConstants.USER_ID);
        UserProfileViewModel userProfileViewModel = userService.getUserProfileViewModel(userId);
        modelAndView.addObject("userProfileViewModel", userProfileViewModel);

        modelAndView.setViewName("user/profile.html");
        return modelAndView;
    }

    @GetMapping("/profile/edit-name")
    public ModelAndView getEditNamePage(ModelAndView modelAndView,
                                        HttpSession session) {
        UserNameModel userNameModel =
                userService.getUserNameModel((Long) session.getAttribute(SessionConstants.USER_ID));
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
                    (Long) session.getAttribute(SessionConstants.USER_ID));

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
                userService.getUserEmailModel((Long) session.getAttribute(SessionConstants.USER_ID));
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
                    (Long) session.getAttribute(SessionConstants.USER_ID));

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

    @ModelAttribute("userNameModel")
    public UserNameModel getUserNameModel() {
        return new UserNameModel();
    }

    @ModelAttribute("userEmailModel")
    public UserEmailModel getUserEmailModel() {
        return new UserEmailModel();
    }
}
