package tu.faas.web.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import tu.faas.domain.models.binding.RestaurantCreateBindingModel;
import tu.faas.domain.models.multipurpose.RestaurantModel;
import tu.faas.domain.models.view.ProductAllViewModel;
import tu.faas.domain.models.view.RestaurantAllViewModel;
import tu.faas.domain.models.view.RestaurantSalesViewModel;
import tu.faas.domain.models.view.RestaurantViewModel;
import tu.faas.services.contracts.ProductService;
import tu.faas.services.contracts.RestaurantService;
import tu.faas.web.session.UserData;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

@Controller
@RequestMapping("/restaurants")
public class RestaurantController {
    private RestaurantService restaurantService;
    private ProductService productService;

    @Autowired
    public RestaurantController(RestaurantService restaurantService, ProductService productService) {
        this.restaurantService = restaurantService;
        this.productService = productService;
    }

    @GetMapping
    public ModelAndView getRestaurantsPage(ModelAndView modelAndView,
                                           @RequestParam(name = "search", required = false) String search,
                                           @RequestParam(name = "option", required = false) String option) {
        List<RestaurantAllViewModel> restaurantAllViewModels =
                restaurantService.getRestaurantAllViewModels(search, option);

        modelAndView.addObject("restaurantAllViewModels", restaurantAllViewModels);
        modelAndView.setViewName("restaurant/restaurants.html");
        return modelAndView;
    }

    @GetMapping("/my-restaurants")
    public ModelAndView getMyRestaurantsPage(ModelAndView modelAndView,
                                             HttpSession session,
                                             @RequestParam(name = "search", required = false) String search,
                                             @RequestParam(name = "option", required = false) String option) {
        Long managerId = ((UserData) session.getAttribute(UserData.NAME)).getId();
        List<RestaurantAllViewModel> restaurantAllViewModels =
                restaurantService.getRestaurantsByManager(managerId, search, option);

        modelAndView.addObject("restaurantAllViewModels", restaurantAllViewModels);
        modelAndView.setViewName("restaurant/manager-restaurants.html");
        return modelAndView;
    }

    @GetMapping("/create")
    public String getRestaurantCreatePage() {
        return "restaurant/create-restaurant.html";
    }

    @PostMapping("/create")
    public ModelAndView postRestaurantCreatePage(
            HttpSession session,
            ModelAndView modelAndView,
            @Valid @ModelAttribute("restaurantCreateBindingModel") RestaurantCreateBindingModel bindingModel,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            modelAndView.setViewName("restaurant/create-restaurant.html");
            return modelAndView;
        }

        UserData userData = (UserData)session.getAttribute(UserData.NAME);

        Long restaurantId = restaurantService.createRestaurant(bindingModel, userData.getId());
        userData.getManagedRestaurants().add(restaurantId);
        modelAndView.setViewName("redirect:/restaurants/view/" + restaurantId);
        return modelAndView;
    }

    @GetMapping("/view/{restaurantId}")
    public ModelAndView getRestaurantViewPage(
            ModelAndView modelAndView,
            @PathVariable(name = "restaurantId", required = true) Long restaurantId) {
        RestaurantViewModel restaurantViewModel = restaurantService.getRestaurantViewModel(restaurantId);
        modelAndView.addObject("restaurantViewModel", restaurantViewModel);
        List<ProductAllViewModel> productAllViewModels =
                productService.getProductAllViewModelsByRestaurantId(restaurantId);
        modelAndView.addObject("productAllViewModels", productAllViewModels);

        modelAndView.setViewName("restaurant/view-restaurant.html");
        return modelAndView;
    }

    @GetMapping("/edit/{restaurantId}")
    public ModelAndView getRestaurantEditPage(ModelAndView modelAndView,
                                              @PathVariable(name = "restaurantId") Long restaurantId) {
        RestaurantModel restaurantModel = restaurantService.getRestaurantModelById(restaurantId);
        modelAndView.addObject("restaurantModel", restaurantModel);
        modelAndView.setViewName("restaurant/edit-restaurant.html");
        return modelAndView;
    }

    @PutMapping("/edit/{id}")
    public String putRestaurantEditPage(
            @PathVariable(name = "id") Long restaurantId,
            @Valid @ModelAttribute("restaurantModel") RestaurantModel bindingModel,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "restaurant/edit-restaurant.html";
        }

        restaurantService.editRestaurant(bindingModel);
        return "redirect:/restaurants/view/" + restaurantId;
    }

    @GetMapping("/delete/{id}")
    public ModelAndView getRestaurantDeletePage(
            ModelAndView modelAndView,
            @PathVariable(name = "id") Long restaurantId) {
        RestaurantModel restaurantModel = restaurantService.getRestaurantModelById(restaurantId);
        modelAndView.addObject("restaurantModel", restaurantModel);
        modelAndView.setViewName("restaurant/delete-restaurant.html");
        return modelAndView;
    }

    @DeleteMapping("/delete/{id}")
    public String deleteRestaurant(@PathVariable(name = "id") Long restaurantId) {
        restaurantService.deleteRestaurant(restaurantId);
        return "redirect:/restaurants/my-restaurants";
    }

    @GetMapping("/sales/{id}")
    public ModelAndView getRestaurantStatisticsPage(ModelAndView modelAndView,
                                                    @PathVariable(name = "id", required = true) Long restaurantId,
                                                    @RequestParam(name = "dateFrom", required = false) String dateFrom,
                                                    @RequestParam(name = "dateTo", required = false) String dateTo) {
        LocalDate dateFromLocalDate;
        try {
            dateFromLocalDate = LocalDate.parse(dateFrom);
        } catch (DateTimeParseException | NullPointerException err) {
            dateFromLocalDate = null;
        }

        LocalDate dateToLocalDate;
        try {
            dateToLocalDate = LocalDate.parse(dateTo);
        } catch (DateTimeParseException | NullPointerException err) {
            dateToLocalDate = null;
        }

        RestaurantSalesViewModel restaurantSalesViewModel =
                restaurantService.getRestaurantSalesViewModel(restaurantId, dateFromLocalDate, dateToLocalDate);
        modelAndView.addObject("restaurantSalesViewModel", restaurantSalesViewModel);

        modelAndView.setViewName("restaurant/sales.html");
        return modelAndView;
    }

    @ModelAttribute("restaurantModel")
    public RestaurantModel getRestaurantModel() {
        return new RestaurantModel();
    }

    @ModelAttribute("restaurantCreateBindingModel")
    public RestaurantCreateBindingModel getRestaurantCreateBindingModel() {
        return new RestaurantCreateBindingModel();
    }

}
