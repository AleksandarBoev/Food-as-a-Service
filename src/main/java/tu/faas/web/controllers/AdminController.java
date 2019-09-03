package tu.faas.web.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import tu.faas.domain.constants.RoleConstants;
import tu.faas.domain.constants.SessionConstants;
import tu.faas.domain.exceptions.WrongPassword;
import tu.faas.domain.models.binding.AdminAction;
import tu.faas.domain.models.view.UserUsersViewModel;
import tu.faas.services.contracts.UserService;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private UserService userService;

    @Autowired
    public AdminController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    public ModelAndView getUsersPage(ModelAndView modelAndView, HttpSession session) {
        List<UserUsersViewModel> userViewModels =
                userService.getUserViewModelsWithoutAdmin((Long) session.getAttribute(SessionConstants.USER_ID));
        modelAndView.addObject("userViewModels", userViewModels);

        modelAndView.setViewName("admin/users.html");
        return modelAndView;
    }

    @PostMapping("/users")
    @ResponseBody
    public ResponseEntity orderProduct(HttpSession session,
                                       @RequestBody AdminAction requestBodyAdminAction) {
        Set<String> roles = (Set<String>) session.getAttribute(SessionConstants.ROLES);

        if (!roles.contains(RoleConstants.ROLE_ROOT_ADMIN)) {
            return new ResponseEntity(HttpStatus.UNAUTHORIZED); //wrong password
        }

        Long adminId = (Long)session.getAttribute(SessionConstants.USER_ID);

        try {
            userService.updateUser(requestBodyAdminAction, adminId);
            return new ResponseEntity(HttpStatus.OK); //everything is alright
        } catch (WrongPassword wp) {
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }
    }
}
