package com.instasimulator.automation.tests;

import com.instasimulator.automation.config.AutomationConfig;
import com.instasimulator.automation.driver.AppiumManager;
import com.instasimulator.automation.driver.DriverManager;
import com.instasimulator.automation.pages.HomePage;
import com.instasimulator.automation.pages.LoginPage;
import com.instasimulator.automation.reports.TestReporter;
import com.instasimulator.automation.utils.AutomationUtils;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Sample Android UI login test using Appium 2 + UiAutomator2.
 *
 * <p>Prerequisites:
 * <ul>
 *   <li>Android emulator running</li>
 *   <li>Appium 2 server: {@code appium --base-path /}</li>
 *   <li>Demo app installed or {@code appium.app} pointing to an APK</li>
 * </ul>
 */
public class LoginTest {

    private AutomationConfig config;

    @BeforeMethod
    public void setUp() {
        config = new AutomationConfig();
        AppiumManager.logServerExpectation(config);
        DriverManager.startDriver(config);
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        DriverManager.quitDriver();
    }

    @Test(description = "Launch app, login, and verify home screen")
    public void shouldLoginAndReachHomeScreen() {
        String testName = "shouldLoginAndReachHomeScreen";
        try {
            TestReporter.info("Opening login screen");
            LoginPage loginPage = new LoginPage(DriverManager.getDriver());
            Assert.assertTrue(loginPage.isLoaded(), "Login screen should be visible");

            HomePage homePage = loginPage.loginAs("demo.user", "Password123!");
            Assert.assertTrue(homePage.isLoaded(), "Home screen should be visible after login");

            AutomationUtils.takeScreenshot(DriverManager.getDriver(), "home-after-login");
            TestReporter.pass(testName);
        } catch (AssertionError | RuntimeException e) {
            TestReporter.fail(testName, e);
            AutomationUtils.takeScreenshot(DriverManager.getDriver(), "login-failure");
            throw e;
        }
    }
}
