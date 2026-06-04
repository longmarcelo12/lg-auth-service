package com.example.lgauthservice.shared.utils;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;

import static com.example.lgauthservice.shared.utils.DateUtils.*;

public class TimeUtils {

    public static Timestamp timeStampNow() {
        return Timestamp.from(instantNow());
    }

    public static Instant instantNow() {
        return ZonedDateTime.now(SYSTEM_DEFAULT_ZONE).toInstant().truncatedTo(ChronoUnit.SECONDS);
    }

    // ====================== START / END OF DAY ======================
    public static Instant startOfDay(Instant instant) {
        if (instant == null) return null;
        return instant.atZone(SYSTEM_DEFAULT_ZONE).toLocalDate().atStartOfDay(SYSTEM_DEFAULT_ZONE).toInstant();
    }

    public static Instant endOfDay(Instant instant) {
        if (instant == null) return null;
        return instant.atZone(SYSTEM_DEFAULT_ZONE)
                .toLocalDate()
                .plusDays(1)
                .atStartOfDay(SYSTEM_DEFAULT_ZONE)
                .minusNanos(1)
                .toInstant();
    }
}
