package tu.faas.web.interceptors;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import tu.faas.domain.constants.RoleConstants;
import tu.faas.domain.constants.SessionConstants;
import tu.faas.domain.exceptions.Unauthorized;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Set;

@Component
public class ManagerPagesInterceptor extends HandlerInterceptorAdapter {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession();
        Set<String> currentUserRoles =
                (Set<String>)session.getAttribute(SessionConstants.ROLES);
        if (!currentUserRoles.contains(RoleConstants.ROLE_MANAGER)) {
            throw new Unauthorized();
        }

        String uriPath = request.getRequestURI(); //   /restaurants/edit/5
        Set<String> myRestaurants = (Set<String>)session.getAttribute(SessionConstants.MY_RESTAURANTS);
        String uriPathEnding = uriPath.substring(uriPath.lastIndexOf("/") + 1); //   /5
        if (uriPath.startsWith("/restaurants") && (uriPath.contains("edit") || uriPath.contains("delete")) && !myRestaurants.contains(uriPathEnding)) {
            throw new Unauthorized();
        }
        return true;
    }
}
