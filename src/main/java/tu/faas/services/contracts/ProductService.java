package tu.faas.services.contracts;

import tu.faas.domain.models.view.ProductHomeViewModel;

import java.util.List;

public interface ProductService {
    List<ProductHomeViewModel> getNewestProductHomeViewModels(Integer count);

}
