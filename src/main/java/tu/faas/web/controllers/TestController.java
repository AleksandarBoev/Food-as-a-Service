package tu.faas.web.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/test")
public class TestController {
    @GetMapping
    public ModelAndView getTestPage(ModelAndView modelAndView,
                                    @RequestParam(name = "date", required = false)String date) {
        System.out.println(date);
        if (date != null && !"".equals(date)) {
            LocalDate localDate = LocalDate.parse(date);
            System.out.println(localDate);
        }
        modelAndView.setViewName("testing-something.html");
        return modelAndView;
    }
}
