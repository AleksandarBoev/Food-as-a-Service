package tu.faas.domain.exceptions;

public class NotAnAdmin extends FaasException {
    public static String MESSAGE = "Not an admin!";

    public NotAnAdmin() {
        super(MESSAGE);
    }
}
