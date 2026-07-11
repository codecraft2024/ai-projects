package com.instasimulator.automation.reports;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Minimal test reporting helper.
 */
public final class TestReporter {

    private static final Logger log = LoggerFactory.getLogger(TestReporter.class);

    private TestReporter() {
    }

    public static void info(String message) {
        log.info("[REPORT] {}", message);
    }

    public static void pass(String testName) {
        log.info("[PASS] {}", testName);
    }

    public static void fail(String testName, Throwable error) {
        log.error("[FAIL] {} :: {}", testName, error.getMessage());
    }
}
