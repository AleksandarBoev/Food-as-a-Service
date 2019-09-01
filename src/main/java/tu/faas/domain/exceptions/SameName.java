package tu.faas.domain.exceptions;

public class SameName extends RuntimeException {
    public static final String MESSAGE = "You entered the same name!";

    public SameName() {
        super(MESSAGE);
    }
}
