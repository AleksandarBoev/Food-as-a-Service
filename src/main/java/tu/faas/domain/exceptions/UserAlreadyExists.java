package tu.faas.domain.exceptions;

public class UserAlreadyExists extends RuntimeException{
    public static final String USERNAME_ALREADY_EXISTS = "User with this username already exists!";
    public static final String EMAIL_ALREADY_EXISTS = "User with this email already exists!";

    public UserAlreadyExists(String message) {
        super(message);
    }
}
