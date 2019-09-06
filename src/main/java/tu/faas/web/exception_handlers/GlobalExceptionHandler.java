package tu.faas.web.exception_handlers;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import tu.faas.domain.exceptions.FaasException;
import tu.faas.domain.exceptions.NoSuchProduct;
import tu.faas.domain.exceptions.NoSuchRestaurant;
import tu.faas.domain.exceptions.Unauthorized;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    public ModelAndView handleAnyError(Exception e) {
        e.printStackTrace();
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("errorMessage", "Something went wrong.");
        modelAndView.setViewName("error.html");

        return modelAndView;
    }

    @ExceptionHandler(FaasException.class)
    public ModelAndView handleDatabaseErrors(FaasException fe) {
        ModelAndView modelAndView = new ModelAndView("error.html");
        modelAndView.addObject("errorMessage", fe.getMessage());

        return modelAndView;
    }

}
