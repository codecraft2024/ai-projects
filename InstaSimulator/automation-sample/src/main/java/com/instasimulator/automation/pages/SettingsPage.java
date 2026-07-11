package com.instasimulator.automation.pages;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import org.openqa.selenium.WebElement;

/**
 * Settings screen page object.
 */
public class SettingsPage extends BasePage {

    @AndroidFindBy(id = "com.instasimulator.demo:id/settingsTitle")
    private WebElement settingsTitle;

    @AndroidFindBy(id = "com.instasimulator.demo:id/logoutButton")
    private WebElement logoutButton;

    public SettingsPage(AppiumDriver driver) {
        super(driver);
    }

    public boolean isLoaded() {
        return isDisplayed(settingsTitle);
    }

    public LoginPage logout() {
        tap(logoutButton);
        return new LoginPage(driver);
    }
}
