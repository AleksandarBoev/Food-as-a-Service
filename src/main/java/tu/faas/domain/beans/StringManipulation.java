package tu.faas.domain.beans;

public class StringManipulation {
    public String cropString(String string, int maxLength, String endingReplacement) {
        if (string.length() > maxLength) {
            string = string.substring(0, maxLength + 1);
            string += endingReplacement;
        }

        return string;
    }
}
