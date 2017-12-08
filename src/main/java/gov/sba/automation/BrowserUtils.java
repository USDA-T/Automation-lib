package gov.sba.automation;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BrowserUtils {
  private static final Logger logger = LoggerFactory.getLogger(BrowserUtils.class);

  private WebDriverWait wait;
  private WebDriver driver;

  public BrowserUtils() {
    initFirefox();
    // TODO: allow Chrome driver here if you like
    driver = new FirefoxDriver();

    // Some sensible default for testing
    // driver.manage().window().maximize();
    driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

    // wait for at most 10 seconds before timeout
    wait = new WebDriverWait(driver, 10);
  }

  public WebDriverWait getWaitDriver() {
    return wait;
  }

  public WebDriver getWebDriver() {
    return driver;
  }

  public static boolean isWindow() {
    return System.getProperty("os.name").toLowerCase().contains("windows");
  }

  // Note: support just Firefox during development to simplify the code
  public void initFirefox() {
    String driverPath = System.getenv("HOME") + "/apps/geckodriver";
    logger.info("FYI: using driverPath : " + driverPath);

    if (isWindow()) {
      driverPath += ".exe";
    }

    System.setProperty("webdriver.firefox.driver", driverPath);
    System.setProperty("webdriver.gecko.driver", driverPath);
    System.setProperty("webdriver.firefox.port", "7046");
  }

}
