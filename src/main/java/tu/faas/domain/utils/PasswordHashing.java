package tu.faas.domain.utils;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordHashing {
    /*
    https://security.stackexchange.com/questions/17207/recommended-of-rounds-for-bcrypt
    So basically, the higher the value of logRounds, the longer it will take to hash a password.
    Sounds bad for the user experience, but also very bad for hackers trying to brute force a password.
    A user can suffer 200 more ms. Will probably not even notice it. But a hacker trying millions or
    billions of combinations of passwords to guess a certain users password will take A LOT LONGER.
    Maybe even days/weeks longer than usual.

    Different servers take different time to hash passwords. A super strong machine will hash it in a
    blink of an eye. So if the app is running on a super PC then "logRounds" should have a higher value.
     */
    private static final int logRounds = 10; // the higher the value, the more time it takes to hash a string

    /**
     * Do NOT try to hash a password and check if it equals an already hashed password.
     * This will not work! Instead use "checkPassword"
     */
    public static String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt(logRounds));
    }

    /**
     * Checks if provided password matches its hashed version.
     */
    public static boolean checkPassword(String password, String passwordHash) {
        return BCrypt.checkpw(password, passwordHash);
    }
}
