package tu.faas.domain.entities;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "products")
public class Product extends BaseEntity {
    private String name;
    private BigDecimal price;
    private Integer weightInGrams;
    private String imageUrl;
    private String description;
    private Restaurant restaurant;
    private List<ProductOrder> productOrders;

    @Column(nullable = false, columnDefinition = "VARCHAR(50)")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(nullable = false, columnDefinition = "DECIMAL(7,2)")
    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    @Column(name = "weight_in_grams") //, columnDefinition = "INT(5)") //doesn't work for some reason
    public Integer getWeightInGrams() {
        return weightInGrams;
    }

    public void setWeightInGrams(Integer weightInGrams) {
        this.weightInGrams = weightInGrams;
    }

    @Column(name = "image_url")
    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Column
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @ManyToOne
    @JoinColumn(name = "restaurant_id", referencedColumnName = "id")
    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    public List<ProductOrder> getProductOrders() {
        return productOrders;
    }

    public void setProductOrders(List<ProductOrder> productOrders) {
        this.productOrders = productOrders;
    }
}
