package tu.faas.web.interceptors;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import tu.faas.web.session.UserData;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.*;

@Component
public class GlobalInterceptor extends HandlerInterceptorAdapter {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession();
//        if (session.getAttribute("roles") == null || session.getAttribute("userId") == null) {
//            Set<String> roles = new HashSet<>();
//            roles.add(RoleConstants.ROLE_ANONYMOUS);
//            session.setAttribute(SessionConstants.ROLES, roles);
//
//            session.setAttribute(SessionConstants.USER_ID, 0L);
//
//            Map<Long, Integer> productIdCount = new LinkedHashMap<>();
//            session.setAttribute(SessionConstants.SHOPPING_CART, productIdCount);
//            session.setAttribute(SessionConstants.SHOPPING_CART_ITEMS_COUNT, 0);
//
//            SessionClass sessionClass = new SessionClass();
//            session.setAttribute("sessionClass", sessionClass);
//        }
        if (session.getAttribute(UserData.NAME) == null) {
            UserData userData = new UserData();
            session.setAttribute(UserData.NAME, userData);
        }

        return true;
    }
}
