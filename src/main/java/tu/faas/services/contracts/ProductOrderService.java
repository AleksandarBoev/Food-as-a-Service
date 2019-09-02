package tu.faas.services.contracts;

import tu.faas.domain.models.view.ProductOrderRestaurantSalesViewModel;

import java.time.LocalDate;
import java.util.List;

public interface ProductOrderService {
    /**
     * Gets all productOrder view models, ordered by date descending
     */
    List<ProductOrderRestaurantSalesViewModel> getProductOrdersByRestaurantIdOrderByNewest(Long restaurantId);

    /**
     * Gets all productOrder view models after given date, ordered by date descending
     */
    List<ProductOrderRestaurantSalesViewModel> getProductOrdersByRestaurantIdAfterDateOrderByNewest(Long restaurantId, LocalDate afterDate);

    /**
     * Gets all productOrder view models before given date, ordered by date descending
     */
    List<ProductOrderRestaurantSalesViewModel> getProductOrdersByRestaurantIdBeforeDateOrderByNewest(Long restaurantId, LocalDate  date);

    /**
     * Gets all productOrder view models between two dates, ordered by date descending
     */
    List<ProductOrderRestaurantSalesViewModel> getProductOrdersByRestaurantIdBetweenDatesOrderByNewest(
            Long restaurantId, LocalDate date1, LocalDate date2);
}
