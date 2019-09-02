package tu.faas.domain.models.view;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class OrderHistoryViewModel {
    private LocalDateTime dateTimeOfOrder;
    private List<ProductOrderUserHistoryViewModel>  productOrders;
    private BigDecimal totalPrice;
    private String billingType;
    private String address;

    public LocalDateTime getDateTimeOfOrder() {
        return dateTimeOfOrder;
    }

    public void setDateTimeOfOrder(LocalDateTime dateTimeOfOrder) {
        this.dateTimeOfOrder = dateTimeOfOrder;
    }

    public List<ProductOrderUserHistoryViewModel> getProductOrders() {
        return productOrders;
    }

    public void setProductOrders(List<ProductOrderUserHistoryViewModel> productOrders) {
        this.productOrders = productOrders;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getBillingType() {
        return billingType;
    }

    public void setBillingType(String billingType) {
        this.billingType = billingType;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
