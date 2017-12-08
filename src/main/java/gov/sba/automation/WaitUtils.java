package gov.sba.automation;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.google.common.base.Function;

public class WaitUtils {

  /**
   * Poll for element until the element is presented or timeout
   */
  public static WebElement waitForElement(WebDriver driver, By locator) {
    FluentWait<WebDriver> wait = new FluentWait<WebDriver>(driver);
    wait.pollingEvery(250, TimeUnit.MILLISECONDS);
    wait.withTimeout(30, TimeUnit.SECONDS);
    wait.ignoring(NoSuchElementException.class);

    WebElement element = wait.until(new Function<WebDriver, WebElement>() {
      public WebElement apply(WebDriver d) {
        return d.findElement(locator);
      }
    });

    return element;
  }

  public static boolean waitForCondition(WebDriver driver, int timeout, String title) {
    WebDriverWait wait = getDriverWait(driver, timeout);
    return wait.until(ExpectedConditions.titleContains(title));
  }

  public static WebElement getWhenClickable(WebDriver driver, int timeout, By locator) {
    WebDriverWait wait = getDriverWait(driver, timeout);
    return wait.until(ExpectedConditions.elementToBeClickable(locator));
  }

  public static WebElement getWhenVisible(WebDriver driver, By locator, int timeout) {
    WebDriverWait wait = getDriverWait(driver, timeout);
    return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
  }

  public static List<WebElement> getWhenVisibleOfAllElements(WebDriver driver, By locator,
      int timeout) {
    WebDriverWait wait = getDriverWait(driver, timeout);
    return wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(locator));
  }

  public static void clickWhenReady(WebDriver driver, By locator, int timeout) {
    WaitUtils.getWhenClickable(driver, timeout, locator).click();
  }

  private static WebDriverWait getDriverWait(WebDriver driver, int timeout) {
    return new WebDriverWait(driver, timeout);
  }

}
