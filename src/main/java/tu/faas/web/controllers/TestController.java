package tu.faas.web.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/test")
public class TestController {
    class SomeClass {
        int number;
        String name;

        public SomeClass(int number, String name) {
            this.number = number;
            this.name = name;
        }

        public int getNumber() {
            return number;
        }

        public void setNumber(int number) {
            this.number = number;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    @GetMapping
    public ModelAndView getTestPage(ModelAndView modelAndView) {
        List<SomeClass> someCLasses = new ArrayList<>();
        SomeClass someClass = new SomeClass(1, "Name 1");
        SomeClass someClass2 = new SomeClass(2, "Name Two");
        SomeClass someClass3 = new SomeClass(3, "Name Three");
        SomeClass someClass4 = new SomeClass(4, "Name 4");

        someCLasses.add(someClass);
        someCLasses.add(someClass2);
        someCLasses.add(someClass3);
        someCLasses.add(someClass4);

        modelAndView.addObject("someClasses", someCLasses);

        modelAndView.setViewName("test/fragments-test.html");
        return modelAndView;
    }
}
