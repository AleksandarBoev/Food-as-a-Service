package tu.faas.domain.models.binding;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class RestaurantCreateBindingModel {
    public static final String RESTAURANT_NAME_NULL_OR_EMPTY =
            "Restaurant name can\'t be null or empty!";
    public static final int RESTAURANT_NAME_MIN_LETTERS = 3;
    public static final int RESTAURANT_NAME_MAX_LETTERS = 50;
    public static final String RESTAURANT_NAME_LENGTH_ERROR_MESSAGE =
            "Restaurant name should be between " + RESTAURANT_NAME_MIN_LETTERS +
            " and " + RESTAURANT_NAME_MAX_LETTERS + " letters long!";

    public static final String RESTAURANT_NAME_FORMAT_ERROR_MESSAGE =
            "Restaurant name should start with capital letter and contain only letters!";

    private String name;
    private String imageUrl;
    private String description;

    @NotEmpty(message = RESTAURANT_NAME_NULL_OR_EMPTY)
    @Pattern(regexp = "^[A-Z][A-z]+( [A-z]+)*$", message = RESTAURANT_NAME_FORMAT_ERROR_MESSAGE)
    @Size(
            min = RESTAURANT_NAME_MIN_LETTERS,
            max = RESTAURANT_NAME_MAX_LETTERS,
            message = RESTAURANT_NAME_LENGTH_ERROR_MESSAGE)
    /*
    Pokriva sluchaite za:
    Bulgarska Kuhnq
    Bulgarska
    Bulgarska kuhnq
    Bulgarskata kuhnq na Jivko
     */
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
