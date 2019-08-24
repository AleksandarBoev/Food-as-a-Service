package tu.faas.configurations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import tu.faas.web.interceptors.GlobalInterceptor;
import tu.faas.web.interceptors.GuestPagesInterceptor;
import tu.faas.web.interceptors.UserPagesInterceptor;

@Configuration
public class InterceptorsConfiguration implements WebMvcConfigurer {
    //TODO create the "Profile" page and "Logout" in the navbar. Should be seen only by logged in users
    //TODO fill "Login" post logic. Session stuff should be in the service? Can it be there?
    //TODO try to make the controller exception handler to catch the custom exception,
    //which will be thrown from the service. And try to NOT make the page reload, just add
    //a regular looking red-text mistake "Name has been taken"

    private GuestPagesInterceptor guestPagesInterceptor;
    private UserPagesInterceptor userPagesInterceptor;
    private GlobalInterceptor globalInterceptor;

    @Autowired
    public InterceptorsConfiguration(GuestPagesInterceptor guestPagesInterceptor, UserPagesInterceptor userPagesInterceptor, GlobalInterceptor globalInterceptor) {
        this.guestPagesInterceptor = guestPagesInterceptor;
        this.userPagesInterceptor = userPagesInterceptor;
        this.globalInterceptor = globalInterceptor;
    }

    /*
        ? matches one character
        * matches zero or more characters
        ** matches zero or more 'directories' in a path
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //idea is to check certain privileges for certain pages. For guest-stuff i want only guests
        //to access it. So I check that page for guest privileges.
        registry.addInterceptor(guestPagesInterceptor).addPathPatterns("/guest-stuff");
        registry.addInterceptor(userPagesInterceptor).addPathPatterns("/important-stuff/**");
        registry.addInterceptor(globalInterceptor);
    }
}
