package tu.faas.domain.exceptions;

public class SameEmail extends FaasException {
    public static final String MESSAGE = "You entered the same email!";

    public SameEmail() {
        super(MESSAGE);
    }
}
