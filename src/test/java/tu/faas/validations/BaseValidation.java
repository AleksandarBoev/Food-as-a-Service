package tu.faas.validations;

import org.junit.Assert;

import javax.validation.Validation;
import javax.validation.Validator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public abstract class BaseValidation {
    private Validator validator;

    protected BaseValidation() {
        this.validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    protected Validator getValidator() {
        return validator;
    }

    private List<String> getErrorMessages(Object object) {
        return this.validator.validate(object)
                .stream()
                .map(e -> e.getMessage())
                .collect(Collectors.toList());
    }

    private void assertOnlyOneErrorThrown(Object object, List<String> errorMessages) {
        Assert.assertEquals(String.format("Expected only 1 error, got %d! Messages are: %s", errorMessages.size(), String.join(" | ", errorMessages)),
                1, errorMessages.size());
    }

    private void assertCorrectMessageThrown(String expectedErrorMessage, String actualErrorMessage) {
        Assert.assertEquals(String.format("Wrong validation exception message thrown! %nExpected %s %nGot %s", expectedErrorMessage, actualErrorMessage), expectedErrorMessage, actualErrorMessage);
    }

    protected void assertOnlyOneCorrectErrorMessageThrown(Object object, String expectedErrorMessage) {
        List<String> errorMessages = getErrorMessages(object);
        assertOnlyOneErrorThrown(object, errorMessages);
        assertCorrectMessageThrown(expectedErrorMessage, errorMessages.get(0));
    }
}
