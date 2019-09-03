package tu.faas.domain.beans;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class RegionDateTime {
    private static final ZoneId BULGARIA_ZONE_ID = ZoneId.of("Europe/Helsinki");

    public LocalDate getPresentDate() {
        return LocalDate.now(BULGARIA_ZONE_ID);
    }

    public LocalDateTime getPresentDateTime() {
        return LocalDateTime.now(BULGARIA_ZONE_ID);
    }
}
