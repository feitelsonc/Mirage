import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

/**
 * This class demonstrates how to connect to MySQL and run some basic commands.
 * 
 * In order to use this, you have to download the Connector/J driver and add
 * its .jar file to your build path.  You can find it here:
 * 
 * http://dev.mysql.com/downloads/connector/j/
 * 
 * You will see the following exception if it's not in your class path:
 * 
 * java.sql.SQLException: No suitable driver found for jdbc:mysql://localhost:3306/
 * 
 * To add it to your class path:
 * 1. Right click on your project
 * 2. Go to Build Path -> Add External Archives...
 * 3. Select the file mysql-connector-java-5.1.24-bin.jar
 *    NOTE: If you have a different version of the .jar file, the name may be
 *    a little different.
 *    
 * The user name and password are both "root", which should be correct if you followed
 * the advice in the MySQL tutorial. If you want to use different credentials, you can
 * change them below. 
 * 
 * You will get the following exception if the credentials are wrong:
 * 
 * java.sql.SQLException: Access denied for user 'userName'@'localhost' (using password: YES)
 * 
 * You will instead get the following exception if MySQL isn't installed, isn't
 * running, or if your serverName or portNumber are wrong:
 * 
 * java.net.ConnectException: Connection refused
 */
public class HealthInformationSystem {

	/** The name of the MySQL account to use (or empty for anonymous) */
	private final String userName = "root";

	/** The password for the MySQL account (or empty for anonymous) */
	private final String password = "";

	/** The name of the computer running MySQL */
	private final String serverName = "localhost";

	/** The port of the MySQL server (default is 3306) */
	private final int portNumber = 3306;

	/** The name of the database we are testing with (this default is installed with MySQL) */
	public static final String dbName = "HealthInformationSystem";
	
	/** The name of the table we are testing with */
	public static final String TableNamePatient = "Patient";
	public static final String TableNameGuardian = "Guardian";
	public static final String TableNameLabTestReportOf = "Lab_Test_Report_Of";
	public static final String TableNameFamilyMemberOfPatient = "Family_Member_Of_Patient";
	public static final String TableNameAuthor = "Author";
	public static final String TableNameAssignedTo = "Assigned_To";
	public static final String TableNameInsurance = "Insurance";
	public static final String TableNameSubstance = "Substance";
	public static final String TableNameAllergicTo = "Allergic_To";
	public static final String TableNamePlanScheduledFor = "Plan_Scheduled_For";

	
	/**
	 * Get a new database connection
	 * 
	 * @return
	 * @throws SQLException
	 */
	public Connection getConnection() throws SQLException {
		Connection conn = null;
		String initDB;
		Properties connectionProps = new Properties();
		connectionProps.put("user", this.userName);
		connectionProps.put("password", this.password);

		conn = DriverManager.getConnection("jdbc:mysql://"
				+ this.serverName + ":" + this.portNumber,
				connectionProps);
		
		try {
			initDB = "CREATE DATABASE " + dbName;
		    this.executeUpdate(conn, initDB);
			System.out.println("Created Database");
		} catch (SQLException e) {
			System.out.println(dbName + " Exists");
			try {
				initDB = "DROP DATABASE " + dbName;
			    this.executeUpdate(conn, initDB);
			    System.out.println("Dropped Existing Database");
				initDB = "CREATE DATABASE " + dbName;
			    this.executeUpdate(conn, initDB);
				System.out.println("Created Database");
			} catch (SQLException e2) {
				System.out.println("ERROR: FAILED TO DROP OR CREATE DATABASE: " + dbName);
				e.printStackTrace();
				throw new SQLException();
			}
		}
		
		conn = DriverManager.getConnection("jdbc:mysql://"
				+ this.serverName + ":" + this.portNumber + "/" + dbName,
				connectionProps);


		return conn;
	}

	/**
	 * Run a SQL command which does not return a recordset:
	 * CREATE/INSERT/UPDATE/DELETE/DROP/etc.
	 * 
	 * @throws SQLException If something goes wrong
	 */
	public boolean executeUpdate(Connection conn, String command) throws SQLException {
	    Statement stmt = null;
	    try {
	        stmt = conn.createStatement();
	        stmt.executeUpdate(command); // This will throw a SQLException if it fails
	        return true;
	    } finally {

	    	// This will run whether we throw an exception or not
	        if (stmt != null) { stmt.close(); }
	    }
	}
	
