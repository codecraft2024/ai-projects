package com.instasimulator.common.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Generates realistic random test data for simulations.
 */
public final class RandomDataGenerator {

    private static final List<String> FIRST_NAMES = List.of(
            "Alex", "Sam", "Jordan", "Taylor", "Morgan", "Casey", "Riley", "Avery");
    private static final List<String> LAST_NAMES = List.of(
            "Smith", "Johnson", "Williams", "Brown", "Jones", "Garcia", "Miller", "Davis");

    private RandomDataGenerator() {
    }

    public static String username() {
        return pick(FIRST_NAMES).toLowerCase() + "." + pick(LAST_NAMES).toLowerCase()
                + ThreadLocalRandom.current().nextInt(10, 99);
    }

    public static String email() {
        return username() + "@example.com";
    }

    public static String phone() {
        return "+1555" + ThreadLocalRandom.current().nextInt(1000000, 9999999);
    }

    public static BigDecimal amount(double min, double max) {
        double value = ThreadLocalRandom.current().nextDouble(min, max);
        return BigDecimal.valueOf(value).setScale(2, RoundingMode.HALF_UP);
    }

    public static long thinkTimeMs(long minMs, long maxMs) {
        return ThreadLocalRandom.current().nextLong(minMs, maxMs + 1);
    }

    public static <T> T pick(List<T> values) {
        return values.get(ThreadLocalRandom.current().nextInt(values.size()));
    }
}
