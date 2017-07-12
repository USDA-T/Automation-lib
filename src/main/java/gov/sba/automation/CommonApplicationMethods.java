// TS Created By Deepa_Patri
package gov.sba.automation;

import com.esotericsoftware.yamlbeans.YamlException;
import com.esotericsoftware.yamlbeans.YamlReader;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.Dimension;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.Rectangle;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.*;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static gov.sba.automation.ConfigUtils.isUnix;
import static gov.sba.automation.ConfigUtils.systemType;
import static org.junit.Assert.assertEquals;

public class CommonApplicationMethods {

  private static final Logger logger =
      LogManager.getLogger(CommonApplicationMethods.class.getName());
  // ------------------------------------------------------------------------------------------------------------
  // Usage [ Repeat for Find Elements]
  // 1. Find element with Locator - WebElement aa= find_Element(WebDriver webDriver, String
  // locator_Yaml);
  // 2. Find element with Values - WebElement aa= find_Element(WebDriver webDriver, String
  // locator_Type, String locator_Value);
  // 3. Optional Find element with Locator - WebElement aa= find_Element(WebDriver webDriver, String
  // locator_Yaml, Boolean true);
  // 4. Optional Find element with Values - WebElement aa= find_Element(WebDriver webDriver, String
  // locator_Type, String locator_Value, Boolean true);
  // ____________________________________________________________________________________________________________
  // ------------------------------------------------------------------------------------------------------------
  // All_Find_elements
  // ____________________________________________________________________________________________________________

  /*
   -----------------------------------------------------------------------------------------------
                   All_Find_Elements Only
   ____________________________________________________________________________________________________________
  */

  public static List<WebElement> find_Elements(WebDriver webdriver, String type_Locator,
      String value_Locator) throws Exception /* Non Optional */
  {
    double elapsed_Seconds = 0;
    logger.info(elapsed_Seconds);
    List<WebElement> element_01 = null;
    long tStart = System.currentTimeMillis();
      for (int i = 0; i < 1000; i++) {
      try {
        switch (type_Locator.toLowerCase()) {
          case "xpath":
            element_01 = webdriver.findElements(By.xpath(value_Locator));
            break;
          case "id":
            element_01 = webdriver.findElements(By.id(value_Locator));
            break;
          case "classname":
            element_01 = webdriver.findElements(By.className(value_Locator));
            break;
          case "name":
            element_01 = webdriver.findElements(By.name(value_Locator));
            break;
          case "cssselector":
            element_01 = webdriver.findElements(By.cssSelector(value_Locator));
            break;
          case "linktext":
            element_01 = webdriver.findElements(By.linkText(value_Locator));
            break;
        }
        elapsed_Seconds = (System.currentTimeMillis() - tStart) / 1000.0;
        if (element_01.size() > 0) {
          logger.info(elapsed_Seconds);
          return element_01;
        }

        if (elapsed_Seconds > 12) {
          logger.info(elapsed_Seconds);
          logger.info("After Successfull Find - Too Long - Check Performance");
          i = 9999;
        }

      } catch (Exception e) {
        elapsed_Seconds = (System.currentTimeMillis() - tStart) / 1000.0;
        if (elapsed_Seconds > 12) {
          logger.info(elapsed_Seconds);
          logger.info("After UnSuccessfull Find - Exception");
          i = 9999;
        }
      }
    }
    display("Trying to find BY " + type_Locator + ":" + value_Locator);
    throw new Exception("Elements Not Found");
  }

  public static List<WebElement> find_Elements(WebDriver webdriver, String locator_Yaml)
      throws Exception /* Non Optional */
  {
    Map locator = getLocator(locator_Yaml);
    String loc = locator.get("Locator").toString();
    String val = locator.get("Value").toString();
    return find_Elements(webdriver, loc, val);
  }

  public static List<WebElement> find_Elements(WebDriver webdriver, String type_Locator,
      String value_Locator, Boolean optional_Check) throws Exception /* Optional */
  {
    List<WebElement> element_01 = null;
    try {
      element_01 = find_Elements(webdriver, type_Locator, value_Locator);
      return element_01;
    } catch (Exception e) {
      return null;
    }
  }

