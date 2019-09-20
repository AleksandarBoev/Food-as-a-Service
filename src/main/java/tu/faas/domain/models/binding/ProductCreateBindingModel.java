package tu.faas.domain.models.binding;

import javax.validation.constraints.*;
import java.math.BigDecimal;

public class ProductCreateBindingModel {
    public static final String PRODUCT_NAME_INVALID_FORMAT_MESSAGE =
            "Product name should start with capital latin letter and be followed with " +
                    "latin letters, digits and spaces only!";
    public static final int PRODUCT_NAME_MIN_LENGTH = 3;
    public static final int PRODUCT_NAME_MAX_LENGTH = 40;
    public static final String PRODUCT_NAME_INVALID_LENGTH_MESSAGE =
            "Product name should be between " + PRODUCT_NAME_MIN_LENGTH +
                    " and " + PRODUCT_NAME_MAX_LENGTH + " characters long!";

    public static final String PRODUCT_PRICE_NOT_POSITIVE_MESSAGE =
            "Product price should be a positive number!";
    public static final String PRODUCT_PRICE_NULL_MESSAGE =
            "Product price can\'t be null!";

    public static final String PRODUCT_MAX_PRICE = "99999";
    public static final String PRODUCT_MAX_PRICE_ERROR_MESSAGE =
            "Product price can\'t exceed " + PRODUCT_MAX_PRICE + "!";

    public static final String PRODUCT_WEIGHT_NOT_POSITIVE_MESSAGE =
            "Product weight should be a positive number!";


    private String name;
    private BigDecimal price;
    private Integer weightInGrams;
    private String imageUrl;
    private String description;

    @Pattern(regexp = "^[A-Z][ A-z0-9]+$",
            message = PRODUCT_NAME_INVALID_FORMAT_MESSAGE)
    @Size(min = PRODUCT_NAME_MIN_LENGTH,
            max = PRODUCT_NAME_MAX_LENGTH,
            message = PRODUCT_NAME_INVALID_LENGTH_MESSAGE)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Positive(message = PRODUCT_PRICE_NOT_POSITIVE_MESSAGE)
    @DecimalMax(value = PRODUCT_MAX_PRICE, message = PRODUCT_MAX_PRICE_ERROR_MESSAGE)
    @NotNull(message = PRODUCT_PRICE_NULL_MESSAGE)
    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    @Positive(message = PRODUCT_WEIGHT_NOT_POSITIVE_MESSAGE)
    public Integer getWeightInGrams() {
        return weightInGrams;
    }

    public void setWeightInGrams(Integer weightInGrams) {
        this.weightInGrams = weightInGrams;
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
