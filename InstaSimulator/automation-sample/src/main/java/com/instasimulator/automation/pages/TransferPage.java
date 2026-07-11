package com.instasimulator.automation.pages;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import org.openqa.selenium.WebElement;

/**
 * Transfer funds screen page object.
 */
public class TransferPage extends BasePage {

    @AndroidFindBy(id = "com.instasimulator.demo:id/beneficiaryField")
    private WebElement beneficiaryField;

    @AndroidFindBy(id = "com.instasimulator.demo:id/amountField")
    private WebElement amountField;

    @AndroidFindBy(id = "com.instasimulator.demo:id/submitTransfer")
    private WebElement submitButton;

    @AndroidFindBy(id = "com.instasimulator.demo:id/transferTitle")
    private WebElement transferTitle;

    public TransferPage(AppiumDriver driver) {
        super(driver);
    }

    public boolean isLoaded() {
        return isDisplayed(transferTitle);
    }

    public TransferPage enterBeneficiary(String beneficiary) {
        type(beneficiaryField, beneficiary);
        return this;
    }

    public TransferPage enterAmount(String amount) {
        type(amountField, amount);
        return this;
    }

    public HomePage submit() {
        tap(submitButton);
        return new HomePage(driver);
    }
}
