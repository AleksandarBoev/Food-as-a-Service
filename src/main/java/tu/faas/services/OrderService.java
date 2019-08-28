package tu.faas.services;

import tu.faas.domain.models.view.ShoppingCartViewModel;

import java.util.Map;

public interface OrderService {
    ShoppingCartViewModel getShoppingCartViewModel(Map<Long, Integer> productIdCount);
}
