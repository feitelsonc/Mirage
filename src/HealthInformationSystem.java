import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class HealthInformationSystem {

	// The name of the MySQL account to use (or empty for anonymous)
	public static final String userName = "root";

	// The password for the MySQL account (or empty for anonymous)
	public static final String password = "";

	// The name of the computer running MySQL
	public static final String serverName = "localhost";

	// The port of the MySQL server (default is 3306)
	public static final int portNumber = 3306;

	// The name of the database we are testing with (this default is installed with MySQL)
	public static final String dbName = "HealthInformationSystem";
	
	// The name of the tables we are testing with
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

	
	// Creates HealthInformationSystem database and returns connection to it. Throws SQLException if creation of HealthInformationSystem fails
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
			// Attempt to create HealthInformationSystem database
			initDB = "CREATE DATABASE IF NOT EXISTS " + dbName;
		    this.executeUpdate(conn, initDB);
//			System.out.println("Created Database");
		} catch (SQLException e) {
//			System.out.println(dbName + " Exists");
			// Creation of HealthInformationSystem database failed because it already exists so delete the existing HealthInformationSystem and create a new one
			try {
				initDB = "DROP DATABASE " + dbName;
			    this.executeUpdate(conn, initDB);
//			    System.out.println("Dropped Existing Database");
				initDB = "CREATE DATABASE " + dbName;
			    this.executeUpdate(conn, initDB);
//				System.out.println("Created Database");
			} catch (SQLException e2) {
				// Creation of new HealthInformationSystem database failed so throw error
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

	// Method to execute a SQL statement
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
	
	// Creates all tables in HealthInformationSystem database
	public void initializeDB() {

		String createTable;
//		String messageExchangeString;
		
//		boolean ifMessagesExist = false;
		
		// Connect to MySQL
		Connection conn = null;
//		Connection connMessage = null;
		
//		Properties connectionProps = new Properties();
//		connectionProps.put("user", this.userName);
//		connectionProps.put("password", this.password);
		
		try {
			conn = this.getConnection();
//			connMessage = DriverManager.getConnection("jdbc:mysql://"
//					+ this.serverName + ":" + this.portNumber,
//					connectionProps);
//			System.out.println("Connected to database");
		} catch (SQLException e) {
			System.out.println("ERROR: Could not connect to the database");
			e.printStackTrace();
			return;
		}
		
		
		//TODO: Add delete constraints example: CASCADE
		
		// Create table Insurance
		try {
			createTable = "CREATE TABLE  IF NOT EXISTS " + TableNameInsurance + " ( " +
					"PayerId varchar(100), " +
					"Name varchar(100) DEFAULT NULL, " +
					"PRIMARY KEY (PayerId)" +
					")";
		    this.executeUpdate(conn, createTable);
//			System.out.println("Created table " + TableNameInsurance);
	    } catch (SQLException e) {
			System.out.println("ERROR: Could not create the table " + TableNameInsurance);
			e.printStackTrace();
			return;
		}
		
		// Create table Guardian
		try {
			createTable = "CREATE TABLE IF NOT EXISTS " + TableNameGuardian + " ( " +
					"GuardianNo varchar(100), " +
					"Phone varchar(100) DEFAULT NULL, " +
					"Address varchar(100) DEFAULT NULL, " +
					"State varchar(100) DEFAULT NULL, " +
					"GivenName varchar(100) DEFAULT NULL, " +
					"FamilyName varchar(100) DEFAULT NULL, " +
					"City varchar(100) DEFAULT NULL, " +
					"Zip varchar(100) DEFAULT NULL, " +
					"PRIMARY KEY (GuardianNo) " +
					")";
			this.executeUpdate(conn, createTable);
//			System.out.println("Created table " + TableNameGuardian);
		} catch (SQLException e) {
			System.out.println("ERROR: Could not create the table " + TableNameGuardian);
			e.printStackTrace();
			return;
		}

		// Create table Patient
		try {
			createTable = "CREATE TABLE IF NOT EXISTS " + TableNamePatient + " ( " +
			        "PatientId varchar(100), " +
		    		"FamilyName varchar(100) DEFAULT NULL, " +
			        "GivenName varchar(100) DEFAULT NULL, " +
		    		"Suffix varchar(100) DEFAULT NULL, " +
		    		"BirthTime varchar(100) DEFAULT NULL, " +
		    		"Gender varchar(100) DEFAULT NULL, " +
		    		"ProviderId varchar(100) DEFAULT NULL, " +
		    		"xmlHealthCreationDateTime varchar(100) NOT NULL, " +
		    		"GuardianNo varchar(100) DEFAULT NULL, " +
		    		"PayerId varchar(100) DEFAULT NULL, " +
		    		"PatientRole varchar(100) DEFAULT NULL, " +
		    		"PolicyType varchar(100) DEFAULT NULL, " +
		    		"PolicyHolder varchar(100) DEFAULT NULL, " +
		    		"Purpose varchar(100) DEFAULT NULL, " +
		    		"PRIMARY KEY (PatientId), " +
		    		"FOREIGN KEY (GuardianNo) REFERENCES Guardian (GuardianNo), " +
		    		"FOREIGN KEY (PayerId) REFERENCES Insurance (PayerId)" +
		    		")";
		    this.executeUpdate(conn, createTable);
//			System.out.println("Created table " + TableNamePatient);
	    } catch (SQLException e) {
			System.out.println("ERROR: Could not create the table " + TableNamePatient);
			e.printStackTrace();
			return;
		}
		
		// Create table LabTestReportOf
		try {
			createTable = "CREATE TABLE IF NOT EXISTS " + TableNameLabTestReportOf + " ( " +
					"LabTestResultId varchar(100), " +
					"LabTestType varchar(100) DEFAULT NULL, " +
					"ReferenceRangeHigh varchar(100) DEFAULT NULL, " +
					"PatientVisitId varchar(100) DEFAULT NULL, " +
					"LabTestPerformedDate varchar(100) DEFAULT NULL, " +
					"TestResultValue varchar(100) DEFAULT NULL, " +
					"ReferenceRangeLow varchar(100) DEFAULT NULL, " +
					"PatientId varchar(100) NOT NULL, " +
					"PRIMARY KEY (LabTestResultId), " +
					"FOREIGN KEY (PatientId) REFERENCES Patient (PatientId) " +
					")";
		    this.executeUpdate(conn, createTable);
//			System.out.println("Created table " + TableNameLabTestReportOf);
	    } catch (SQLException e) {
			System.out.println("ERROR: Could not create the table " + TableNameLabTestReportOf);
			e.printStackTrace();
			return;
		}
		
		// Create table FamilyMemberOfPatient
		try {
			createTable = "CREATE TABLE IF NOT EXISTS " + TableNameFamilyMemberOfPatient + " ( " +
					"Id varchar(100), " +
					"Age varchar(100) DEFAULT NULL, " +
					"Relationship varchar(100) DEFAULT NULL, " +
					"Diagnosis varchar(100) DEFAULT NULL, " +
					"PatientId varchar(100) NOT NULL, " +
					"PRIMARY KEY (Id, PatientId), " +
					"FOREIGN KEY (PatientId) REFERENCES Patient (PatientId) " +
					")";
		    this.executeUpdate(conn, createTable);
//			System.out.println("Created table " + TableNameFamilyMemberOfPatient);
	    } catch (SQLException e) {
			System.out.println("ERROR: Could not create the table " + TableNameFamilyMemberOfPatient);
			e.printStackTrace();
			return;
		}
		
		// Create table Author
		try { 
			createTable = "CREATE TABLE IF NOT EXISTS " + TableNameAuthor + " ( " +
					"AuthorId varchar(100), " +
					"AuthorFirstName varchar(100) DEFAULT NULL, " +
					"AuthorTitle varchar(100) DEFAULT NULL, " +
					"AuthorLastName varchar(100) DEFAULT NULL, " +
					"PRIMARY KEY (AuthorId) " +
					")";
		    this.executeUpdate(conn, createTable);
//			System.out.println("Created table " + TableNameAuthor);
	    } catch (SQLException e) {
			System.out.println("ERROR: Could not create the table " + TableNameAuthor);
			e.printStackTrace();
			return;
		}
		
		// Create table AssignedTo
		try {
			createTable = "CREATE TABLE IF NOT EXISTS " + TableNameAssignedTo + " ( " +
					"AuthorId varchar(100) NOT NULL, " +
					"PatientId varchar(100) NOT NULL, " +
					"ParticipatingRole varchar(100) DEFAULT NULL, " +
					"PRIMARY KEY (AuthorId, PatientId), " +
					"FOREIGN KEY (AuthorId) REFERENCES Author (AuthorId), " +
					"FOREIGN KEY (PatientId) REFERENCES Patient (PatientId)" +
					")";
		    this.executeUpdate(conn, createTable);
//			System.out.println("Created table " + TableNameAssignedTo);
	    } catch (SQLException e) {
			System.out.println("ERROR: Could not create the table " + TableNameAssignedTo);
			e.printStackTrace();
			return;
		}
		
		// Create table Substance
		try {
			createTable = "CREATE TABLE IF NOT EXISTS " + TableNameSubstance + " ( " +
					"SubstanceId varchar(100), " +
					"SubstanceName varchar(100) DEFAULT NULL, " +
					"PRIMARY KEY (SubstanceId)" +
					")";
			this.executeUpdate(conn, createTable);
//			System.out.println("Created table " + TableNameSubstance);
		} catch (SQLException e) {
			System.out.println("ERROR: Could not create the table " + TableNameSubstance);
			e.printStackTrace();
			return;
		}
		
		// Create table AllergicTo
		try {
			createTable = "CREATE TABLE IF NOT EXISTS " + TableNameAllergicTo + " ( " +
					"Reaction varchar(100) DEFAULT NULL, " +
					"Active varchar(100) DEFAULT NULL, " +
					"SubstanceId varchar(100) NOT NULL, " +
					"PatientId varchar(100) NOT NULL, " +
					"PRIMARY KEY (SubstanceId, PatientId), " +
					"FOREIGN KEY (SubstanceId) REFERENCES Substance (SubstanceId), " +
					"FOREIGN KEY (PatientId) REFERENCES Patient (PatientId) " +
					")";
			this.executeUpdate(conn, createTable);
//			System.out.println("Created table " + TableNameAllergicTo);
		} catch (SQLException e) {
			System.out.println("ERROR: Could not create the table " + TableNameAllergicTo);
			e.printStackTrace();
			return;
		}
		
		// Create table PlanScheduledFor
		try {
			createTable = "CREATE TABLE IF NOT EXISTS " + TableNamePlanScheduledFor + " ( " +
					"PlanId varchar(100), " +
					"PlanName varchar(100) DEFAULT NULL, " +
					"PatientId varchar(100) DEFAULT NULL, " +
					"DateScheduled varchar(100) DEFAULT NULL, " +
					"PRIMARY KEY (PlanId), " +
					"FOREIGN KEY (PatientId) REFERENCES Patient (PatientId) " +
					")";
			this.executeUpdate(conn, createTable);
//			System.out.println("Created table " + TableNamePlanScheduledFor);
		} catch (SQLException e) {
			System.out.println("ERROR: Could not create the table " + TableNamePlanScheduledFor);
			e.printStackTrace();
			return;
		}
		
	}
	
	// Entry point into program
	public static void main(String[] args) {
		HealthInformationSystem app = new HealthInformationSystem();
		app.initializeDB();
		Parser.execute();
		InterfaceManager.execute();
	}
}
