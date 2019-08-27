package tu.faas.web.interceptors;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import tu.faas.domain.constants.RoleConstants;
import tu.faas.domain.entities.Product;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class GlobalInterceptor extends HandlerInterceptorAdapter {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession();
        if (session.getAttribute("roles") == null || session.getAttribute("userId") == null) {
            Set<String> roles = new HashSet<>();
            roles.add(RoleConstants.ROLE_ANONYMOUS);
            session.setAttribute("roles", roles);

            session.setAttribute("userId", 0L);

            //TODO what kind of variation of the Product class should the cart hold?
            List<Product> products = new ArrayList<>();
            session.setAttribute("shoppingCart", products);
        }
        return true;
    }
}
