package com.instasimulator.common.util;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

/**
 * Date/time helpers used across logging and reports.
 */
public final class DateUtils {

    private static final DateTimeFormatter ISO =
            DateTimeFormatter.ISO_OFFSET_DATE_TIME.withZone(ZoneOffset.UTC);

    private DateUtils() {
    }

    public static String nowIso() {
        return ISO.format(Instant.now());
    }

    public static String format(Instant instant) {
        return ISO.format(instant);
    }

    public static long elapsedMs(Instant start) {
        return Instant.now().toEpochMilli() - start.toEpochMilli();
    }
}
