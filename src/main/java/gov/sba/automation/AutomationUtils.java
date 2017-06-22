package gov.sba.automation;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AutomationUtils {

  private static final Logger logger = LoggerFactory.getLogger(AutomationUtils.class);

  private WebDriverWait wdriver;
  private WebDriver driver;

  public AutomationUtils() {

    BrowserUtils browserUtils = new BrowserUtils();
    browserUtils.initFirefox();

    wdriver = browserUtils.getWaitDriver();
    driver = browserUtils.getWebDriver();
  }

  public WebDriverWait getWdriver() {
    return wdriver;
  }

  public WebDriver getDriver() {
    return driver;
  }

  public void login(String username, String password) {
    driver.findElement(By.cssSelector(".button-full")).click();
    wdriver.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("#user_email")));

    driver.findElement(By.cssSelector("#user_email")).sendKeys(username);
    driver.findElement(By.cssSelector("#user_password")).sendKeys(password);
    driver.findElement(By.cssSelector("#business_signin")).click();
  }

  public void startNewProgram(String programName) throws Exception {
    driver.get("http://localhost:3000");

    CoreWait.waitForPresenceOfElementLocated(wdriver, By.cssSelector(".button-full"));

    login("john@mailinator.com", "password");

    By locator = By.partialLinkText("Programs");
    CoreWait.waitForElementToBeClickable(driver, wdriver, locator);

    String actual = driver.findElement(locator).getText();
    CoreUtils.assertContentEquals("Programs", actual);

    // Now let's click the program link
    CoreUtils.locateAndClick(driver, locator);

    boolean deleteDraft = true;

    if (deleteDraft) {
      logger.debug("FYI: about to call deleteDrafProgram()");
      // TODO: delete the 'Draft' application if any?
      CoreUtils.deleteDraftProgram(driver, programName);
      Thread.sleep(5000);
    }

    // Get the program name
    String programDesc = CoreUtils.lookupProgram(programName);
    logger.debug("FYI: using : " + programDesc);

    // Need to click "#certificate_type_" + programName here
    locator = By.cssSelector("#certificate_type_" + programName);
    CoreUtils.locateAndClick(driver, locator);

    locator = By.cssSelector("#add_certification");
    CoreUtils.locateAndClick(driver, locator);

    // Then we click Continue button
    CoreUtils.clickContinue(driver);
  }

  public void clickThroughWosb(WebDriver driver) {
    CoreUtils.yesOrNo(driver, "no", new int[] {198});
    CoreUtils.clickContinue(driver);

    CoreUtils.yesOrNo(driver, "no", new int[] {199});
    CoreUtils.clickContinue(driver);

    CoreUtils.yesOrNo(driver, "no", new int[] {201});
    CoreUtils.clickContinue(driver);

    CoreUtils.yesOrNo(driver, "no", new int[] {211});
    CoreUtils.comment(driver, 211);

    CoreUtils.yesOrNo(driver, "no", new int[] {212});
    CoreUtils.comment(driver, 212);
    CoreUtils.clickContinue(driver);

    CoreUtils.yesOrNo(driver, "no", new int[] {213});
    CoreUtils.clickContinue(driver);

    CoreUtils.yesOrNo(driver, "no", new int[] {214, 215, 216});
    CoreUtils.comment(driver, 216);
    CoreUtils.clickContinue(driver);

    CoreUtils.yesOrNo(driver, "no", new int[] {217, 218, 219, 220, 221, 222});
    CoreUtils.comment(driver, 222);
    CoreUtils.clickContinue(driver);

    CoreUtils.yesOrNo(driver, "no", new int[] {223});
    CoreUtils.clickContinue(driver);

    CoreWait.waitForUrlContains(wdriver, "/wosb/review_sections/review/edit");
    CoreUtils.clickContinue(driver);

    CoreUtils.accepTermsAndConditions(driver, new int[] {0, 1, 2, 3, 4, 5});
    // Note: but don't click continue for now to simplify the test
  }

  public static void main(String... args) throws Exception {
    AutomationUtils app = new AutomationUtils();
    app.startNewProgram("wosb");

    app.clickThroughWosb(app.getDriver());
    System.out.println("Done!");
  }
}