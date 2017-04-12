package gov.sba.automation;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.WebDriver;

import junit.framework.TestCase;

public class FirefoxDriverTest extends TestCase {
    @SuppressWarnings("unused")
    private static WebDriver driver;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
        // TODO: add your setup here
    }

    @After
    public void tearDown() throws Exception {
        // TODO: add your teardown code here!
        // driver.quit();
    }

    @Test
    public void testTemplate() throws Exception {
        // Place holder to make maven install the library locally
        Assert.assertEquals(2, 1 + 1);
    }

    // @Test
    // public void testGoogleSearch() throws Exception {
    //     System.out.println("Begin simple Google search...");
    //     driver.get("https://www.google.com/webhp?gws_rd=ssl");
    //     WebElement searchBox = driver.findElement(By.name("q"));
    //     searchBox.sendKeys("ChromeDriver");
    //     searchBox.submit();
    //     Assert.assertEquals("Google", driver.getTitle());
    // }
}
