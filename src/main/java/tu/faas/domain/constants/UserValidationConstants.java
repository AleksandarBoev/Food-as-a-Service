package tu.faas.domain.constants;

public class UserValidationConstants {
    public static final int NAME_MIN_LENGTH = 3;
    public static final int NAME_MAX_LENGTH = 15;
    public static final String NAME_LENGTH_ERROR_MESSAGE =
            "Name length should be between " + NAME_MIN_LENGTH + " and " + NAME_MAX_LENGTH + " letters long!";
    public static final String NAME_FORMAT_ERROR_MESSAGE = "Name should contain only letters and numbers!";

    public static final String USER_NAME_REGEX = "[A-z0-9]+";

    public static final String NAME_NULL_MESSAGE = "Username can't be null!";

    public static final String EMAIL_REGEX = "^[A-z0-9]+@[a-z]+\\.[a-z]+$";
    public static final String EMAIL_FORMAT_ERROR_MESSAGE =
            "Email should start with letters or digits, have a \"@\", continue with lowercase letters, " +
                    "have a dot and finally have lowercase letters.";
    public static final String EMAIL_NULL_MESSAGE = "Email can't be null!";

    public static final int PASSWORD_MIN_LENGTH = 3;
    public static final String PASSWORD_LENGTH_MESSAGE = "Password should have at least 3 characters!";
}
