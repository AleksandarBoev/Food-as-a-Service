package tu.faas.services.contracts;

import tu.faas.domain.models.binding.OrderBindingModel;
import tu.faas.domain.models.view.OrderHistoryViewModel;
import tu.faas.domain.models.view.ShoppingCartViewModel;

import java.util.List;
import java.util.Map;

public interface OrderService {
    ShoppingCartViewModel getShoppingCartViewModel(Map<Long, Integer> productIdCount);

    void makeOrder(OrderBindingModel orderBindingModel);

    List<OrderHistoryViewModel> getOrderHistoryViewModelsByUserIdOrderedByDateNewest(Long userId);

}
