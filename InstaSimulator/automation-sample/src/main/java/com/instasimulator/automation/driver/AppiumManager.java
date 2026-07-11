package com.instasimulator.automation.driver;

import com.instasimulator.automation.config.AutomationConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Helper for documenting Appium server expectations.
 * In CI/local runs, start Appium separately: {@code appium --base-path /}
 */
public final class AppiumManager {

    private static final Logger log = LoggerFactory.getLogger(AppiumManager.class);

    private AppiumManager() {
    }

    public static void logServerExpectation(AutomationConfig config) {
        log.info("Expecting Appium 2 server at {} with automationName={}",
                config.appiumServerUrl(), config.automationName());
    }
}
