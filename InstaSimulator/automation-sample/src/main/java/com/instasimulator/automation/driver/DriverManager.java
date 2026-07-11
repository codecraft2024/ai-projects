package com.instasimulator.automation.driver;

import com.instasimulator.automation.config.AutomationConfig;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.time.Duration;

/**
 * Creates and manages the Appium {@link AndroidDriver} lifecycle.
 */
public final class DriverManager {

    private static final Logger log = LoggerFactory.getLogger(DriverManager.class);
    private static final ThreadLocal<AndroidDriver> DRIVER = new ThreadLocal<>();

    private DriverManager() {
    }

    public static AndroidDriver getDriver() {
        AndroidDriver driver = DRIVER.get();
        if (driver == null) {
            throw new IllegalStateException("Driver not initialized. Call startDriver() first.");
        }
        return driver;
    }

    public static AndroidDriver startDriver(AutomationConfig config) {
        try {
            UiAutomator2Options options = new UiAutomator2Options()
                    .setPlatformName(config.platformName())
                    .setAutomationName(config.automationName())
                    .setDeviceName(config.deviceName())
                    .setAppPackage(config.appPackage())
                    .setAppActivity(config.appActivity())
                    .setNoReset(config.noReset())
                    .setNewCommandTimeout(Duration.ofSeconds(120));

            if (config.udid() != null && !config.udid().isBlank()) {
                options.setUdid(config.udid());
            }
            if (config.appPath() != null && !config.appPath().isBlank()) {
                options.setApp(config.appPath());
            }

            AndroidDriver driver = new AndroidDriver(URI.create(config.appiumServerUrl()).toURL(), options);
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
            DRIVER.set(driver);
            log.info("Appium AndroidDriver started against {}", config.appiumServerUrl());
            return driver;
        } catch (Exception e) {
            throw new IllegalStateException("Failed to start Appium driver", e);
        }
    }

    public static void quitDriver() {
        AndroidDriver driver = DRIVER.get();
        if (driver != null) {
            try {
                driver.quit();
            } finally {
                DRIVER.remove();
                log.info("Appium AndroidDriver quit");
            }
        }
    }
}
