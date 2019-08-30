package tu.faas.domain.models.binding;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public class OrderBindingModel {
    private Long customerId;
    private Map<Long, Integer> productIdQuantities;
    private String billingType;
    private String address;

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public Map<Long, Integer> getProductIdQuantities() {
        return productIdQuantities;
    }

    public void setProductIdQuantities(Map<Long, Integer> productIdQuantities) {
        this.productIdQuantities = productIdQuantities;
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
