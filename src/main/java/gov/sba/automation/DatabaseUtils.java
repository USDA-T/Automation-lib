// created By deepa patri
package gov.sba.automation;

import com.opencsv.CSVParser;
import com.opencsv.CSVReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;

import java.io.FileReader;
import java.sql.*;
import java.util.Properties;

public class DatabaseUtils {

  private static final Logger logger = LogManager.getLogger(DatabaseUtils.class.getName());

  public static void executeSQLScript(String sql_query) throws Exception {
    Connection dbConnection = null;
    PreparedStatement sqlStatement = null;

    try {
      dbConnection = getDatabaseConnection();
      sqlStatement = dbConnection.prepareStatement(sql_query);
      int rows = sqlStatement.executeUpdate();
      logger.info("Your query effects " + rows + " rows");
    } finally {
      if (sqlStatement != null) {
        sqlStatement.close();
      }
      if (dbConnection != null) {
        dbConnection.close();

      }
    }
  }

  public static String[][] queryForData(String sqlQuery, int rowsNeeded, int colsNeeded)
      throws Exception {
    Connection dbConnection = null;
    ResultSet resultSet = null;

    try {

      dbConnection = getDatabaseConnection();

      Statement sqlStatement =
          dbConnection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);

      resultSet = sqlStatement.executeQuery(sqlQuery);

      if (!resultSet.first()) {
        throw new Exception("Problem fetching data using query: " + sqlQuery);
      }

      String[][] sqlData = new String[rowsNeeded][colsNeeded];

      for (int i = 0; i < rowsNeeded; i++) {
        for (int j = 1; j < colsNeeded + 1; j++) {
          sqlData[i][j - 1] = resultSet.getString(j);
        }
      }

      return sqlData;
    } catch (Exception e) {
      logger.error("Unexpected problem getting data:" + e.toString());
      throw e;
    } finally {
      if (resultSet != null) {
        resultSet.close();
      }
      if (dbConnection != null) {
        dbConnection.close();
      }
    }
  }

  public static Connection getDatabaseConnection() throws Exception {
    Properties props = ConfigUtils.loadDefaultProperties();
    return DriverManager.getConnection(
        "jdbc:postgresql://localhost:5432/sbaone_dev?user=postgres&password=password");
    // return DriverManager.getConnection(props.getProperty("db_url"),
    // props.getProperty("db_username"), props.getProperty("db_password"));
  }

  public static String returnOrganization_Id(String duns_Number) throws Exception {
    String organization_Id;
    try {
      organization_Id = DatabaseUtils.queryForData(
          "select id from sbaone.organizations where duns_number = '" + duns_Number + "'", 1,
          1)[0][0];
    } catch (Exception e) {
      logger.info(e.toString() + ": The Duns number retreival has failed");
      throw e;
    }
    return organization_Id;
  }

  /**
   * Simplify implementation of how we should find good row having the available DUNS number to use!
   * 
   * @return
   * @throws Exception
   */
  public static String[] findUnusedDunsNumber() throws Exception {
    String csvFile = FixtureUtils.resourcesDir()
        + ConfigUtils.loadDefaultProperties().getProperty("fixture_file");

    CSVReader reader = new CSVReader(new FileReader(csvFile), CSVParser.DEFAULT_SEPARATOR,
        CSVParser.DEFAULT_QUOTE_CHARACTER, 1);

    String[] detailFields;

    while ((detailFields = reader.readNext()) != null) {

      String email = detailFields[0];
      String password = detailFields[1];
      String dunsNumber = detailFields[2];
      int rowsNeeded = 1;
      int colsNeeded = 1;

      String certificateQuery =
          "select count(*) from sbaone.certificates where organization_id in (select id from sbaone.organizations where duns_number = '"
              + dunsNumber + "')";

      String[][] certificateData =
          DatabaseUtils.queryForData(certificateQuery, rowsNeeded, colsNeeded);

      String applicationQuery =
          "select count(*) from sbaone.sba_applications where organization_id in (select id from sbaone.organizations where duns_number = '"
              + dunsNumber + "')";

      String[][] applicationData =
          DatabaseUtils.queryForData(applicationQuery, rowsNeeded, colsNeeded);

      // If we can't find any combination then it means it is available?
      int counter = Integer.parseInt(certificateData[0][0].toString())
          + Integer.parseInt(applicationData[0][0].toString());

      if (counter <= 0) {
        logger.info(String.format("Found unused rows: %s->%s->%s", email, password, dunsNumber));
        return detailFields;
      }
    }
    // If we reach here we can't find any good Duns number, should just raise exception!
    throw new Exception("No valid Duns number available. Please check your fixture files");
  }

  public static void deleteApplication_SetCert_Set_App_Tables(WebDriver webDriver,
      Integer certificate_ID, String duns_Number) throws Exception {

    String organization_Id = returnOrganization_Id(duns_Number);
    DatabaseUtils.executeSQLScript(
        "update sbaone.certificates  set deleted_at = CURRENT_TIMESTAMP   where organization_id = "
            + organization_Id.toString());
    DatabaseUtils.executeSQLScript(
        "update sbaone.sba_applications  set deleted_at = CURRENT_TIMESTAMP  where organization_id = "
            + organization_Id.toString());

  }

  public static void deleteAllApplicationTypes(WebDriver webDriver, String duns_Number)
      throws Exception {
    // It should be in Vendor Dashboard
    deleteApplication_SetCert_Set_App_Tables(webDriver, 1, duns_Number);
    deleteApplication_SetCert_Set_App_Tables(webDriver, 2, duns_Number);
    deleteApplication_SetCert_Set_App_Tables(webDriver, 3, duns_Number);
    deleteApplication_SetCert_Set_App_Tables(webDriver, 4, duns_Number);

  }

  public static String[] findcontributoremail() throws Exception {
    String csvFile = FixtureUtils.resourcesDir()
        + ConfigUtils.loadDefaultProperties().getProperty("fixture_file");

    CSVReader reader = new CSVReader(new FileReader(csvFile), CSVParser.DEFAULT_SEPARATOR,
        CSVParser.DEFAULT_QUOTE_CHARACTER, 1);

    String[] detailFields;

    while ((detailFields = reader.readNext()) != null) {

      String emailaddress = detailFields[0];
      String password = detailFields[1];
      int rowsNeeded = 1;
      int colsNeeded = 1;

      String emailQuery =
          "select count(*) from sbaone.contributors where email = '" + emailaddress + "')";

      String[][] emailData = DatabaseUtils.queryForData(emailQuery, rowsNeeded, colsNeeded);


      String[][] applicationData = DatabaseUtils.queryForData(emailQuery, rowsNeeded, colsNeeded);

      // If we can't find any combination then it means it is available?
      int counter = Integer.parseInt(emailData[0][0].toString())
          + Integer.parseInt(applicationData[0][0].toString());

      if (counter <= 0) {
        logger.info(String.format("Found unused rows: %s->%s->%s", emailaddress, password));
        return detailFields;
      }
    }
    // If we reach here we can't find any good Duns number, should just raise exception!
    throw new Exception("No valid email available. Please check your fixture files");
  }
}
