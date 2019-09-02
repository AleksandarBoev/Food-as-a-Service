package tu.faas;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.*;

@RunWith(SpringRunner.class)
public class FaasApplicationTests {

    @Test
    public void printStuff() {
        LocalDate localDate = LocalDate.parse("");
        System.out.println(localDate);
    }

    private void throwException() {
        int b = 5;
    }

}
