package com.hxy.recipe.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.concurrent.TimeUnit;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class TimeUtil {

    private static final ZoneOffset CHINA_TIME_ZONE = ZoneOffset.ofHours(8);

    public static long nano2Millis(long nanoseconds) {
        return TimeUnit.NANOSECONDS.toMillis(nanoseconds);
    }

    public static long nano2Seconds(long nanoseconds) {
        return TimeUnit.NANOSECONDS.toSeconds(nanoseconds);
    }

    public static long atStartOfDay(LocalDate localDate) {
        return localDate.atStartOfDay(CHINA_TIME_ZONE).toInstant().toEpochMilli();
    }

    public static LocalDate timestampInMillis2LocalDate(long timestampInMillis) {
        return toZoneDateTime(timestampInMillis).toLocalDate();
    }

    public static LocalDateTime timestampInMillis2LocalDateTime(long timestampInMillis) {
        return toZoneDateTime(timestampInMillis).toLocalDateTime();
    }

    private static ZonedDateTime toZoneDateTime(long timestampInMillis) {
        return Instant.ofEpochMilli(timestampInMillis).atZone(CHINA_TIME_ZONE);
    }

}
