package tu.faas.web.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/test")
public class TestController {
    @GetMapping
    public ModelAndView getTestPage(ModelAndView modelAndView,
                                    @RequestParam(name = "search", required = false) String search,
                                    @RequestParam(name = "option", required = false) String option) {
        System.out.println(search);
        System.out.println(option);
        //search - typing nothing = null
        //search - typing something and deleting it = empty string
        //option - choosing nothing = null
        //can't choose sortby

        modelAndView.setViewName("testing-something.html");
        return modelAndView;
    }
}