	/**
	 * Connect to MySQL and do some stuff.
	 */
	public void initializeDB() {

		String createTable;
		
		// Connect to MySQL
		Connection conn = null;
		try {
			conn = this.getConnection();
			System.out.println("Connected to database");
		} catch (SQLException e) {
			System.out.println("ERROR: Could not connect to the database");
			e.printStackTrace();
			return;
		}
		
		
		//TODO: Add delete constraints example: CASCADE
		
		// Create table Insurance
		try {
			createTable = "CREATE TABLE " + TableNameInsurance + " ( " +
					"PayerId CHAR(20), " +
					"Name CHAR(20), " +
					"PRIMARY KEY (PayerId)" +
					")";
		    this.executeUpdate(conn, createTable);
			System.out.println("Created table " + TableNameInsurance);
	    } catch (SQLException e) {
			System.out.println("ERROR: Could not create the table " + TableNameInsurance);
			e.printStackTrace();
			return;
		}
		
		// Create table Guardian
		try {
			createTable = "CREATE TABLE " + TableNameGuardian + " ( " +
					"GuardianNo CHAR(20), " +
					"Phone CHAR(20), " +
					"Address CHAR(20), " +
					"State CHAR(20), " +
					"GivenName CHAR(20), " +
					"FamilyName CHAR(20), " +
					"City CHAR(20), " +
					"Zip CHAR(20), " +
					"PRIMARY KEY (GuardianNo) " +
					")";
			this.executeUpdate(conn, createTable);
			System.out.println("Created table " + TableNameGuardian);
		} catch (SQLException e) {
			System.out.println("ERROR: Could not create the table " + TableNameGuardian);
			e.printStackTrace();
			return;
		}

		// Create table Patient
		try {
			createTable = "CREATE TABLE " + TableNamePatient + " ( " +
			        "PatientId CHAR(20), " +
		    		"FamilyName CHAR(20), " +
			        "GivenName CHAR(20), " +
		    		"Suffix CHAR(20), " +
		    		"BirthTime CHAR(20), " +
		    		"Gender CHAR(20), " +
		    		"ProviderId CHAR(20), " +
		    		"xmlHealthCreationDateTime CHAR(20), " +
		    		"GuardianNo CHAR(20) NOT NULL, " +
		    		"PayerId CHAR(20) NOT NULL, " +
		    		"PatientRole CHAR(20) NOT NULL, " +
		    		"PolicyType CHAR(20) NOT NULL, " +
		    		"Purpose CHAR(20) NOT NULL, " +
		    		"PRIMARY KEY (PatientId), " +
		    		"FOREIGN KEY (GuardianNo) REFERENCES Guardian (GuardianNo), " +
		    		"FOREIGN KEY (PayerId) REFERENCES Insurance (PayerId)" +
		    		")";
		    this.executeUpdate(conn, createTable);
			System.out.println("Created table " + TableNamePatient);
	    } catch (SQLException e) {
			System.out.println("ERROR: Could not create the table " + TableNamePatient);
			e.printStackTrace();
			return;
		}
		
		// Create table LabTestReportOf
		try {
			createTable = "CREATE TABLE " + TableNameLabTestReportOf + " ( " +
					"LabTestResultId CHAR(20), " +
					"LabTestType CHAR(20), " +
					"ReferenceRangeHigh CHAR(20), " +
					"PatientVisitId CHAR(20), " +
					"LabTestPerfromedDate CHAR(20), " +
					"TestResultValue CHAR(20), " +
					"ReferenceRangeLow CHAR(20), " +
					"PatientId CHAR(20) NOT NULL, " +
					"PRIMARY KEY (LabTestResultId), " +
					"FOREIGN KEY (PatientId) REFERENCES Patient (PatientId) " +
					")";
		    this.executeUpdate(conn, createTable);
			System.out.println("Created table " + TableNameLabTestReportOf);
	    } catch (SQLException e) {
			System.out.println("ERROR: Could not create the table " + TableNameLabTestReportOf);
			e.printStackTrace();
			return;
		}
		
		// Create table FamilyMemberOfPatient
		try {
			createTable = "CREATE TABLE " + TableNameFamilyMemberOfPatient + " ( " +
					"Id CHAR(20), " +
					"Age CHAR(20), " +
					"Relationship CHAR(20), " +
					"Diagnosis CHAR(20), " +
					"PatientId CHAR(20) NOT NULL, " +
					"PRIMARY KEY (Id, PatientId), " +
					"FOREIGN KEY (PatientId) REFERENCES Patient (PatientId) " +
					")";
		    this.executeUpdate(conn, createTable);
			System.out.println("Created table " + TableNameFamilyMemberOfPatient);
	    } catch (SQLException e) {
			System.out.println("ERROR: Could not create the table " + TableNameFamilyMemberOfPatient);
			e.printStackTrace();
			return;
		}
		
		// Create table Author
		try {
			createTable = "CREATE TABLE " + TableNameAuthor + " ( " +
					"AuthorId CHAR(20), " +
					"AuthorFirstName CHAR(20), " +
					"AuthorTitle CHAR(20), " +
					"AuthorLastName CHAR(20), " +
					"PRIMARY KEY (AuthorId) " +
					")";
		    this.executeUpdate(conn, createTable);
			System.out.println("Created table " + TableNameAuthor);
	    } catch (SQLException e) {
			System.out.println("ERROR: Could not create the table " + TableNameAuthor);
			e.printStackTrace();
			return;
		}
		
		// Create table AssignedTo
		try {
			createTable = "CREATE TABLE " + TableNameAssignedTo + " ( " +
					"AuthorId CHAR(20) NOT NULL, " +
					"PatientId CHAR(20) NOT NULL, " +
					"ParticipatingRole CHAR(20), " +
					"PRIMARY KEY (AuthorId, PatientId), " +
					"FOREIGN KEY (AuthorId) REFERENCES Author (AuthorId), " +
					"FOREIGN KEY (PatientId) REFERENCES Patient (PatientId)" +
					")";
		    this.executeUpdate(conn, createTable);
			System.out.println("Created table " + TableNameAssignedTo);
	    } catch (SQLException e) {
			System.out.println("ERROR: Could not create the table " + TableNameAssignedTo);
			e.printStackTrace();
			return;
		}
		
		// Create table Substance
		try {
			createTable = "CREATE TABLE " + TableNameSubstance + " ( " +
					"SubstanceId CHAR(20), " +
					"PRIMARY KEY (SubstanceId)" +
					")";
			this.executeUpdate(conn, createTable);
			System.out.println("Created table " + TableNameSubstance);
		} catch (SQLException e) {
			System.out.println("ERROR: Could not create the table " + TableNameSubstance);
			e.printStackTrace();
			return;
		}
		
		// Create table AllergicTo
		try {
			createTable = "CREATE TABLE " + TableNameAllergicTo + " ( " +
					"Reaction CHAR(20), " +
					"Active CHAR(20), " +
					"SubstanceId CHAR(20) NOT NULL, " +
					"PatientId CHAR(20) NOT NULL, " +
					"PRIMARY KEY (SubstanceId, PatientId), " +
					"FOREIGN KEY (SubstanceId) REFERENCES Substance (SubstanceId), " +
					"FOREIGN KEY (PatientId) REFERENCES Patient (PatientId) " +
					")";
			this.executeUpdate(conn, createTable);
			System.out.println("Created table " + TableNameAllergicTo);
		} catch (SQLException e) {
			System.out.println("ERROR: Could not create the table " + TableNameAllergicTo);
			e.printStackTrace();
			return;
		}
		
		// Create table PlanScheduledFor
		try {
			createTable = "CREATE TABLE " + TableNamePlanScheduledFor + " ( " +
					"PlanName CHAR(20) NOT NULL, " +
					"PatientId CHAR(20) NOT NULL, " +
					"DateScheduled CHAR(20) NOT NULL, " +
					"PRIMARY KEY (PlanName, PatientId, DateScheduled), " +
					"FOREIGN KEY (PatientId) REFERENCES Patient (PatientId) " +
					")";
			this.executeUpdate(conn, createTable);
			System.out.println("Created table " + TableNamePlanScheduledFor);
		} catch (SQLException e) {
			System.out.println("ERROR: Could not create the table " + TableNamePlanScheduledFor);
			e.printStackTrace();
			return;
		}
		
		/******************************************************************************/

		
//		// Add Foreign Key Constraints Patient
//		try {
//			
//			alterTable = "ALTER TABLE " + TableNamePatient + " ADD CONSTRAINT fk_GuardianNo FOREIGN KEY (GuardianNo) REFERENCES Guardian_Of (GuardianNo)";
////					"ADD FOREIGN KEY (PayerId) REFERENCES Insured_By (PayerId)";
//			this.executeUpdate(conn, alterTable);
//			System.out.println("Updated table " + TableNamePatient);
//		} catch (SQLException e) {
//			System.out.println("ERROR: Could not update the table " + TableNamePatient);
//			e.printStackTrace();
//			return;
//		}
//		
//		// Add Foreign Key Constraints GuardianOf
//		try {
//			stmt = conn.createStatement();
//			alterTable = "ALTER TABLE " + TableNameGuardianOf + " " +
//		    		"ADD FOREIGN KEY (PatientId) REFERENCES Patient (PatientId), " +
//		    		"ADD FOREIGN KEY (GuardianNo) REFERENCES Guardian (GuardianNo)";
//			stmt.execute(alterTable);
//			System.out.println("Updated table " + TableNameGuardianOf);
//		} catch (SQLException e) {
//			System.out.println("ERROR: Could not update the table " + TableNameGuardianOf);
//			e.printStackTrace();
//			return;
//		}

		
//		// Drop the table
//		try {
//		    String dropString = "DROP TABLE " + this.tableName;
//			this.executeUpdate(conn, dropString);
//			System.out.println("Dropped the table");
//	    } catch (SQLException e) {
//			System.out.println("ERROR: Could not drop the table");
//			e.printStackTrace();
//			return;
//		}
	}
	
	/**
	 * Connect to the DB and do some stuff
	 */
	public static void main(String[] args) {
		HealthInformationSystem app = new HealthInformationSystem();
		app.initializeDB();
	}
}
