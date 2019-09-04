package tu.faas.web.interceptors;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import tu.faas.domain.constants.RoleConstants;
import tu.faas.domain.constants.SessionConstants;
import tu.faas.domain.exceptions.Unauthorized;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Set;

@Component
public class UserPagesInterceptor extends HandlerInterceptorAdapter {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!((Set<String>)request.getSession().getAttribute(SessionConstants.ROLES)).contains(RoleConstants.ROLE_USER)) {
            throw new Unauthorized();
        }
        return true;
    }
}
