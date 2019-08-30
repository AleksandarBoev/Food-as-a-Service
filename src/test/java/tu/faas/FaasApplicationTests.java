package tu.faas;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class FaasApplicationTests {

    @Test
    public void printStuff() {
        try {
            int a = 5;
            throwException();
        } catch (RuntimeException re) {
            System.out.println(re.getMessage());
        } finally {
            System.out.println("This is the finally message!");
        }
        System.out.println("End of method!");
    }

    private void throwException() {
        int b = 5;
    }

}
