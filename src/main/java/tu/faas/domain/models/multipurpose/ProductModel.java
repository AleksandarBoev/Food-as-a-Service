package tu.faas.domain.models.multipurpose;

import tu.faas.domain.models.binding.ProductCreateBindingModel;

import java.math.BigDecimal;

/**
 * Used as a view model for displaying information in the edit and delete pages.
 * Also as a binding model for the edit page. Extends the createBindingModel
 * as well as its validations.
 */
public class ProductModel extends ProductCreateBindingModel {
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
