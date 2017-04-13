package gov.sba.automation;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class CoreWait {

    public static void waitForPresenceOfElementLocated(WebDriverWait wdriver, By locator) {
        wdriver.until(ExpectedConditions.presenceOfElementLocated(locator));
    }

    public static void waitForVisibilityOf(WebDriver driver, WebDriverWait wdriver, By locator) {
        wdriver.until(ExpectedConditions.visibilityOf(driver.findElement(locator)));
    }

    public static void waitForElementToBeClickable(WebDriver driver, WebDriverWait wdriver, By locator) {
        wdriver.until(ExpectedConditions.elementToBeClickable(driver.findElement(locator)));
    }
    
    public static void waitForTitleIs(WebDriverWait wdriver, String title) {
        wdriver.until(ExpectedConditions.titleIs(title));
    }
    
    public static void waitForTitleContains(WebDriverWait wdriver, String title) {
        wdriver.until(ExpectedConditions.titleContains(title));
    }
    
    public static void waitForTitlex(WebDriverWait wdriver, String url) {
        wdriver.until(ExpectedConditions.urlToBe(url));
    }

    public static void waitForUrlContains(WebDriverWait wdriver, String fraction) {
        wdriver.until(ExpectedConditions.urlContains(fraction));
    }
    
    public static void waitForUrlMatches(WebDriverWait wdriver, String regex) {
        wdriver.until(ExpectedConditions.urlMatches(regex));
    }
    
}