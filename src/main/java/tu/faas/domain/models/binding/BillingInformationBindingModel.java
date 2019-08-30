package tu.faas.domain.models.binding;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

public class BillingInformationBindingModel {
    public static final String ADDRESS_FORMAT_MESSAGE =
            "Address should contain only letters, digits, spaces and dots and be at least 3 characters long!";
    public static final String BILLING_TYPE_MESSAGE =
            "Please select billing type!";
    public static final String CREDIT_CARD_FORMAT_MESSAGE =
            "Credit card should contain 16 digits. If there is separation between digits, it has to be with either spaces or dashes.";
    public static final String SECURITY_CODE_FORMAT_MESSAGE =
            "Security code should be a 3 digit code!";

    private String address;
    private String billingType;
    private String creditCardNumber;
    private String securityCode;

    @Pattern(regexp = "^[A-z0-9\\. ]{3,}$", message = ADDRESS_FORMAT_MESSAGE)
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @NotNull(message = BILLING_TYPE_MESSAGE)
    public String getBillingType() {
        return billingType;
    }

    public void setBillingType(String billingType) {
        this.billingType = billingType;
    }

    @Pattern(regexp = "^[0-9]{4}(-| |)[0-9]{4}\\1[0-9]{4}\\1[0-9]{4}$", message = CREDIT_CARD_FORMAT_MESSAGE)
    public String getCreditCardNumber() {
        return creditCardNumber;
    }

    public void setCreditCardNumber(String creditCardNumber) {
        this.creditCardNumber = creditCardNumber;
    }

    @Pattern(regexp = "^[0-9]{3}$", message = SECURITY_CODE_FORMAT_MESSAGE)
    public String getSecurityCode() {
        return securityCode;
    }

    public void setSecurityCode(String securityCode) {
        this.securityCode = securityCode;
    }
}
