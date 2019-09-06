package tu.faas.domain.exceptions;

public class SameName extends FaasException {
    public static final String MESSAGE = "You entered the same name!";

    public SameName() {
        super(MESSAGE);
    }
}
