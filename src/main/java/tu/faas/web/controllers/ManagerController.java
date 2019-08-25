package tu.faas.web.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import tu.faas.domain.models.binding.RestaurantCreateBindingModel;
import tu.faas.domain.models.view.ManagerRestaurantsViewModel;
import tu.faas.services.ManagerService;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/manager")
public class ManagerController {
    //TODO dali pri razglejdane na restorant ot strana na user i manager da e edna i sushta stranica?
    //no v zavisimost ot rolite... Nope. Shte ima edit stranica i view stranica!
    private ManagerService managerService;

    @Autowired
    public ManagerController(ManagerService managerService) {
        this.managerService = managerService;
    }

    @GetMapping("/restaurants")
    public ModelAndView getRestaurantsPage(ModelAndView modelAndView, HttpSession httpSession) {
        List<ManagerRestaurantsViewModel> restaurants =
                managerService.getRestaurantsByManager((Long)httpSession.getAttribute("userId"));
        modelAndView.addObject("restaurants", restaurants);
        modelAndView.setViewName("manager-restaurants.html");
        return modelAndView;
    }

    @GetMapping("/restaurants/create")
    public String getRestaurantCreatePage(HttpSession httpSession) {
        return "create-restaurant.html";
    }

    @PostMapping("/restaurants/create")
    public ModelAndView postRestaurantCreatePage(
            HttpSession session,
            ModelAndView modelAndView,
            @Valid @ModelAttribute("restaurantCreateBindingModel") RestaurantCreateBindingModel bindingModel,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            modelAndView.addObject("restaurantCreateBindingModel", bindingModel);
            modelAndView.setViewName("create-restaurant.html");
            return modelAndView;
        }

        managerService.createRestaurant(bindingModel, (Long)session.getAttribute("userId"));
        modelAndView.setViewName("redirect:/");
        return modelAndView;
    }

    @GetMapping("/restaurants/edit/{id}")
    public String getRestaurantEditPage(HttpSession httpSession) {
        //za tova kak se vkarva promenliva v th:href=@{...(${})} -->
        //https://www.thymeleaf.org/doc/articles/standardurlsyntax.html
        return "manager-restaurants.html";
    }

    @PutMapping("/restaurants/edit/{id}")
    public String putRestaurantEditPage(HttpSession httpSession) {
        //TODO v neta pishe che html ne poddurja put i delete. No puk gi polzvam prez thymeleaf.
        //tui che ako ne se poluchi, post i na dvete. Ako se poluchi - yay.
        return "manager-restaurants.html";
    }

    @GetMapping("/restaurants/delete/{id}")
    public String getRestaurantDeletePage(HttpSession httpSession) {
        return "manager-restaurants.html";
    }

    @DeleteMapping("/restaurants/delete/{id}")
    public String deleteRestaurant(HttpSession httpSession) {
        return "manager-restaurants.html";
    }

    @ModelAttribute("restaurantCreateBindingModel")
    public RestaurantCreateBindingModel getRestaurantCreateBindingModel() {
        return new RestaurantCreateBindingModel();
    }
}
