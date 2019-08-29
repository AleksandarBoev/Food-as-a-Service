package tu.faas.web.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import tu.faas.domain.constants.SessionConstants;
import tu.faas.domain.models.binding.ProductAddToCartBindingModel;
import tu.faas.domain.models.binding.ProductAdjustQuantityBindingModel;
import tu.faas.domain.models.view.ShoppingCartItemsCount;
import tu.faas.domain.models.view.ShoppingCartViewModel;
import tu.faas.services.OrderService;

import javax.servlet.http.HttpSession;
import java.util.Map;

@Controller
@RequestMapping("/order")
@SuppressWarnings("unchecked")
public class OrderController {
    private OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/add-to-cart")
    @ResponseBody
    public void addToShoppingCart(@RequestBody ProductAddToCartBindingModel bindingModel,
                                  HttpSession session) {
        Map<Long, Integer> productIdCount = getShoppingCart(session);
        incrementProductCount(productIdCount, bindingModel.getProductId());
        session.setAttribute(SessionConstants.SHOPPING_CART_ITEMS_COUNT, getShoppingCartItemsCount(productIdCount));
    }

    @PostMapping("/adjust-quantity")
    @ResponseBody
    public void adjustQuantity(@RequestBody ProductAdjustQuantityBindingModel bindingModel,
                               HttpSession session) {
        Map<Long, Integer> productIdCount = getShoppingCart(session);

        if (bindingModel.getProductId() == -1) { //remove all button pressed
            productIdCount.clear();
        } else if (bindingModel.getQuantity() <= 0) { //remove button pressed
            productIdCount.remove(bindingModel.getProductId());
        } else { //quantity adjust
            productIdCount.put(bindingModel.getProductId(), bindingModel.getQuantity());
        }
        System.out.println(bindingModel);
        System.out.println(productIdCount);
        session.setAttribute(SessionConstants.SHOPPING_CART_ITEMS_COUNT, getShoppingCartItemsCount(productIdCount));
    }

    @GetMapping(value = "/shopping-cart-items-count", produces = "application/json")
    @ResponseBody
    public ShoppingCartItemsCount getShoppingCartItemsCount(HttpSession session) {
        Map<Long, Integer> productIdCount = getShoppingCart(session);

        ShoppingCartItemsCount shoppingCartItemsCount = new ShoppingCartItemsCount();
        shoppingCartItemsCount.setItemsCount(getShoppingCartItemsCount(productIdCount));

        return shoppingCartItemsCount;
    }

    @GetMapping("/shopping-cart")
    public ModelAndView getShoppingCartPage(ModelAndView modelAndView, HttpSession session) {
        ShoppingCartViewModel shoppingCartViewModel =
                orderService.getShoppingCartViewModel(getShoppingCart(session));

        modelAndView.addObject(SessionConstants.SHOPPING_CART, shoppingCartViewModel);
        modelAndView.setViewName("/order/shopping-cart.html");
        return modelAndView;
    }

    @GetMapping("/information-and-billing")
    public ModelAndView getInformationAndBillingPage(ModelAndView modelAndView,
                                                     HttpSession session) {
        ShoppingCartViewModel shoppingCartViewModel =
                orderService.getShoppingCartViewModel(getShoppingCart(session));
        modelAndView.addObject("shoppingCart", shoppingCartViewModel);

        modelAndView.setViewName("/order/information-and-payment.html");
        return modelAndView;
    }

    private Map<Long, Integer> getShoppingCart(HttpSession session) {
        return (Map<Long, Integer>) session.getAttribute(SessionConstants.SHOPPING_CART);
    }

    private void incrementProductCount(Map<Long, Integer> productIdCount, Long productId) {
        if (!productIdCount.containsKey(productId)) {
            productIdCount.put(productId, 0);
        }

        productIdCount.put(productId, productIdCount.get(productId) + 1);
    }

    private int getShoppingCartItemsCount(Map<? extends Number, ? extends Number> productIdCount) {
        int result = 0;
        for (Number count : productIdCount.values()) {
            result += count.intValue();
        }

        return result;
    }
}
