package gov.sba.automation;

import java.io.File;

public class FixtureUtils {

	/**
	 * Return the fixture directory in a system independent way
	 *
	 * @return String path of the fixture directory only e.g.
	 *         ${project_root}/src/main/DataFiles/"
	 */
	public static String fixturesDir() {
		return System.getProperty("user.dir") + File.separator + "src" + File.separator + "main" + File.separator
				+ "DataFiles" + File.separator;
	}

	public static String rootDirExecutionFile() {
		return System.getProperty("user.dir") + File.separator + "Stop_Execution.yaml";
	}

	public static String get_SS_Dir() {
		return System.getProperty("user.dir") + File.separator + "src" + File.separator + "main" + File.separator
				+ "Screenshots" + File.separator;
	}

	public static String fixturesDir_Duns() {
		return System.getProperty("user.dir") + File.separator + "src" + File.separator + "main" + File.separator
				+ "resources" + File.separator;
	}

	public static String resourcesDir() {
		return String.join(File.separator, new String[] { System.getProperty("user.dir"), "src", "main", "resources" })
				+ File.separator;
	}
}
