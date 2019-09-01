package tu.faas.domain.exceptions;

public class WrongPassword extends RuntimeException {
    public static final String MESSAGE = "Wrong password!";

    public WrongPassword() {
        super(MESSAGE);
    }
}
