package gov.sba.automation;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

import static gov.sba.automation.CommonApplicationMethods.create_File_To_Indicate_Currently_Running_In_Headless;
import static gov.sba.automation.CommonApplicationMethods.delete_Any_File_For_Headless;
import static gov.sba.automation.CommonApplicationMethods.get_Stop_Execution_Flag;

import java.util.Properties;
import java.util.concurrent.TimeUnit;

public class TestHelpers {
	final public static String BASE_URL = "base_url_";
	private static final Logger logger = LogManager.getLogger(TestHelpers.class.getName());
	public static String headless_Parm = "yes";

	public static void set_Headless() {
		if (System.getProperty("os.name").startsWith("Windows")) {
			headless_Parm = "no";
			logger.info("Set as Head Chrome");
		}
	}

	public static WebDriver getDefaultWebDriver() throws Exception {
		get_Stop_Execution_Flag();
		delete_Any_File_For_Headless();
		Properties props = ConfigUtils.loadDefaultProperties();
		WebDriver driver = null;

		/* Setup the configuration based on the browser we are using */
		/*
		 * System.setProperty("webdriver.chrome.driver",
		 * props.getProperty("webdriver.chrome.driver"));
		 */

		/* Update the Property File Instead Of Hardcoding */
		String browser = props.getProperty(Constants.BROWSER);
		System.setProperty(Constants.BROWSER, browser);
		String envUnderTest = System.getenv(Constants.TEST_ENV);

		/*
		 * Default to 'development' if none is provided TODO: this should never be null,
		 * may be remove?
		 */

		if (envUnderTest == null)
			envUnderTest = "development";

		logger.debug("Your system under test :" + envUnderTest);
		System.setProperty(Constants.TEST_ENV, envUnderTest);
		logger.debug("Setting environment:" + Constants.TEST_ENV + " to " + envUnderTest);

		String testUrl = props.getProperty(BASE_URL + envUnderTest);

		if (testUrl == null) {
			throw new RuntimeException(
					"You need to setup the '" + BASE_URL + envUnderTest + "' in your default.properties file");
		}

		logger.debug("FYI: your test URL:" + testUrl + " ::- And Browser: " + browser);

		/* Set it so that we can use it later */

		System.setProperty(BASE_URL + envUnderTest, testUrl);

		String[] configKeys;
		switch (browser) {
		case Constants.BROWSER_CHROME:
			configKeys = new String[] { "webdriver.chrome.driver" };
			setSystemProperties(configKeys, props);
			ChromeOptions options = new ChromeOptions();
			options.addArguments("--disable-extensions");
			if (headless_Parm.toLowerCase().indexOf("no") >= 0) {
				/* Not Headless */
			} else {
				/* With Headless */
				if (!props.containsKey("non_headless")) {
					options.addArguments("headless");
					create_File_To_Indicate_Currently_Running_In_Headless();
				}

			}
			if (props.containsKey("binary_Chrome")) {
				String chrome_Binary = props.getProperty("binary_Chrome");
				options.setBinary(chrome_Binary);
				create_File_To_Indicate_Currently_Running_In_Headless();
			}

			options.addArguments("--window-size=1920,1080");
			driver = new ChromeDriver(options);
			driver.manage().window().maximize();
			break;
		case Constants.BROWSER_FIREFOX:

			if (ConfigUtils.isUnix(ConfigUtils.systemType())) {
				configKeys = new String[] { /* Need to provide specific type information for Linux */
						"webdriver.firefox.bin", "webdriver.firefox.port" /* Note: for older version of Firefox */
						/* For newer version of Firefox // "webdriver.gecko.driver" */
				};
				setSystemProperties(configKeys, props);
			}

			if (props.containsKey("webdriver.gecko.driver")) {
				System.setProperty("webdriver.gecko.driver", props.getProperty("webdriver.gecko.driver"));
			}

			/* TODO: verify if we need to do the same for MacOs? */

			// Populate Profile
			FirefoxProfile profile = new FirefoxProfile();
			profile.setPreference("base_url_development", "https://certify.newqa.sba-one.net/");
			profile.setAcceptUntrustedCertificates(true);
			profile.setAssumeUntrustedCertificateIssuer(true);

			// Populate DC
			DesiredCapabilities dc = DesiredCapabilities.firefox();
			dc.setCapability(FirefoxDriver.PROFILE, profile);
			dc.setCapability("acceptInsecureCerts", true);

			// Start Driver
			driver = new FirefoxDriver(dc);
			driver.manage().window().maximize();
			break;
		case Constants.BROWSER_PHANTOMJS:
			configKeys = new String[] {};
			setSystemProperties(configKeys, props);
			driver = new PhantomJSDriver();
			driver.manage().window().maximize();
			break;
		case "ie":
			configKeys = new String[] {
					// Note: add IE specific setting if any
			};
			throw new RuntimeException("IE is currently not supported, will be added later!");
		default:
			throw new RuntimeException("Unknown browser: " + browser);
		}

		// String currentWindowHandle = driver.getWindowHandle();

		// NOTE: additional settings to make the driver more robust
		// driver.manage().deleteAllCookies();

		// driver.manage().window().maximize();
		// driver.switchTo().window(currentWindowHandle);

		driver.manage().timeouts().pageLoadTimeout(60, TimeUnit.SECONDS);
		driver.manage().timeouts().setScriptTimeout(60, TimeUnit.SECONDS);
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);

		return driver;
	}

	public static String getBaseUrl() {
		return System.getProperty(TestHelpers.BASE_URL + System.getProperty(Constants.TEST_ENV));
	}

	private static void setSystemProperties(String[] configKeys, Properties props) {
		for (String confKey : configKeys) {
			logger.debug("Update system property :" + confKey + "=" + props.getProperty(confKey));
			System.setProperty(confKey, props.getProperty(confKey));
		}
	}
}