  public static List<WebElement> find_Elements(WebDriver webdriver, String locator_Yaml,
      Boolean optional_Check) throws Exception /* Optional */
  {
    Map locator = getLocator(locator_Yaml);
    String loc = locator.get("Locator").toString();
    String val = locator.get("Value").toString();
    List<WebElement> element_01 = null;
    try {
      element_01 = find_Elements(webdriver, loc, val);
      return element_01;
    } catch (Exception e) {
      return null;
    }
  }

  /*
   -----------------------------------------------------------------------------------------------
            All_Find_element Only
   ____________________________________________________________________________________________________________
   */

  public static WebElement find_Element(WebDriver webdriver, String type_Locator,
      String value_Locator) throws Exception /* Non Optional */
  {
    double elapsed_Seconds = 0;
    WebElement element_01 = null;
    Exception aa = null;
    /* logger.info(elapsed_Seconds); */ /* Debug */
    long tStart = System.currentTimeMillis();
      for (int i = 0; i < 1000; i++) {
      try {
        switch (type_Locator.toLowerCase()) {
          case "xpath":
            element_01 = webdriver.findElement(By.xpath(value_Locator));
            return element_01;
          case "id":
            element_01 = webdriver.findElement(By.id(value_Locator));
            return element_01;
          case "classname":
            element_01 = webdriver.findElement(By.className(value_Locator));
            return element_01;
          case "name":
            element_01 = webdriver.findElement(By.name(value_Locator));
            return element_01;
          case "cssselector":
            element_01 = webdriver.findElement(By.cssSelector(value_Locator));
            return element_01;
          case "linktext":
            element_01 = webdriver.findElement(By.linkText(value_Locator));
            return element_01;
        }
      } catch (Exception e) {
        elapsed_Seconds = (System.currentTimeMillis() - tStart) / 1000.0;
          logger.info("Did not find element");
          logger.info(elapsed_Seconds);
        if (elapsed_Seconds > 12) {
          logger.info(elapsed_Seconds);
          logger.info("After UnSuccessfull Find - Error:" + type_Locator + ":" + value_Locator);
          i = 9999;
          throw e;
        }
      }

      elapsed_Seconds = (System.currentTimeMillis() - tStart) / 1000.0;
          logger.info(elapsed_Seconds);
          logger.info("try Next");

      if (elapsed_Seconds > 12) {
        logger.info(elapsed_Seconds);
        logger.info("After Successfull Find - Too Long - Check Performance");
        i = 9999;
        throw new Exception(
            "Element Not Found after 12 Seconds:" + type_Locator + ":" + value_Locator);
      }
    }
    display("Trying to find BY:" + type_Locator + ":" + value_Locator);
    return element_01;
  }

  public static WebElement find_Element(WebDriver webdriver, String locator_Yaml)
      throws Exception /* Non Optional */
  {
    Map locator = getLocator(locator_Yaml);
    String loc = locator.get("Locator").toString();
    String val = locator.get("Value").toString();
    return find_Element(webdriver, loc, val);
  }

  public static WebElement find_Element(WebDriver webdriver, String type_Locator,
      String value_Locator, Boolean optional_Check) throws Exception /* Optional */
  {
    WebElement element_01 = null;
    try {
      element_01 = find_Element(webdriver, type_Locator, value_Locator);
      return element_01;
    } catch (Exception e) {
      return null;
    }
  }

  public static WebElement find_Element(WebDriver webdriver, String locator_Yaml,
      Boolean check_Optional) throws Exception /* Optional */
  {
    Map locator = getLocator(locator_Yaml);
    String loc = locator.get("Locator").toString();
    String val = locator.get("Value").toString();
    WebElement element_01 = null;
    try {
      element_01 = find_Element(webdriver, loc, val);
      return element_01;
    } catch (Exception e) {
      return null;
    }
  }

  /*
   * -----------------------------------------------------------------------------------------------
   * ------------- Non Find elements
   * ____________________________________________________________________________________________________________
   */
  public static void verify_Text(WebDriver webdriver, String locator_Yaml, String text_to_Verify)
      throws Exception {
    Map loc = getLocator(locator_Yaml);
    assertEquals(find_Element(webdriver, loc.get("Locator").toString(), loc.get("Value").toString())
        .getText(), text_to_Verify);
  }

