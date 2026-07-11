package com.instasimulator.automation.pages;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import org.openqa.selenium.WebElement;

/**
 * Login screen page object.
 */
public class LoginPage extends BasePage {

    @AndroidFindBy(id = "com.instasimulator.demo:id/username")
    private WebElement usernameField;

    @AndroidFindBy(id = "com.instasimulator.demo:id/password")
    private WebElement passwordField;

    @AndroidFindBy(id = "com.instasimulator.demo:id/loginButton")
    private WebElement loginButton;

    @AndroidFindBy(id = "com.instasimulator.demo:id/loginTitle")
    private WebElement loginTitle;

    public LoginPage(AppiumDriver driver) {
        super(driver);
    }

    public boolean isLoaded() {
        return isDisplayed(loginTitle) || isDisplayed(loginButton);
    }

    public LoginPage enterUsername(String username) {
        type(usernameField, username);
        return this;
    }

    public LoginPage enterPassword(String password) {
        type(passwordField, password);
        return this;
    }

    public HomePage tapLogin() {
        tap(loginButton);
        return new HomePage(driver);
    }

    public HomePage loginAs(String username, String password) {
        return enterUsername(username).enterPassword(password).tapLogin();
    }
}
