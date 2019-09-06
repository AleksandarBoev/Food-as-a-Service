package tu.faas.domain.exceptions;

public class UsernameAlreadyExists extends FaasException{
    public static final String USERNAME_ALREADY_EXISTS = "User with this username already exists!";

    public UsernameAlreadyExists() {
        super(USERNAME_ALREADY_EXISTS);
    }
}
