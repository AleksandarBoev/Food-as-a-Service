package tu.faas.web.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import tu.faas.domain.models.multipurpose.ProductEditModel;
import tu.faas.domain.models.multipurpose.RestaurantModel;
import tu.faas.domain.models.binding.ProductCreateBindingModel;
import tu.faas.domain.models.binding.RestaurantCreateBindingModel;
import tu.faas.domain.models.view.ManagerRestaurantsViewModel;
import tu.faas.domain.models.view.ProductEditRestaurantsViewModel;
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
                managerService.getRestaurantsByManager((Long) httpSession.getAttribute("userId"));
        modelAndView.addObject("restaurants", restaurants);
        modelAndView.setViewName("manager-restaurants.html");
        return modelAndView;
    }

    @GetMapping("/restaurants/create")
    public String getRestaurantCreatePage() {
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

        managerService.createRestaurant(bindingModel, (Long) session.getAttribute("userId"));
        modelAndView.setViewName("redirect:/");
        return modelAndView;
    }

    @GetMapping("/restaurants/edit")
    public ModelAndView getRestaurantEditPage(
            ModelAndView modelAndView,
            @RequestParam(name = "id", required = true) Long restaurantId) {
        modelAndView.addObject("restaurant_id", restaurantId);
        RestaurantModel restaurantModel = managerService.getRestaurantById(restaurantId);
        modelAndView.addObject("restaurantModel", restaurantModel);

        List<ProductEditRestaurantsViewModel> productViewModels =
                managerService.getProductViewModels(restaurantId);
        modelAndView.addObject("products", productViewModels);

        modelAndView.setViewName("edit-restaurant.html");
        return modelAndView;
    }

    @PutMapping("/restaurants/edit")
    public ModelAndView putRestaurantEditPage(
            ModelAndView modelAndView,
            @RequestParam(name = "id", required = true) Long restaurantId,
            @Valid @ModelAttribute("restaurantModel") RestaurantModel bindingModel,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            modelAndView.addObject("restaurantModel", bindingModel);
            modelAndView.setViewName("edit-restaurant.html");
            return modelAndView;
        }

        managerService.editRestaurant(bindingModel);
        modelAndView.setViewName("redirect:/manager/restaurants");
        return modelAndView;
    }

    @GetMapping("/restaurants/delete")
    public ModelAndView getRestaurantDeletePage(
            ModelAndView modelAndView,
            @RequestParam(name = "id", required = true) Long restaurantId
    ) {
        RestaurantModel restaurantModel = managerService.getRestaurantById(restaurantId);
        modelAndView.addObject("restaurantModel", restaurantModel);
        modelAndView.setViewName("delete-restaurant.html");
        return modelAndView;
    }

    @DeleteMapping("/restaurants/delete")
    public String deleteRestaurant(@RequestParam(name = "id", required = true) Long restaurantId) {
        managerService.deleteRestaurant(restaurantId);
        return "redirect:/manager/restaurants";
    }

    @GetMapping("/{restaurant_id}/create_product")
    public ModelAndView getCreateProductPage(
            ModelAndView modelAndView,
            @PathVariable(value = "restaurant_id") Long restaurantId) {
        modelAndView.addObject("restaurant_id", restaurantId);
        modelAndView.setViewName("create-product");
        return modelAndView;
    }

    @PostMapping("/{restaurant_id}/create_product")
    public ModelAndView postCreateProductPage(
            ModelAndView modelAndView,
            @PathVariable(value = "restaurant_id") Long restaurantId,
            @Valid @ModelAttribute("productCreateBindingModel") ProductCreateBindingModel bindingModel,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            modelAndView.addObject("productCreateBindingModel", bindingModel);
            modelAndView.setViewName("create-product.html");
            return modelAndView;
        }

        managerService.createProduct(bindingModel, restaurantId);
        modelAndView.setViewName("redirect:/manager/restaurants/edit?id=" + restaurantId);
        return modelAndView;
    }

    @GetMapping("/edit-product")
    public ModelAndView getEditProductPage(ModelAndView modelAndView,
                                           @RequestParam(name = "id", required = true) Long productId) {
        ProductEditModel productEditModel = managerService.getProductEditModel(productId);
        modelAndView.addObject("productEditModel", productEditModel);

        modelAndView.setViewName("edit-product.html");
        return modelAndView;
    }

    @PutMapping("/edit-product")
    public ModelAndView putEditProductPage(
            ModelAndView modelAndView,
            @RequestParam(name = "id", required = true) Long productId,
            @Valid @ModelAttribute("productEditModel") ProductEditModel bindingModel,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            modelAndView.addObject("productEditModel", bindingModel);
            modelAndView.setViewName("edit-restaurant.html");
            return modelAndView;
        }

        managerService.editProduct(bindingModel);
        modelAndView.setViewName("redirect:/manager/restaurants");
        return modelAndView;
    }

    //TODO get and delete mappings for product

    @ModelAttribute("restaurantModel")
    public RestaurantModel getRestaurantModel() {
        return new RestaurantModel();
    }

    @ModelAttribute("restaurantCreateBindingModel")
    public RestaurantCreateBindingModel getRestaurantCreateBindingModel() {
        return new RestaurantCreateBindingModel();
    }

    @ModelAttribute("productCreateBindingModel")
    public ProductCreateBindingModel getProductCreateBindingModel() {
        return new ProductCreateBindingModel();
    }

    @ModelAttribute("productEditModel")
    public ProductEditModel getProductEditModel() {
        return new ProductEditModel();
    }
}
