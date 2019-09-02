package tu.faas.domain.exceptions;

public class NoSuchProduct extends RuntimeException {
    public static final String PRODUCT_NOT_FOUND_MESSAGE =
            "Product does not exist";

    public static final String PRODUCT_DOES_NOT_EXIST_CHECKOUT =
            "One of the products does not exist anymore! We apologise for the inconvenience.";

    public NoSuchProduct() {
        super(PRODUCT_NOT_FOUND_MESSAGE);
    }

    public NoSuchProduct(String message) {
        super(message);
    }
}
