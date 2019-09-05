package tu.faas.web.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import tu.faas.domain.SessionClass;
import tu.faas.domain.models.binding.AdminAction;

import javax.servlet.http.HttpSession;
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

    @GetMapping("/data")
    public String getPage() {
        return "testingagain.html";
    }

    @PostMapping("/data")
    @ResponseBody
    public ResponseEntity orderProduct(@RequestBody AdminAction requestBodyAdminAction) {
        System.out.println(requestBodyAdminAction.getAction());
        System.out.println(requestBodyAdminAction.getPassword());
        System.out.println(requestBodyAdminAction.getUserId());

        Long userId = requestBodyAdminAction.getUserId();
        if (userId == 1L) {
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        } else if (userId == 2L){
            return new ResponseEntity(HttpStatus.OK);
        } else {
            return new ResponseEntity(HttpStatus.CONFLICT);
        }
    }

    @GetMapping("/session")
    public ModelAndView getSessionPage(HttpSession session,
                                       ModelAndView modelAndView) {
        SessionClass sessionClass = (SessionClass)session.getAttribute("sessionClass");

        modelAndView.setViewName("test/session.html");
        return modelAndView;
    }
}
