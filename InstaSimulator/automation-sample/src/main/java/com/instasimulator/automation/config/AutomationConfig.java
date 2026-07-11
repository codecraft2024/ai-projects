package com.instasimulator.automation.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Loads Appium / device configuration from {@code appium.properties}.
 */
public final class AutomationConfig {

    private final Properties properties = new Properties();

    public AutomationConfig() {
        try (InputStream in = getClass().getClassLoader().getResourceAsStream("appium.properties")) {
            if (in != null) {
                properties.load(in);
            }
        } catch (IOException e) {
            throw new IllegalStateException("Unable to load appium.properties", e);
        }
    }

    public String get(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }

    public String appiumServerUrl() {
        return get("appium.server.url", "http://127.0.0.1:4723");
    }

    public String platformName() {
        return get("appium.platformName", "Android");
    }

    public String automationName() {
        return get("appium.automationName", "UiAutomator2");
    }

    public String deviceName() {
        return get("appium.deviceName", "Android Emulator");
    }

    public String udid() {
        return get("appium.udid", "");
    }

    public String appPackage() {
        return get("appium.appPackage", "com.instasimulator.demo");
    }

    public String appActivity() {
        return get("appium.appActivity", ".ui.LoginActivity");
    }

    public String appPath() {
        return get("appium.app", "");
    }

    public boolean noReset() {
        return Boolean.parseBoolean(get("appium.noReset", "true"));
    }
}
