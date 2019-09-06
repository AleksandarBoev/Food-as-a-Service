package tu.faas.domain.exceptions;

public class SamePassword extends FaasException {
    public static String MESSAGE = "Old password and new password match!";

    public SamePassword() {
        super(MESSAGE);
    }
}
