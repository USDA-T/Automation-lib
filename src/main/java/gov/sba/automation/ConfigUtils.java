//
package gov.sba.automation;

import java.io.IOException;
import java.util.Properties;

public class ConfigUtils {

	public static Properties loadDefaultProperties() {
		Properties props = new Properties();
		try {
			props.load(TestHelpers.class.getResourceAsStream("/default.properties"));
			// Set the default test environment to test if none was set
			System.setProperty(Constants.TEST_ENV, props.getProperty(Constants.TEST_ENV, Constants.ENV_DEVELOPMENT));
		} catch (IOException e) {
			throw new RuntimeException("Error loading the resource file." + e.getMessage());
		}
		return props;
	}

	public static String systemType() {
		return System.getProperty("os.name").toLowerCase();
	}

	public static boolean isWindows(String osName) {
		return (osName.indexOf("win") >= 0);
	}

	public static boolean isMac(String osName) {
		return (osName.indexOf("mac") >= 0);
	}

	public static boolean isUnix(String osName) {
		return (osName.indexOf("nix") >= 0 || osName.indexOf("nux") >= 0 || osName.indexOf("aix") > 0);
	}
}