package gov.sba.automation;

import java.awt.Robot;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RobotUtils {
	@SuppressWarnings("unused")
	private static final Logger logger = LoggerFactory.getLogger(RobotUtils.class);

	private WebDriver driver;

	public RobotUtils(WebDriver driver) {
		this.driver = driver;
	}

	public void confirmAction(WebDriver driver, String expr) throws Exception {
		driver.findElement(By.name(expr)).submit();
		Thread.sleep(2000);
	}

	private void setClipboardWithFileLocation(String inputPath) {
		StringSelection selection = new java.awt.datatransfer.StringSelection(inputPath);
		java.awt.Toolkit.getDefaultToolkit().getSystemClipboard().setContents(selection, null);
	}

	private void uploadRobot() throws Exception {
		Robot robot = new Robot();
		robot.setAutoDelay(200);
		robot.setAutoWaitForIdle(true);
		robot.delay(200);

		robot.keyPress(KeyEvent.VK_CONTROL);
		robot.delay(200);

		robot.keyPress(KeyEvent.VK_V);
		robot.delay(200);

		robot.keyPress(KeyEvent.VK_V);
		robot.delay(200);

		robot.keyPress(KeyEvent.VK_CONTROL);
		robot.delay(200);
		robot.delay(200);

		robot.keyPress(KeyEvent.VK_ENTER);
		robot.delay(200);

		robot.keyPress(KeyEvent.VK_ENTER);
		robot.delay(200);
	}

	public void uploadFile(String filePath) throws Exception {
		if (filePath == null) {
			filePath = System.getProperty("user.dir") + "/src/main/resources/sample.pdf";
		}
		this.setClipboardWithFileLocation(filePath);
		Thread.sleep(1000);
		this.uploadRobot();
		driver.findElement(By.xpath("//button[@id='attach'")).click();
		Thread.sleep(1000);
		this.confirmAction(driver, "commit");
	}
}