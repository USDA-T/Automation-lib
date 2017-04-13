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
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testTemplate() throws Exception {
        // Place holder to make maven install the library locally
        Assert.assertEquals(2, 1 + 1);
    }
}
