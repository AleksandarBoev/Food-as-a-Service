package tu.faas.web.interceptors;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import tu.faas.web.session.UserData;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Component
public class OrderPagesInterceptor extends HandlerInterceptorAdapter {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession();
        UserData userData = (UserData) session.getAttribute(UserData.NAME);

        if (userData.getShoppingCartItemsCount() == 0) {
            response.sendRedirect("/order/shopping-cart");
            return false;
        }

        if (request.getRequestURI().contains("checkout")
                && (session.getAttribute("billingType") == null || session.getAttribute("address") == null)) {
            response.sendRedirect("/order/billing");
            return false;
        }

        return true;
    }
}