  public static void verify_Text(WebDriver webdriver, String loc_Yml, String val_Yml,
      String text_to_Verify) throws Exception {
    assertEquals(find_Element(webdriver, loc_Yml, val_Yml).getText(), text_to_Verify);
  }

  public static void click_Element(WebDriver webdriver, String type_Locator, String value_Locator)
      throws Exception {
    find_Element(webdriver, type_Locator, value_Locator).click();
  }

  public static void accept_Alert(WebDriver webDriver) throws Exception {
    // If alert not present Throw error after few tries
    for (int i = 0; i < 14; i++) {
      try {
        webDriver.switchTo().alert().accept();
        return;
      } catch (Exception e) {
        if (i >= 14) {
          throw new Exception("Alert Not found");
        } else {
          // display("Trying to Accept Alert");
          Thread.sleep(300);
        }
      }
    }
    throw new Exception("Alert Not found");
  }

  public static void accept_Alert(WebDriver webDriver, int counter) throws Exception {
    // If alert not present its fine.
    for (int i = 0; i < counter; i++) {
      try {
        webDriver.switchTo().alert().accept();
        return;
      } catch (Exception e) {
        // display("Trying to Accept Alert");
        Thread.sleep(300);
      }
    }
  }

  public static void click_Element(WebDriver webDriver, String locator_Yaml) throws Exception {
    try {

      long tStart = System.currentTimeMillis();
      for (int i = 0; i < 9900000; i++) {
        // Start Measuring
        double elapsed_Seconds = (System.currentTimeMillis() - tStart) / 1000.0;
        Map locator = getLocator(locator_Yaml);
        String loc = locator.get("Locator").toString();
        String val = locator.get("Value").toString();
        WebElement get_Element = find_Element(webDriver, loc, val);
        // display(get_Element.getText()); // display(get_Element.getAttribute("innerHTML")); //
        // //Debug
        Dimension get_Element_D = get_Element.getSize();
        if (get_Element_D.getWidth() > 0 && get_Element_D.getHeight() > 0
            && get_Element.isEnabled()) {
          get_Element.click();
          return;
        }

        if (elapsed_Seconds > 12)
          throw new Exception(
              "Unable to click element as Either not displayed to Selenium Click or Hidden");
      }
    } catch (Exception e) {
      display(e.toString());
      throw e;
    }
  }

  public static void setText_Element(WebDriver webDriver, String locator_Yaml, String textVal)
      throws Exception {
    try {

      long tStart = System.currentTimeMillis();
      for (int i = 0; i < 9900000; i++) {
        // Start Measuring
        double elapsed_Seconds = (System.currentTimeMillis() - tStart) / 1000.0;
        Map locator = getLocator(locator_Yaml);
        String loc = locator.get("Locator").toString();
        String val = locator.get("Value").toString();
        WebElement get_Element = find_Element(webDriver, loc, val);
        // display(get_Element.getText()); // display(get_Element.getAttribute("innerHTML")); //
        // //Debug

        if (get_Element.getSize().getWidth() > 0 && get_Element.getSize().getHeight() > 0
            && get_Element.isEnabled()) {
          get_Element.click();
          try { get_Element.clear(); } catch (Exception e) { display("We are good"); }
          get_Element.sendKeys(textVal);
          return;
        }

        if (elapsed_Seconds > 12)
          throw new Exception("Unable to click element as Either not displayed to Selenium Click or Hidden");
      }

    } catch (Exception e) {
      display(e.toString());
      throw e;
    }
  }

  /*
   * -----------------------------------------------------------------------------------------------
   * ------------- Common Methods
   * ____________________________________________________________________________________________________________
   */

  public static String get_currentTimestamp()throws  Exception{
      String timestamp = new Timestamp(System.currentTimeMillis()).toString().replaceAll("(-|:|\\.|\\s)","");
      return timestamp;
  }

  public static WebDriver set_Timeouts(WebDriver webDriver) throws Exception {
    webDriver.manage().timeouts().pageLoadTimeout(40, TimeUnit.SECONDS);
    webDriver.manage().timeouts().setScriptTimeout(40, TimeUnit.SECONDS);
    webDriver.manage().timeouts().implicitlyWait(8, TimeUnit.SECONDS);
    return webDriver;
  }

