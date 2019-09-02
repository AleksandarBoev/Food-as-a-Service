package tu.faas.domain.lib;

import java.math.BigDecimal;
import java.util.Collection;

public class Calculation {
    public static BigDecimal getSum(Collection<BigDecimal> bigDecimalCollection) {
        BigDecimal totalSum = new BigDecimal("0");
        for (BigDecimal current : bigDecimalCollection) {
            totalSum = totalSum.add(current);
        }

        return totalSum;
    }
}
