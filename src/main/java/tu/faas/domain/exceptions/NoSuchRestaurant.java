package tu.faas.domain.exceptions;

public class NoSuchRestaurant extends RuntimeException {
    public static final String RESTAURANT_NOT_FOUND_MESSAGE = "Restaurant not found!";

    public NoSuchRestaurant() {
        super(RESTAURANT_NOT_FOUND_MESSAGE);
    }

    public NoSuchRestaurant(String message) {
        super(message);
    }
}
