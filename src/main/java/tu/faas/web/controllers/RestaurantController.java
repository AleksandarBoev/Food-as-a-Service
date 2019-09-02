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
        modelAndView.setViewName("restaurant/restaurants2.html");
        return modelAndView;
    }

    @GetMapping("/my-restaurants")
    public ModelAndView getMyRestaurantsPage(ModelAndView modelAndView,
                                             HttpSession session) {
        //TODO rename html to my-restaurants
        //TODO maybe throw in a statistics comparison between restauratns... or not.
        List<RestaurantAllViewModel> restaurantAllViewModels =
                restaurantService.getRestaurantsByManager2((Long)session.getAttribute("userId"));

        modelAndView.addObject("restaurantAllViewModels", restaurantAllViewModels);
        modelAndView.setViewName("/restaurant/restaurants.html");
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

        Long restaurantId = restaurantService.createRestaurant(bindingModel, (Long) session.getAttribute("userId"));
        modelAndView.setViewName("redirect:/restaurants/view/" + restaurantId);
        return modelAndView;
    }

    @GetMapping("/view/{restaurantId}")
    public ModelAndView getRestaurantViewPage(
            ModelAndView modelAndView,
            @PathVariable(name = "restaurantId", required = true) Long restaurantId) {
        //TODO change up the way products are listed. List them like in "products" page.
        //Just filter out the ones, that aren't part of this restaurant.
        //TODO and while you're at it, add the table-kind of visualization to a fragment if you can
        //But how is the quantities button and quantities field gonna be replaced? Maybe just
        //make the input disabled.
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
                                              @PathVariable(name = "restaurantId", required = true) Long restaurantId) {
        RestaurantModel restaurantModel = restaurantService.getRestaurantModelById(restaurantId);
        modelAndView.addObject("restaurantModel", restaurantModel);
        modelAndView.setViewName("restaurant/edit-restaurant.html");
        return modelAndView;
    }

    @PutMapping("/edit/{id}")
    public String putRestaurantEditPage(
            @PathVariable(name = "id", required = true) Long restaurantId,
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
            @PathVariable(name = "id", required = true) Long restaurantId
    ) {
        RestaurantModel restaurantModel = restaurantService.getRestaurantModelById(restaurantId);
        modelAndView.addObject("restaurantModel", restaurantModel);
        modelAndView.setViewName("restaurant/delete-restaurant.html");
        return modelAndView;
    }

    @DeleteMapping("/delete/{id}")
    public String deleteRestaurant(@PathVariable(name = "id", required = true) Long restaurantId) {
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
