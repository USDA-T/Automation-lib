package gov.sba.automation;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class CoreWait {

  public static void waitForPresenceOfElementLocated(WebDriverWait wdriver, By locator) {
    wdriver.until(ExpectedConditions.presenceOfElementLocated(locator));
  }

  public static void waitForVisibilityOf(WebDriver driver, WebDriverWait wdriver, By locator) {
    wdriver.until(ExpectedConditions.visibilityOf(driver.findElement(locator)));
  }

  public static void waitForElementToBeClickable(WebDriver driver, WebDriverWait wdriver,
      By locator) {
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

  public static void waitForVisibilityOfAllElementsLocatedBy(WebDriverWait wdriver, By locator) {
    wdriver.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(locator));
  }

  public static void waitForVisibilityOfAllElements(WebDriverWait wdriver,
      List<WebElement> elements) {
    wdriver.until(ExpectedConditions.visibilityOfAllElements(elements));
  }

  public static void waitForVisibilityOf(WebDriverWait wdriver, WebElement element) {
    wdriver.until(ExpectedConditions.visibilityOf(element));
  }

  public static void waitForTextToBePresentInElementLocated(WebDriverWait wdriver, By locator,
      String text) {
    wdriver.until(ExpectedConditions.textToBePresentInElementLocated(locator, text));
  }

  public static void waitForTextToBePresentInElementValue(WebDriverWait wdriver, By locator,
      String text) {
    wdriver.until(ExpectedConditions.textToBePresentInElementValue(locator, text));
  }

  public static void waitForFrameToBeAvailableAndSwitchToIt(WebDriverWait wdriver, By locator) {
    wdriver.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(locator));
  }

  public static void waitForFrameToBeAvailableAndSwitchToIt(WebDriverWait wdriver,
      String frameLocator) {
    wdriver.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(frameLocator));
  }

  public static void waitForElementToBeSelected(WebDriverWait wdriver, final WebElement element) {
    wdriver.until(ExpectedConditions.elementToBeSelected(element));
  }

  public static void waitForElementToBeSelected(WebDriverWait wdriver, final By locator) {
    wdriver.until(ExpectedConditions.elementToBeSelected(locator));
  }

  public static void waitForAlertIsPresent(WebDriverWait wdriver) {
    wdriver.until(ExpectedConditions.alertIsPresent());
  }

  public static void waitForNumberOfWindowsToBe(WebDriverWait wdriver, final int windowsCount) {
    wdriver.until(ExpectedConditions.numberOfWindowsToBe(windowsCount));
  }
}