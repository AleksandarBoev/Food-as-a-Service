package tu.faas.domain.exceptions;

public class PasswordsNotSame extends FaasException {
    public static String PASSWORDS_DONT_MATCH =
            "Password and re-password do not match!";

    public static String NEW_PASSWORD_RE_PASSWORD_DONT_MATCH =
            "New password and re-password do not match!";

    public PasswordsNotSame() {
        super(PASSWORDS_DONT_MATCH);
    }

    public PasswordsNotSame(String message) {
        super(message);
    }
}
