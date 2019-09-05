package tu.faas.web.interceptors;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import tu.faas.domain.constants.RoleConstants;
import tu.faas.domain.constants.SessionConstants;
import tu.faas.domain.exceptions.Unauthorized;
import tu.faas.web.session.UserData;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Set;

@Component
public class RestaurantPagesInterceptor extends HandlerInterceptorAdapter {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        UserData userData = (UserData)request.getSession().getAttribute(UserData.NAME);

        if (!userData.getRoles().contains(RoleConstants.ROLE_MANAGER)) {
            throw new Unauthorized("You are not authorized to access manager pages!");
        }

        String uriPath = request.getRequestURI(); //   example: /restaurants/edit/5
        if (uriPath.contains("edit") || uriPath.contains("delete") || uriPath.contains("sales")) {
            //if a manager wants to edit/delete a restaurant, which is not his, then throw exception
            String uriPathEnding = uriPath.substring(uriPath.lastIndexOf("/") + 1); //exclude dash
            Long restaurantId = Long.parseLong(uriPathEnding);

            String action;
            if (uriPath.contains("edit")) {
                action = "edit";
            } else if (uriPath.contains("delete")) {
                action = "delete";
            } else {
                action = "view sales";
            }

            if (!userData.getManagedRestaurants().contains(restaurantId)) {
                throw new Unauthorized(String.format("You can't perform action \"%s\" on a restaurant you don't own!", action));
            }
        }
        return true;
    }
}
