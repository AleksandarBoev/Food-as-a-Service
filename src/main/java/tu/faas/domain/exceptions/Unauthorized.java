package tu.faas.domain.exceptions;

public class Unauthorized extends RuntimeException {
    public static final String MESSAGE = "You do not have access to this page";

    public Unauthorized() {
        super(MESSAGE);
    }

    public Unauthorized(String message) {
        super(message);
    }
}
