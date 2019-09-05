package tu.faas.web.interceptors;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import tu.faas.domain.constants.RoleConstants;
import tu.faas.domain.exceptions.Unauthorized;
import tu.faas.web.session.UserData;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class AdminPagesInterceptor extends HandlerInterceptorAdapter {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        UserData userData = (UserData) request.getSession().getAttribute(UserData.NAME);

        if (!userData.getRoles().contains(RoleConstants.ROLE_ROOT_ADMIN)) {
            throw new Unauthorized("You are no authorized to access admin pages.");
        }

        return true;
    }
}
