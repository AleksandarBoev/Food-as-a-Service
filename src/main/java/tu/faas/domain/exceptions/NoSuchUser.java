package tu.faas.domain.exceptions;

public class NoSuchUser extends FaasException {
    public static final String NO_SUCH_USER = "Wrong username or password!";

    public NoSuchUser() {
        super(NO_SUCH_USER);
    }
}
