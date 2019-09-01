package tu.faas.web.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import tu.faas.domain.models.binding.ProductCreateBindingModel;
import tu.faas.domain.models.multipurpose.ProductModel;
import tu.faas.domain.models.view.ProductAllViewModel;
import tu.faas.domain.models.view.ProductViewModel;
import tu.faas.services.contracts.ProductService;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/products")
public class ProductController {
    private ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public ModelAndView getProductsPage(ModelAndView modelAndView,
                                        HttpSession session,
                                        @RequestParam(name = "search", required = false) String search,
                                        @RequestParam(name = "option", required = false) String option) {
        List<ProductAllViewModel> productAllViewModels =
                productService.getProductAllViewModels(search, option); //6 rows
        modelAndView.addObject("productAllViewModels", productAllViewModels);

        modelAndView.setViewName("product/products.html");
        return modelAndView;
    }

    @GetMapping("/{restaurant_id}/create")
    public ModelAndView getCreateProductPage(
            ModelAndView modelAndView,
            @PathVariable(value = "restaurant_id") Long restaurantId) {
        modelAndView.addObject("restaurant_id", restaurantId);

        modelAndView.setViewName("product/create-product");
        return modelAndView;
    }

    @PostMapping("/{restaurant_id}/create")
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

        productService.createProduct(bindingModel, restaurantId);
        modelAndView.setViewName("redirect:/restaurants/view?id=" + restaurantId);
        return modelAndView;
    }

    @GetMapping("/edit")
    public ModelAndView getEditProductPage(ModelAndView modelAndView,
                                           @RequestParam(name = "id", required = true) Long productId) {
        ProductModel productModel = productService.getProductModel(productId);
        modelAndView.addObject("productModel", productModel);

        modelAndView.setViewName("product/edit-product.html");
        return modelAndView;
    }

    @PutMapping("/edit")
    public String putEditProductPage(
            @RequestParam(name = "id", required = true) Long productId,
            @Valid @ModelAttribute("productModel") ProductModel bindingModel,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "product/edit-product.html";
        }

        Long restaurantId = productService.editProduct(bindingModel);
        return "redirect:/restaurants/view?id=" + restaurantId;
    }

    @GetMapping("/delete")
    public ModelAndView deleteProductViewPage(
            ModelAndView modelAndView,
            @RequestParam(name = "id", required = true) Long productId) {
        ProductModel productModel = productService.getProductModel(productId);
        modelAndView.addObject("productModel", productModel);
        modelAndView.setViewName("product/delete-product.html");
        return modelAndView;
    }

    @DeleteMapping("/delete")
    public String deleteProductFormSubmit(@RequestParam(name = "id", required = true) Long productId) {
        Long restaurantId = productService.deleteProduct(productId);
        return "redirect:/restaurants/view?id=" + restaurantId;
    }

    @GetMapping("/view")
    public ModelAndView getProductView(ModelAndView modelAndView,
                                       @RequestParam(name = "id", required = true) Long productId) {
        ProductViewModel productViewModel = productService.getProductViewModel(productId);
        modelAndView.addObject("productViewModel", productViewModel);

        modelAndView.setViewName("/product/view-product.html");
        return modelAndView;
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