  public static WebDriver set_Timeouts(WebDriver webDriver, int pageLoad, int script, int wait) throws Exception {
    webDriver.manage().timeouts().pageLoadTimeout(pageLoad, TimeUnit.SECONDS);
    webDriver.manage().timeouts().setScriptTimeout(script, TimeUnit.SECONDS);
    webDriver.manage().timeouts().implicitlyWait(wait, TimeUnit.SECONDS);
    return webDriver;
  }

  public static void display(String sme) throws Exception {
    LogManager.getLogger(CommonApplicationMethods.class.getName()).info(sme);
  }

  public static void focus_window() throws AWTException, InterruptedException {
    final Robot robot = new Robot();
    robot.mouseMove(300, 300);
    robot.mousePress(InputEvent.BUTTON3_DOWN_MASK);
    Thread.sleep(700);
    robot.mouseRelease(InputEvent.BUTTON3_DOWN_MASK);
    Thread.sleep(700);
    robot.keyPress(KeyEvent.VK_ESCAPE);
    robot.keyRelease(KeyEvent.VK_ESCAPE);
    Thread.sleep(700);
    robot.keyPress(KeyEvent.VK_ESCAPE);
    robot.keyRelease(KeyEvent.VK_ESCAPE);
    Thread.sleep(700);
  }

  public static void clear_Env_Chrome() throws InterruptedException, IOException {
    if (System.getProperty("os.name").startsWith("Windows")) {
      Runtime rt = Runtime.getRuntime();
      rt.exec("Taskkill /IM chrome.exe /F");
      rt.exec("Taskkill /IM chromedriver.exe /F");
      rt.exec("Taskkill /IM firefox.exe /F");
      Thread.sleep(1000); // Deepa Sleep needed here.
    }
    if (isUnix(systemType())) {
      Runtime rt = Runtime.getRuntime();
      rt.exec("ps aux | grep chrome | awk ' { print $2 } ' | xargs kill	 -9");
      rt.exec("ps aux | grep chromedriver | awk ' { print $2 } ' | xargs kill	 -9");
      rt.exec("ps aux | grep firefox | awk ' { print $2 } ' | xargs kill	 -9");
      Thread.sleep(1000); // Deepa Sleep needed here
    }
  }

  public static boolean get_Stop_Execution_Flag() throws Exception {

    String filePath = FixtureUtils.rootDirExecutionFile();
    File f = new File(filePath);
    if (f.exists() && !f.isDirectory()) {
      YamlReader reader = new YamlReader(new FileReader(filePath));
      Object object = reader.read();
      Map map = (Map) object;
      String value = map.get("Should_Execution_Stop").toString();
      if (value.toUpperCase().equals("TRUE")) {
        reader.close();
        throw new Error(
            "Stop Execution - Hard Stop Requested Was Requested, Should Reset Automatically At the end");
      }
    }
    return false;
  }

  public static Map getLocator(String locatorName) throws YamlException, FileNotFoundException {
    YamlReader reader =
        new YamlReader(new FileReader(FixtureUtils.fixturesDir() + "Locators.yaml"));
    Object object = reader.read(); // System.out.println(object);
    Map map = (Map) object; // System.out.println(map.get(locatorName));
    return (Map) map.get(locatorName);
  }

  public static void take_ScreenShot_TestCaseName(WebDriver webDriver, String[] stringValueArray)
      throws Exception {
    File src = ((TakesScreenshot) webDriver).getScreenshotAs(OutputType.FILE);
    String time = Integer.toString((int) (new Date().getTime() / 1000));
    display(time);

    try {
      // now copy the screenshot to the screenshot folder.
      if (stringValueArray.length == 2) {
        FileUtils.copyFile(src, new File(
            FixtureUtils.get_SS_Dir() + stringValueArray[0] + stringValueArray[1] + time + ".png"));
      } else {
        FileUtils.copyFile(src,
            new File(FixtureUtils.get_SS_Dir() + stringValueArray[0] + "Exception" + ".png"));
      }
    } catch (IOException e) {
      throw e;
    }
  }

