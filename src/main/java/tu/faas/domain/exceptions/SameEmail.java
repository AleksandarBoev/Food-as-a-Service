package tu.faas.domain.exceptions;

public class SameEmail extends RuntimeException {
    public static final String MESSAGE = "You entered the same email!";

    public SameEmail() {
        super(MESSAGE);
    }
}
