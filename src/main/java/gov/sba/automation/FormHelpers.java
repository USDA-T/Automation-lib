package gov.sba.automation;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class FormHelpers {

  public static void fillElement(WebDriver driver, By findBy, String text) throws Exception {
    WebElement element = driver.findElement(findBy);
    element.sendKeys(text);
  }

  /**
   * Submit a form having a given finder specified in findBy object
   *
   * @param driver
   * @param findBy
   * @throws Exception
   */
  public static void submitForm(WebDriver driver, By findBy) throws Exception {
    WebElement element = driver.findElement(findBy);
    element.click();
  }

  /**
   * Submit the form of a given element that we already know exists.
   *
   * @param element
   * @throws Exception
   */
  public static void submitForm(WebElement element) throws Exception {
    element.click();
  }

}
