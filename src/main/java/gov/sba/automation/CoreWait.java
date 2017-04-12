package gov.sba.automation;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class CoreWait {

    public static void waitForPresenceOfElement(WebDriver driver, WebDriverWait wdriver, By locator) {
        wdriver.until(ExpectedConditions.presenceOfElementLocated(locator));
    }

    public static void waitForVisibilityOfElement(WebDriver driver, WebDriverWait wdriver, By locator) {
        wdriver.until(ExpectedConditions.visibilityOf(driver.findElement(locator)));
    }

    public static void waitForElementToBeClickable(WebDriver driver, WebDriverWait wdriver, By locator) {
        wdriver.until(ExpectedConditions.elementToBeClickable(driver.findElement(locator)));
    }

}