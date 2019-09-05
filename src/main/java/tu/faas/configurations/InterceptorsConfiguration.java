package tu.faas.configurations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import tu.faas.web.interceptors.*;

@Configuration
public class InterceptorsConfiguration implements WebMvcConfigurer {
    private GlobalInterceptor globalInterceptor;
    private GuestPagesInterceptor guestPagesInterceptor;
    private UserPagesInterceptor userPagesInterceptor;
    private RestaurantPagesInterceptor restaurantPagesInterceptor;
    private ProductPagesInterceptor productPagesInterceptor;
    private AdminPagesInterceptor adminPagesInterceptor;

    @Autowired
    public InterceptorsConfiguration(GlobalInterceptor globalInterceptor,
                                     GuestPagesInterceptor guestPagesInterceptor,
                                     UserPagesInterceptor userPagesInterceptor,
                                     RestaurantPagesInterceptor restaurantPagesInterceptor,
                                     ProductPagesInterceptor productPagesInterceptor,
                                     AdminPagesInterceptor adminPagesInterceptor) {
        this.globalInterceptor = globalInterceptor;
        this.guestPagesInterceptor = guestPagesInterceptor;
        this.userPagesInterceptor = userPagesInterceptor;
        this.restaurantPagesInterceptor = restaurantPagesInterceptor;
        this.productPagesInterceptor = productPagesInterceptor;
        this.adminPagesInterceptor = adminPagesInterceptor;
    }

    /*
        ? matches one character
        * matches zero or more characters
        ** matches zero or more 'directories' in a path
    */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(globalInterceptor); //all paths
        registry.addInterceptor(guestPagesInterceptor).addPathPatterns("/login", "/register");
        registry.addInterceptor(userPagesInterceptor).addPathPatterns("/user/**", "/order/**");
        registry.addInterceptor(restaurantPagesInterceptor).addPathPatterns(
                "/restaurants/my-restaurants",
                "/restaurants/create",
                "/restaurants/edit/**",
                "/restaurants/delete/**",
                "/restaurants/sales/**");
        registry.addInterceptor(productPagesInterceptor).addPathPatterns("/products/**");
        registry.addInterceptor(adminPagesInterceptor).addPathPatterns("/admin/**");
    }
}
