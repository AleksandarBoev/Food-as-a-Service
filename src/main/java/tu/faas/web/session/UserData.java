package tu.faas.web.session;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class UserData {
    //less likely for making a typo when getting the object from session
    public static final String NAME = "userData";

    private Long id;
    private Set<String> roles;
    private Map<Long, Integer> shoppingCart;
    private Set<Long> managedRestaurants;

    public UserData() {
        id = 0L; // anonymous user
        roles = new HashSet<>();
        shoppingCart = new LinkedHashMap<>();
        managedRestaurants = new HashSet<>();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Set<String> getRoles() {
        return roles;
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }

    public Map<Long, Integer> getShoppingCart() {
        return shoppingCart;
    }

    public void setShoppingCart(Map<Long, Integer> shoppingCart) {
        this.shoppingCart = shoppingCart;
    }

    public Set<Long> getManagedRestaurants() {
        return managedRestaurants;
    }

    public void setManagedRestaurants(Set<Long> managedRestaurants) {
        this.managedRestaurants = managedRestaurants;
    }

    public int getShoppingCartItemsCount() {
        int result = 0;
        for (Integer count : shoppingCart.values()) {
            result += count;
        }

        return result;
    }
}
