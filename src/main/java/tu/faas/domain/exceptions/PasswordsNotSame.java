package tu.faas.domain.exceptions;

public class PasswordsNotSame extends RuntimeException {
    public static String PASSWORDS_DONT_MATCH =
            "Password and re-password no not match!";

    public PasswordsNotSame() {
        super(PASSWORDS_DONT_MATCH);
    }
}
