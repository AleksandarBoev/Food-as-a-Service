package tu.faas.domain.exceptions;

public class EmailAlreadyExists extends FaasException {
    public static final String EMAIL_ALREADY_EXISTS = "User with this email already exists!";

    public EmailAlreadyExists() {
        super(EMAIL_ALREADY_EXISTS);
    }
}
