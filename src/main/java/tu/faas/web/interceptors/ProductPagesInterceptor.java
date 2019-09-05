package tu.faas.web.interceptors;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import tu.faas.domain.exceptions.Unauthorized;
import tu.faas.web.session.UserData;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class ProductPagesInterceptor extends HandlerInterceptorAdapter {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String uriPath = request.getRequestURI();

        if (uriPath.contains("edit") || uriPath.contains("delete") || uriPath.contains("create")) {
            String[] pathTokens = uriPath.split("/");
            Long restaurantId;

            if (uriPath.contains("edit") || uriPath.contains("delete")) { //  /products/2/delete/1
                restaurantId = Long.parseLong(pathTokens[pathTokens.length - 3]);
            } else { // /products/2/create
                restaurantId = Long.parseLong(pathTokens[pathTokens.length - 2]);
            }

            UserData userData = (UserData) request.getSession().getAttribute(UserData.NAME);

            String action;
            if (uriPath.contains("edit")) {
                action = "edit";
            } else if (uriPath.contains("delete")) {
                action = "delete";
            } else {
                action = "create";
            }

            if (!userData.getManagedRestaurants().contains(restaurantId)) {
                throw new Unauthorized(String.format("You can't perform action \"%s\" on a product from a restaurant you don't own!", action));
            }
        }

        return true;
    }
}
