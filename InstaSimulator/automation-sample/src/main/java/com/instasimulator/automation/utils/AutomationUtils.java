package com.instasimulator.automation.utils;

import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.OutputType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;

/**
 * Screenshot and wait helpers for UI tests.
 */
public final class AutomationUtils {

    private static final Logger log = LoggerFactory.getLogger(AutomationUtils.class);

    private AutomationUtils() {
    }

    public static Path takeScreenshot(AppiumDriver driver, String name) {
        try {
            Path dir = Path.of("automation-reports", "screenshots");
            Files.createDirectories(dir);
            Path file = dir.resolve(name + "-" + Instant.now().toEpochMilli() + ".png");
            Files.write(file, driver.getScreenshotAs(OutputType.BYTES));
            log.info("Screenshot saved: {}", file.toAbsolutePath());
            return file;
        } catch (IOException e) {
            throw new IllegalStateException("Unable to save screenshot", e);
        }
    }
}
