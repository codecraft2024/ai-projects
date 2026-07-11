package com.instasimulator.automation.pages;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import org.openqa.selenium.WebElement;

/**
 * Home screen page object.
 */
public class HomePage extends BasePage {

    @AndroidFindBy(id = "com.instasimulator.demo:id/homeTitle")
    private WebElement homeTitle;

    @AndroidFindBy(id = "com.instasimulator.demo:id/transferButton")
    private WebElement transferButton;

    @AndroidFindBy(id = "com.instasimulator.demo:id/settingsButton")
    private WebElement settingsButton;

    public HomePage(AppiumDriver driver) {
        super(driver);
    }

    public boolean isLoaded() {
        return isDisplayed(homeTitle);
    }

    public TransferPage openTransfer() {
        tap(transferButton);
        return new TransferPage(driver);
    }

    public SettingsPage openSettings() {
        tap(settingsButton);
        return new SettingsPage(driver);
    }
}
