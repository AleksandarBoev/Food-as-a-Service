package tu.faas.configurations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import tu.faas.web.interceptors.GlobalInterceptor;
import tu.faas.web.interceptors.GuestPagesInterceptor;
import tu.faas.web.interceptors.ManagerPagesInterceptor;
import tu.faas.web.interceptors.UserPagesInterceptor;

@Configuration
public class InterceptorsConfiguration implements WebMvcConfigurer {
    private GuestPagesInterceptor guestPagesInterceptor;
    private UserPagesInterceptor userPagesInterceptor;
    private ManagerPagesInterceptor managerPagesInterceptor;
    private GlobalInterceptor globalInterceptor;

    @Autowired
    public InterceptorsConfiguration(GuestPagesInterceptor guestPagesInterceptor, UserPagesInterceptor userPagesInterceptor, ManagerPagesInterceptor managerPagesInterceptor, GlobalInterceptor globalInterceptor) {
        this.guestPagesInterceptor = guestPagesInterceptor;
        this.userPagesInterceptor = userPagesInterceptor;
        this.managerPagesInterceptor = managerPagesInterceptor;
        this.globalInterceptor = globalInterceptor;
    }

    /*
        ? matches one character
        * matches zero or more characters
        ** matches zero or more 'directories' in a path
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(guestPagesInterceptor).addPathPatterns("/guest-stuff");
        registry.addInterceptor(userPagesInterceptor).addPathPatterns("/user/**", "/order/**");
        registry.addInterceptor(managerPagesInterceptor).addPathPatterns(
                "/restaurants/my-restaurants",
                "/restaurants/create",
                "/restaurants/edit/**",
                "/restaurants/delete/**",
                "/restaurants/sales/**",
                "/products/*/create",
                "/products/edit/**",
                "/products/delete/**");
        registry.addInterceptor(globalInterceptor);
    }
}
