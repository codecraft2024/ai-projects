package com.instasimulator.common.util;

import java.util.UUID;

/**
 * Generates correlation and session identifiers.
 */
public final class IdGenerator {

    private IdGenerator() {
    }

    public static String correlationId() {
        return "corr-" + UUID.randomUUID();
    }

    public static String sessionId() {
        return "sess-" + UUID.randomUUID();
    }

    public static String executionId() {
        return "exec-" + UUID.randomUUID();
    }

    public static String shortId() {
        return UUID.randomUUID().toString().substring(0, 8);
    }
}