  public static void take_Desktop_SShot_TestCaseName(String[] stringValueArray) throws Exception {
    Robot robot = new Robot();
    SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd hh mm ss a");
    Calendar now = Calendar.getInstance();
    BufferedImage screenShot =
        robot.createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));
    if (stringValueArray.length == 2) {
      ImageIO.write(screenShot, "JPG",
          new File(FixtureUtils.get_SS_Dir() + stringValueArray[0] + stringValueArray[1] + ".jpg"));
    } else {
      ImageIO.write(screenShot, "JPG",
          new File(FixtureUtils.get_SS_Dir() + stringValueArray[0] + "_Exception" + ".jpg"));
    }
  }

  /*
   * -----------------------------------------------------------------------------------------------
   * ------------- Application Specific Common Methods
   * ____________________________________________________________________________________________________________
   */

  public static void deleteApplication(WebDriver webDriver, String type_Of_App,
      String status_Of_App) throws Exception {

    List<WebElement> deleteElem = null;
    switch (type_Of_App.toLowerCase() + status_Of_App.toLowerCase()) {
      case "edwosbdraft":
        deleteElem = find_Elements(webDriver, "SBA_Application_All_Cases_Page_WOSB_Draft", true);
        break;
      case "wosbdraft":
        deleteElem = find_Elements(webDriver, "SBA_Application_All_Cases_Page_EDWOSB_Draft", true);
        break;
      case "mppdraft":
        deleteElem = find_Elements(webDriver, "SBA_Application_All_Cases_Page_MPP_Draft", true);
        break;
    }
    if (deleteElem.size() > 0) {
      deleteElem.get(0).click();
      accept_Alert(webDriver, 8);
    }
  }

  public static void clickOnApplicationAllCasesPage(WebDriver webDriver, String type_Of_App)
      throws Exception {
    // It should be in Vendor Dashboard
    switch (type_Of_App.toLowerCase()) {
      case "wosb":
        click_Element(webDriver, "SBA_Application_All_Cases_Page_WOSB");
      case "edwosb":
        click_Element(webDriver, "SBA_Application_All_Cases_Page_EDWOSB");
      case "mpp":
        click_Element(webDriver, "SBA_Application_All_Cases_Page_MPP");
    }
  }

  public static String returnOrganization_Id(String duns_Number) throws Exception {
    String organization_Id;
    try {

      Thread.sleep(3000); /*
                           * DEEPA: Sleep is needed here since we are querying SQL, and its too fast
                           * See below Start
                           */
      organization_Id = DatabaseUtils.queryForData(
          "select id from sbaone.organizations where duns_number = '" + duns_Number + "'", 1,
          1)[0][0];
    } catch (Exception e) {
      display(e.toString() + ": The Duns number retreival has failed");
      throw e;
    }
    return organization_Id;
  }

  public static void createApplication(WebDriver webDriver, String type_Of_App) throws Exception {
    navigationMenuClick(webDriver, "Programs");
    switch (type_Of_App.toUpperCase()) {
      case "EDWOSB":
        click_Element(webDriver, "JoinNewPgm_Create_App_EDWOSB");
        break;
      case "WOSB":
        click_Element(webDriver, "JoinNewPgm_Create_App_WOSB");
        break;
      case "MPP":
        click_Element(webDriver, "JoinNewPgm_Create_App_MPP");
        break;
      case "8A":
        click_Element(webDriver, "JoinNewPgm_Create_App_8A");
        break;
      default:
        // Assert.assertEquals("Edwosb or WOSB or MPP or 8a", "Not Found");
    }
    click_Element(webDriver, "JoinNewPgm_Add_Cert");
    click_Element(webDriver, "Application_Common_Accept_Button");
  }

  public static void searchDuns_Number(WebDriver webDriver, String search_Text) throws Exception {
    click_Element(webDriver, "Search_Duns_Search_Text");
    setText_Element(webDriver, "Search_Duns_Search_Query", search_Text);
    click_Element(webDriver, "Search_Duns_Search_Submit");
  }

  public static void non_Vendor_searchDuns_Number(WebDriver webDriver, String search_Text)
      throws Exception {
    click_Element(webDriver, "Search_Duns_Search_Text_Non_Vendor");
    setText_Element(webDriver, "Search_Duns_Search_Text_Non_Vendor", search_Text);
    click_Element(webDriver, "Search_Duns_Search_Submit_Non_Vendor");
  }

  public static void casesPageSearch(WebDriver webDriver, String searchValue) throws Exception {
    CommonApplicationMethods.setText_Element(webDriver, "Apllication_Case_Search_Text",
        searchValue);
    CommonApplicationMethods.click_Element(webDriver, "Apllication_Case_Search_Button");
  }

  public static void search_Cases_Duns_Number_Table(WebDriver webDriver, String search_Text)
      throws Exception {
    CommonApplicationMethods.setText_Element(webDriver, "SBA_CaseTable_Search", search_Text);
    CommonApplicationMethods.click_Element(webDriver, "Search_Duns_Cases_Submit");
  }

  public static void navigationMenuClick(WebDriver webDriver, String which_Button)
      throws Exception {
    switch (which_Button.toUpperCase()) {
      case "LOGOUT":
        click_Element(webDriver, "Navigation_Logout");
        break;
      case "HELP":
        click_Element(webDriver, "Navigation_Help");
        break;
      case "CASES":
        click_Element(webDriver, "Navigation_Cases");
        break;
      case "PROGRAMS":
        click_Element(webDriver, "Navigation_Programs");
        break;
      case "DASHBOARD":
        click_Element(webDriver, "Navigation_Dashboard");
        break;
      case "BUSINESS":
        click_Element(webDriver, "Navigation_Business");
        break;
      case "DOCUMENTS":
        click_Element(webDriver, "Navigation_Documents");
        break;
      case "HOME":
        click_Element(webDriver, "Navigation_Home");
        break;
      default:
        // Assert.assertEquals("Navigation Menu Not correct", "among present Options");
    }
  }

  public static void navigationBarClick(WebDriver webDriver, String which_Button) throws Exception {
    switch (which_Button.toUpperCase()) {
      case "LOGOUT":
        click_Element(webDriver, "Navigation_Bar_Profile");
        click_Element(webDriver, "Navigation_Bar_Logout");
        break;
      case "SETTINGS":
        click_Element(webDriver, "Navigation_Bar_Profile");
        click_Element(webDriver, "Navigation_Bar_Settings");
        break;
      case "HELP":
        click_Element(webDriver, "Navigation_Bar_Help");
        break;
      case "CASES":
        click_Element(webDriver, "Navigation_Bar_Cases");
        break;
      case "PROGRAMS":
        click_Element(webDriver, "Navigation_Bar_Programs");
        break;
      case "DASHBOARD":
        click_Element(webDriver, "Navigation_Bar_Dashboard");
        break;
      case "BUSINESS":
        click_Element(webDriver, "Navigation_Bar_Business");
        break;
      case "DOCUMENTS":
        click_Element(webDriver, "Navigation_Bar_Documents");
        break;
      case "HOME":
        click_Element(webDriver, "Navigation_Bar_Home");
        break;
      default:
        // Assert.assertEquals("Navigation Menu Not correct", "among present Options");
    }
  }

  public static String getflagvalue() throws Exception {
    String flagforRunfile = FixtureUtils.fixturesDir() + "flagforRunEmailNotification.config";
    BufferedReader bufferedReader = new BufferedReader(new FileReader(flagforRunfile));
    String detailFlag = bufferedReader.readLine();
    bufferedReader.close();
    return detailFlag;
  }

  public static Boolean checkApplicationExists(WebDriver webDriver, String type_Of_App,
      String status_Of_App) throws Exception {
    // It should be in Vendor Dashboard
    String xp = "";
    switch (type_Of_App.toLowerCase((Locale.ENGLISH))
        + status_Of_App.toLowerCase((Locale.ENGLISH))) {
      case "edwosbactive":
        xp = "//table[@id='certifications']/tbody/tr[ (td[position()=5 and contains(text(),'ctive')]) and  (td[position()=1]/a[contains(text(),'EDWOSB')]) ]";
        return find_Elements(webDriver, "xpath", xp).size() > 0;
      case "wosbactive":
        xp = "//table[@id='certifications']/tbody/tr[ (td[position()=5 and contains(text(),'ctive')]) and (td[position()=1]/a[contains(text(),'WOSB') and not(contains(text(),'EDWOSB'))]) ]";
        return find_Elements(webDriver, "xpath", xp).size() > 0;
      case "mpppending":
        xp = "//table[@id='certifications']/tbody/tr[  (td[position()=5 and contains(text(),'ending')]) and (td/a[position()=1 and contains(text(),'MPP')]) ]";
        return find_Elements(webDriver, "xpath", xp).size() > 0;
      default:
        return false;
    }
  }

}
