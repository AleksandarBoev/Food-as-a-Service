package tu.faas.domain.models.multipurpose;

import tu.faas.domain.models.binding.ProductCreateBindingModel;

import java.math.BigDecimal;

public class ProductEditModel extends ProductCreateBindingModel {
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
