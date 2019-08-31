package tu.faas.web.exception_handlers;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import tu.faas.domain.exceptions.NoSuchRestaurant;

@ControllerAdvice
public class GlobalExceptionHandler {
    //handle ALL exceptions. Throw out a generic message.
    //TODO attach a logger to this? Oor just save error to db.
    @ExceptionHandler(Exception.class)
    public ModelAndView handleAnyError(Exception e) {
        e.printStackTrace();
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("errorMessage", "Something went wrong.");
        modelAndView.setViewName("error.html");

        return modelAndView;
    }

    @ExceptionHandler(NoSuchRestaurant.class)
    public ModelAndView handleDatabaseErrors(NoSuchRestaurant nsr) {
        ModelAndView modelAndView = new ModelAndView("error.html");
        modelAndView.addObject("errorMessage", nsr.getMessage());

        return modelAndView;
    }

}
