package tu.faas.web.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import tu.faas.domain.models.multipurpose.ProductModel;
import tu.faas.domain.models.multipurpose.RestaurantModel;
import tu.faas.domain.models.binding.ProductCreateBindingModel;
import tu.faas.domain.models.binding.RestaurantCreateBindingModel;
import tu.faas.domain.models.view.RestaurantListViewModel;
import tu.faas.domain.models.view.ProductViewModel;
import tu.faas.domain.models.view.RestaurantViewModel;
import tu.faas.services.contracts.ManagerService;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/manager")
public class ManagerController {
    private ManagerService managerService;

    @Autowired
    public ManagerController(ManagerService managerService) {
        this.managerService = managerService;
    }

    @GetMapping("/restaurants")
    public ModelAndView getRestaurantsPage(ModelAndView modelAndView, HttpSession httpSession) {
        List<RestaurantListViewModel> restaurants =
                managerService.getRestaurantsByManager((Long) httpSession.getAttribute("userId"));
        //TODO also add number of products each restaurant has?
        modelAndView.addObject("restaurants", restaurants);
        modelAndView.setViewName("manager-restaurants.html");
        return modelAndView;
    }

    @GetMapping("/restaurants/create")
    public String getRestaurantCreatePage() {
        return "restaurant/create-restaurant.html";
    }

    @PostMapping("/restaurants/create")
    public ModelAndView postRestaurantCreatePage(
            HttpSession session,
            ModelAndView modelAndView,
            @Valid @ModelAttribute("restaurantCreateBindingModel") RestaurantCreateBindingModel bindingModel,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
//            modelAndView.addObject("restaurantCreateBindingModel", bindingModel);
            modelAndView.setViewName("restaurant/create-restaurant.html");
            return modelAndView;
        }
        //TODO logic for unique named restaurants? In that case keep the modelAndView.
        //it will be needed. Or just add new FieldError to the binding result.
        //problem is the filled name will disappear and also from where should I
        //add the FieldError logic? Maybe just to it with try ... catch. That way
        //the format will be checked first (easy) and THEN the db will be touched
        // (db operations are hard on the server)
        Long restaurantId = managerService.createRestaurant(bindingModel, (Long) session.getAttribute("userId"));
        modelAndView.setViewName("redirect:/manager/restaurants/view?id=" + restaurantId);
        return modelAndView;
    }

    @GetMapping("/restaurants/view")
    public ModelAndView getRestaurantViewPage(
            ModelAndView modelAndView,
            @RequestParam(name = "id", required = true) Long restaurantId) {
        RestaurantViewModel restaurantViewModel = managerService.getRestaurantViewModel(restaurantId);
        modelAndView.addObject("restaurantViewModel", restaurantViewModel);

        modelAndView.setViewName("restaurant/view-restaurant.html");
        return modelAndView;
    }

    @GetMapping("/restaurants/edit")
    public ModelAndView getRestaurantEditPage(ModelAndView modelAndView,
                                              @RequestParam(name = "id", required = true) Long restaurantId) {
        RestaurantModel restaurantModel = managerService.getRestaurantModelById(restaurantId);
        modelAndView.addObject("restaurantModel", restaurantModel);
        modelAndView.setViewName("restaurant/edit-restaurant.html");
        return modelAndView;
    }

    @PutMapping("/restaurants/edit")
    public String putRestaurantEditPage(
            @RequestParam(name = "id", required = true) Long restaurantId,
            @Valid @ModelAttribute("restaurantModel") RestaurantModel bindingModel,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "restaurant/edit-restaurant.html";
        }

        managerService.editRestaurant(bindingModel);
        return "redirect:/manager/restaurants/view?id=" + restaurantId;
    }

    @GetMapping("/restaurants/delete")
    public ModelAndView getRestaurantDeletePage(
            ModelAndView modelAndView,
            @RequestParam(name = "id", required = true) Long restaurantId
    ) {
        RestaurantModel restaurantModel = managerService.getRestaurantModelById(restaurantId);
        modelAndView.addObject("restaurantModel", restaurantModel);
        modelAndView.setViewName("restaurant/delete-restaurant.html");
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
        modelAndView.setViewName("product/create-product");
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
            modelAndView.setViewName("product/create-product.html");
            return modelAndView;
        }

        managerService.createProduct(bindingModel, restaurantId);
        modelAndView.setViewName("redirect:/manager/restaurants/view?id=" + restaurantId);
        return modelAndView;
    }

    @GetMapping("/edit-product")
    public ModelAndView getEditProductPage(ModelAndView modelAndView,
                                           @RequestParam(name = "id", required = true) Long productId) {
        ProductModel productModel = managerService.getProductModel(productId);
        modelAndView.addObject("productModel", productModel);

        modelAndView.setViewName("product/edit-product.html");
        return modelAndView;
    }

    @PutMapping("/edit-product")
    public String putEditProductPage(
            @RequestParam(name = "id", required = true) Long productId,
            @Valid @ModelAttribute("productModel") ProductModel bindingModel,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "product/edit-product.html";
        }

        Long restaurantId = managerService.editProduct(bindingModel);
        return "redirect:/manager/restaurants/view?id=" + restaurantId;
    }

    @GetMapping("/delete-product")
    public ModelAndView deleteProductViewPage(
            ModelAndView modelAndView,
            @RequestParam(name = "id", required = true) Long productId) {
        ProductModel productModel = managerService.getProductModel(productId);
        modelAndView.addObject("productModel", productModel);
        modelAndView.setViewName("product/delete-product.html");
        return modelAndView;
    }

    @DeleteMapping("/delete-product")
    public String deleteProductFormSubmit(@RequestParam(name = "id", required = true) Long productId) {
        Long restaurantId = managerService.deleteProduct(productId);
        return "redirect:/manager/restaurants/view?id=" + restaurantId;
    }

    @GetMapping("/view-product")
    public ModelAndView getProductView(ModelAndView modelAndView,
                                       @RequestParam(name = "id", required = true) Long productId) {
        ProductViewModel productViewModel = managerService.getProductViewModel(productId);
        modelAndView.addObject("productViewModel", productViewModel);

        modelAndView.setViewName("/product/view-product.html");
        return modelAndView;
    }

    @GetMapping("/restaurants/statistics")
    public ModelAndView getRestaurantStatisticsPage(ModelAndView modelAndView,
                                           @RequestParam(name = "id", required = true) Long restaurantId) {

        modelAndView.addObject("restaurant_id", restaurantId);
        modelAndView.setViewName("restaurant/statistics.html");
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

    @ModelAttribute("productCreateBindingModel")
    public ProductCreateBindingModel getProductCreateBindingModel() {
        return new ProductCreateBindingModel();
    }

    @ModelAttribute("productModel")
    public ProductModel getProductModel() {
        return new ProductModel();
    }
}
