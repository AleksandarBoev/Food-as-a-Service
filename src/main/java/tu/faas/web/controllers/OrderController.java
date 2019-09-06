package tu.faas.web.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import tu.faas.domain.constants.RoleConstants;
import tu.faas.domain.models.binding.BillingInformationBindingModel;
import tu.faas.domain.models.binding.OrderBindingModel;
import tu.faas.domain.models.binding.ProductAddToCartBindingModel;
import tu.faas.domain.models.binding.ProductAdjustQuantityBindingModel;
import tu.faas.domain.models.view.ShoppingCartViewModel;
import tu.faas.services.contracts.OrderService;
import tu.faas.web.session.UserData;

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
    public ResponseEntity addToShoppingCart(@RequestBody ProductAddToCartBindingModel bindingModel,
                                            HttpSession session) {
        UserData userData = (UserData) session.getAttribute(UserData.NAME);

        if (!userData.getRoles().contains(RoleConstants.ROLE_USER)) {
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        } else {
            Map<Long, Integer> shoppingCart = userData.getShoppingCart();
            incrementProductCount(shoppingCart, bindingModel.getProductId());
            return new ResponseEntity(HttpStatus.OK);
        }
    }

    @PostMapping("/adjust-quantity")
    @ResponseBody
    public void adjustQuantity(@RequestBody ProductAdjustQuantityBindingModel bindingModel,
                               HttpSession session) {
        Map<Long, Integer> shoppingCart = ((UserData)session.getAttribute(UserData.NAME)).getShoppingCart();

        if (bindingModel.getProductId() == -1) { //remove all button pressed
            shoppingCart.clear();
        } else if (bindingModel.getQuantity() <= 0) { //remove button pressed
            shoppingCart.remove(bindingModel.getProductId());
        } else { //quantity adjust
            shoppingCart.put(bindingModel.getProductId(), bindingModel.getQuantity());
        }
    }

    @GetMapping(value = "/shopping-cart-items-count", produces = "application/json")
    @ResponseBody
    public Integer getShoppingCartItemsCount(HttpSession session) {
        UserData userData = (UserData)session.getAttribute(UserData.NAME);
        Integer itemsCount = userData.getShoppingCartItemsCount();
        return itemsCount;
    }

    @GetMapping("/shopping-cart")
    public ModelAndView getShoppingCartPage(ModelAndView modelAndView, HttpSession session) {
        Map<Long, Integer> shoppingCart = ((UserData) session.getAttribute(UserData.NAME)).getShoppingCart();
        ShoppingCartViewModel shoppingCartViewModel =
                orderService.getShoppingCartViewModel(shoppingCart);

        modelAndView.addObject("shoppingCart", shoppingCartViewModel);
        modelAndView.setViewName("order/shopping-cart.html");
        return modelAndView;
    }

    @GetMapping("/billing")
    public ModelAndView getInformationAndBillingPage(ModelAndView modelAndView,
                                                     HttpSession session) {
        Map<Long, Integer> shoppingCart = ((UserData) session.getAttribute(UserData.NAME)).getShoppingCart();
        ShoppingCartViewModel shoppingCartViewModel =
                orderService.getShoppingCartViewModel(shoppingCart);

        modelAndView.addObject("shoppingCart", shoppingCartViewModel);
        modelAndView.setViewName("order/billing.html");
        return modelAndView;
    }

    @PostMapping("/billing")
    public String postInformationAndPilling(HttpSession session,
                           @Valid @ModelAttribute("billingInformation") BillingInformationBindingModel bindingModel,
                           BindingResult bindingResult,
                           RedirectAttributes redirectAttributes) {
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
        Map<Long, Integer> shoppingCart = ((UserData)session.getAttribute(UserData.NAME)).getShoppingCart();
        ShoppingCartViewModel shoppingCartViewModel =
                orderService.getShoppingCartViewModel(shoppingCart);

        modelAndView.addObject("shoppingCart", shoppingCartViewModel);
        modelAndView.addObject("billingInformation", billingInformationBindingModel);
        modelAndView.setViewName("order/checkout.html");
        return modelAndView;
    }

    @PostMapping("/checkout")
    public String postCheckoutPage(HttpSession session,
                                   @RequestParam(name = "address", required = false) String address,
                                   @RequestParam(name = "billingType", required = false) String billingType) {
        OrderBindingModel orderBindingModel = new OrderBindingModel();

        orderBindingModel.setAddress((String) session.getAttribute("address"));
        orderBindingModel.setBillingType((String) session.getAttribute("billingType"));

        UserData userData = (UserData)session.getAttribute(UserData.NAME);
        orderBindingModel.setCustomerId(userData.getId());

        Map<Long, Integer> shoppingCart = userData.getShoppingCart();
        orderBindingModel.setProductIdQuantities(shoppingCart);

        orderService.makeOrder(orderBindingModel);
        shoppingCart.clear();

        return "order/success-order.html";
    }

    @ModelAttribute("billingInformation")
    public BillingInformationBindingModel getBillingInformationModel() {
        return new BillingInformationBindingModel();
    }

    private void incrementProductCount(Map<Long, Integer> productIdCount, Long productId) {
        if (!productIdCount.containsKey(productId)) {
            productIdCount.put(productId, 0);
        }

        productIdCount.put(productId, productIdCount.get(productId) + 1);
    }
}
