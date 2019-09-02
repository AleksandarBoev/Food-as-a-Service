package tu.faas.domain.lib;

public class StringManipulation {
    public static String cropString(String string, int maxLength, String endingReplacement) {
        if (string.length() > maxLength) {
            string = string.substring(0, maxLength + 1);
            string += endingReplacement;
        }

        return string;
    }
}
