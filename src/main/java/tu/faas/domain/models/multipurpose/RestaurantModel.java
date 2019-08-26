package tu.faas.domain.models.multipurpose;

import tu.faas.domain.models.binding.RestaurantCreateBindingModel;

public class RestaurantModel extends RestaurantCreateBindingModel {
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
