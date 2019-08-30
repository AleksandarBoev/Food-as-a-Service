package tu.faas.web.controllers;

import org.aspectj.weaver.ast.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import tu.faas.domain.constants.SessionConstants;
import tu.faas.domain.models.binding.BillingInformationBindingModel;
import tu.faas.domain.models.binding.OrderBindingModel;
import tu.faas.domain.models.binding.ProductAddToCartBindingModel;
import tu.faas.domain.models.binding.ProductAdjustQuantityBindingModel;
import tu.faas.domain.models.view.ShoppingCartItemsCount;
import tu.faas.domain.models.view.ShoppingCartViewModel;
import tu.faas.services.contracts.OrderService;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
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
        Map<Long, Integer> productIdCount = getShoppingCartMap(session);
        incrementProductCount(productIdCount, bindingModel.getProductId());
        session.setAttribute(SessionConstants.SHOPPING_CART_ITEMS_COUNT, getShoppingCartItemsCount(productIdCount));
    }

    @PostMapping("/adjust-quantity")
    @ResponseBody
    public void adjustQuantity(@RequestBody ProductAdjustQuantityBindingModel bindingModel,
                               HttpSession session) {
        Map<Long, Integer> productIdCount = getShoppingCartMap(session);

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
        Map<Long, Integer> productIdCount = getShoppingCartMap(session);

        ShoppingCartItemsCount shoppingCartItemsCount = new ShoppingCartItemsCount();
        shoppingCartItemsCount.setItemsCount(getShoppingCartItemsCount(productIdCount));

        return shoppingCartItemsCount;
    }

    @GetMapping("/shopping-cart")
    public ModelAndView getShoppingCartPage(ModelAndView modelAndView, HttpSession session) {
        ShoppingCartViewModel shoppingCartViewModel =
                orderService.getShoppingCartViewModel(getShoppingCartMap(session));

        modelAndView.addObject(SessionConstants.SHOPPING_CART, shoppingCartViewModel);
        modelAndView.setViewName("/order/shopping-cart.html");
        return modelAndView;
    }

    @GetMapping("/billing")
    public ModelAndView getInformationAndBillingPage(ModelAndView modelAndView,
                                                     HttpSession session) {
        ShoppingCartViewModel shoppingCartViewModel =
                orderService.getShoppingCartViewModel(getShoppingCartMap(session));
        modelAndView.addObject("shoppingCart", shoppingCartViewModel);

        modelAndView.setViewName("/order/billing.html");
        return modelAndView;
    }

    @PostMapping("/billing")
    public String checkout(HttpSession session,
                           @Valid @ModelAttribute("billingInformation") BillingInformationBindingModel bindingModel,
                           BindingResult bindingResult,
                           RedirectAttributes redirectAttributes) {
        //TODO if cash is selected and only address is wrong, then also credit info gets errors.
        if ("cash".equals(bindingModel.getBillingType())
                && bindingResult.getFieldError("address") == null) { //check validation for address only
            session.setAttribute("billingType", bindingModel.getBillingType());
            session.setAttribute("address", bindingModel.getAddress());
            return "redirect:/order/checkout";
        } else {
            if (bindingResult.hasErrors()) {
                return "order/billing.html";
            }
        }

        session.setAttribute("billingType", bindingModel.getBillingType());
        session.setAttribute("address", bindingModel.getAddress());
        return "redirect:/order/checkout";
    }

    @GetMapping("/checkout")
    public ModelAndView getCheckoutPage(ModelAndView modelAndView,
                                        HttpSession session,
                                        @ModelAttribute("flashBillingInformation") BillingInformationBindingModel billingInformationBindingModel) {
        ShoppingCartViewModel shoppingCartViewModel =
                orderService.getShoppingCartViewModel(getShoppingCartMap(session));
        modelAndView.addObject("shoppingCart", shoppingCartViewModel);
        modelAndView.addObject("billingInformation", billingInformationBindingModel);
        modelAndView.setViewName("/order/checkout.html");
        return modelAndView;
    }

    @PostMapping("/checkout")
    public String postCheckoutPage(HttpSession session,
                                   @RequestParam(name = "address", required = false) String address,
                                   @RequestParam(name = "billingType", required = false) String billingType) {
        OrderBindingModel orderBindingModel = new OrderBindingModel();

        orderBindingModel.setAddress((String) session.getAttribute("address"));
        orderBindingModel.setBillingType((String) session.getAttribute("billingType"));

        orderBindingModel.setCustomerId((Long) session.getAttribute("userId"));
        orderBindingModel.setProductIdQuantities(getShoppingCartMap(session));

        orderService.makeOrder(orderBindingModel);

        return "/order/success-order.html";
    }

    @ModelAttribute("billingInformation")
    public BillingInformationBindingModel getBillingInformationModel() {
        return new BillingInformationBindingModel();
    }

    private Map<Long, Integer> getShoppingCartMap(HttpSession session) {
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
