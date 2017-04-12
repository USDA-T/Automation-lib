package gov.sba.automation;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Function;

public class CoreUtils {
    private static final Logger logger = LoggerFactory.getLogger(CoreUtils.class);
    
    /**
     * Poll for element until the element is presented or timeout
     */
    public static WebElement waitForElement(WebDriver driver, By locator) {
        FluentWait<WebDriver> wait = new FluentWait<WebDriver>(driver);
        wait.pollingEvery(250, TimeUnit.MILLISECONDS);
        wait.withTimeout(10, TimeUnit.SECONDS);
        wait.ignoring(NoSuchElementException.class);

        WebElement element = wait.until(new Function<WebDriver, WebElement>() {
            public WebElement apply(WebDriver d) {
                return d.findElement(locator);
            }
        });

        return element;
    }

    public static void locateAndClick(WebDriver driver, By locator) {
        WebElement element = waitForElement(driver, locator);
        element.click();
    }

    public static void locateAndSubmit(WebDriver driver, By locator) {
        WebElement element = waitForElement(driver, locator);
        element.submit();
    }

    public static void locateAndFill(WebDriver driver, By locator, String text) {
        WebElement element = waitForElement(driver, locator);
        element.sendKeys(text);
    }

    public static void yesOrNo(WebDriver driver, String answerType, int[] ids) {
        String expr;
        for (int id : ids) {
            expr = "#answers_" + id + "_value_" + answerType;
            locateAndClick(driver, By.cssSelector(expr));
        }
    }

    public static void comment(WebDriver driver, int id) {
        String expr = "#answers_" + id + "_comment";
        locateAndFill(driver, By.cssSelector(expr), "Comment for : " + id);
    }

    public static void clickContinue(WebDriver driver) {
        locateAndSubmit(driver, By.name("commit"));
    }

    public static void accepTermsAndConditions(WebDriver driver, int[] ids) {
        for (int id : ids) {
            locateAndClick(driver, By.cssSelector("#legal_" + id));
        }
    }

    // Utility functions
    public static void assertContentEquals(String expected, String actual) {
        if (!(expected.equals(actual))) {
            throw new RuntimeException("Expected: " + expected + ", Actual: " + actual);
        }
    }

    private static final Map<String, String> programsMap;

    static {
        Map<String, String> aMap = new HashMap<String, String>();

        aMap.put("mpp", "MPP");
        aMap.put("wosb", "WOSB Self-Certification");
        aMap.put("eight_a", "8(a) Document Upload");
        aMap.put("edwosg", "EDWOSB");

        programsMap = Collections.unmodifiableMap(aMap);
    }

    public static String lookupProgram(String programName) {
       return programsMap.get(programName);
    }

    public static void deleteDraftProgram(WebDriver driver, String programName) {    
        logger.info("FYI: looking up using programName : " + programName);
        
        String programDesc = CoreUtils.lookupProgram(programName);
        logger.info("FYI: looking up using programDesc : " + programDesc);
        
        try {
            // xpath expression for 'Draft expression.."
            String xpathExpr = String.format("//*[@id='certifications']/tbody/tr[(td[position()=1]/a[contains(text(),'%s')]) and (td[position()=5 and contains(text(),'Draft')])]/td[position()=7]/a[ contains(text(),'Delete')]",
                    programDesc
                    );
            
            By locator = By.xpath(xpathExpr);

            WebElement element = driver.findElement(locator);

            if (element != null) {
                logger.debug("FYI: element found, and will be click: " + element);
                element.click();
                driver.switchTo().alert().accept();
            } else {
                logger.debug("FYI: element not found, no action will be taken!");
            }
        } catch (Exception e) {
            logger.warn("FYI: exceptions .." + e.getMessage());
            // NOTE: this is to be expected if we don't have any 'Draft' certification on the first run
        }
    }

    public static void acceptPopupAlert(WebDriver driver) {
        System.out.println("FYI: beginning of accept..");
        // http://stackoverflow.com/questions/16448173/selenium-automatically-accepting-alerts
        WebDriverWait wait = new WebDriverWait(driver, 10); // wait for 5 seconds
        wait.until(ExpectedConditions.alertIsPresent());
        driver.switchTo().alert().accept();
        System.out.println("FYI: end of accept..");
    }
    
}
