// created By deepa patri
package gov.sba.automation;

import static com.opencsv.CSVWriter.DEFAULT_QUOTE_CHARACTER;
import static com.opencsv.CSVWriter.DEFAULT_SEPARATOR;
import static gov.sba.automation.ConfigUtils.loadDefaultProperties;
import static gov.sba.automation.FixtureUtils.resourcesDir;

import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;

import com.opencsv.CSVParser;
import com.opencsv.CSVReader;

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
    Properties p = loadDefaultProperties();
    return DriverManager.getConnection(p.getProperty("db_url"), p.getProperty("db_username"),
        p.getProperty("db_password"));
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

  public static String[] findUnusedDunsNumber(String type_Of_Business) throws Exception {
    String csvFile = resourcesDir() + loadDefaultProperties().getProperty("fixture_file");

    CSVReader reader =
        new CSVReader(new FileReader(csvFile), DEFAULT_SEPARATOR, DEFAULT_QUOTE_CHARACTER, 1);

    String[] detailFields;
    int counter;

    while ((detailFields = reader.readNext()) != null) {
      boolean business_Type_Matching_If_Passed = true;
      counter = 0;
      String email = detailFields[0];
      String password = detailFields[1];
      String dunsNumber = detailFields[2];
      int rowsNeeded = 1;
      int colsNeeded = 1;


      if (type_Of_Business.length() > 0) {
        String type_Of_Business_Query =
            "select count(*) from sbaone.organizations where duns_number = 'replace_Duns_Number' and business_type = 'replace_Business'"
                .replace("replace_Duns_Number", dunsNumber)
                .replace("replace_Business", type_Of_Business);
        String[][] business_data =
            DatabaseUtils.queryForData(type_Of_Business_Query, rowsNeeded, colsNeeded);
        int business_Type = Integer.parseInt(business_data[0][0].toString());
        if (business_Type > 0) {
          business_Type_Matching_If_Passed = true;
        } else {
          business_Type_Matching_If_Passed = false;
        }
      }

      if (business_Type_Matching_If_Passed) {
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
        counter = Integer.parseInt(certificateData[0][0]) + Integer.parseInt(applicationData[0][0]);

        if (counter <= 0) {
          logger.info(String.format("Found unused rows: %s->%s->%s", email, password, dunsNumber));
          reader.close();
          return detailFields;
        }
      }

    }
    reader.close();
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
    String csvFile = resourcesDir() + loadDefaultProperties().getProperty("fixture_file");

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
        reader.close();
        return detailFields;
      }
    }
    // If we reach here we can't find any good Duns number, should just raise exception!
    reader.close();
    throw new Exception("No valid email available. Please check your fixture files");
  }
}
