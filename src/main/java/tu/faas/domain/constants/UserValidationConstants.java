package tu.faas.domain.constants;

public class UserValidationConstants {
    public static final int NAME_MIN_LENGTH = 3;
    public static final int NAME_MAX_LENGTH = 15;
    public static final String NAME_LENGTH_ERROR_MESSAGE =
            "Name length should be between " + NAME_MIN_LENGTH + " and " + NAME_MAX_LENGTH + " letters long!";
    public static final String NAME_FORMAT_ERROR_MESSAGE = "Name should contain only letters and numbers!";

    public static final String USER_NAME_REGEX = "[A-z0-9]+";
}
