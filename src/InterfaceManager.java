import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;


public class InterfaceManager {

	private static String errorString = "Invalid input. Check spelling and try again.";

	public static void execute() {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String in;

		Connection con = null;
		String id;

		Properties connectionProps = new Properties();
		connectionProps.put("user", HealthInformationSystem.userName);
		connectionProps.put("password", HealthInformationSystem.password);

		try {
			con = DriverManager.getConnection("jdbc:mysql://"
					+ HealthInformationSystem.serverName + ":" + HealthInformationSystem.portNumber + "/" + HealthInformationSystem.dbName,
					connectionProps);
		} catch (SQLException e1) {
			e1.printStackTrace();
		}


		try {
			while (true) {
				System.out.println("Login as patient, doctor, or admin?\nor enter \"exit\" to exit HealthInformationSystem");
				in = br.readLine();

				if (in.toLowerCase().equals("patient")) {
					patientInterface(con);
				}
				else if (in.toLowerCase().equals("doctor")) {
					doctorInterface(con);
				}
				else if (in.toLowerCase().equals("admin")) {
					administratorInterface(con);
				}
				else if (in.toLowerCase().equals("exit")) {
					System.out.println("Exited system");
					return;
				}
				else {
					System.out.println("Login mode not recognized. Check spelling and try again.");
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void patientInterface(Connection con) {
		System.out.println("Entering patient mode.");
		System.out.println("Enter your patient ID.");
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String in = "";
		String patientId = "";
		boolean isValidId = false;
		while (!isValidId) {
			try {
				in = br.readLine();
				patientId = in;
				isValidId = checkIsValidPatientId(patientId, con);
				
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			if (!isValidId) {
				if (in.toLowerCase().equals("exit")) {
					return;
				}
				System.out.println("ID is not a valid patient ID. Try again or enter \"exit\" to quit.");	
			}
		}
		
		boolean quit = false;
		try {
			while (!quit) {
				System.out.println("Enter (1) to view all of your data");
				System.out.println("Enter (2) to edit your data");
				System.out.println("Enter (3) to logout");

				in = br.readLine();

				if (in.equals("1")) {
					quit = false;
					processPatientOp1(con, patientId);
				}
				else if (in.equals("2")) {
					quit = false;
//					processPatientOp2(con, patientId);
				}
				else if (in.equals("3") || in.toLowerCase().equals("logout")) {
					System.out.println("Logout out");
					quit = true;
				}
				else {
					quit = false;
					System.out.println(errorString);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private static void doctorInterface(Connection con) {
		System.out.println("Entering doctor mode.");

	}

	private static void administratorInterface(Connection con) {
		System.out.println("Entering administrator mode.");
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String in;
		boolean quit = false;
		try {
			while (!quit) {
				System.out.println("Enter (1) to view number of patients for each type of allergy");
				System.out.println("Enter (2) to view the patients who have more than one allergy");
				System.out.println("Enter (3) to view the patients who have a plan for today");
				System.out.println("Enter (4) to view authors who have more than one patient");
				System.out.println("Enter (5) to logout");

				in = br.readLine();

				if (in.equals("1")) {
					quit = false;
					processAdminOp1(con);
				}
				else if (in.equals("2")) {
					quit = false;
					processAdminOp2(con);
				}
				else if (in.equals("3")) {
					quit = false;
					processAdminOp3(con);
				}
				else if (in.equals("4")) {
					quit = false;
					processAdminOp4(con);
				}
				else if (in.equals("5") || in.toLowerCase().equals("logout")) {
					System.out.println("Logout out");
					quit = true;
				}
				else {
					quit = false;
					System.out.println(errorString);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private static void processAdminOp1(Connection con) {

		try {
			Statement st = null;
			ResultSet rs = null;
			String countNum;
			String SubstanceName;

			String query =
					"SELECT COUNT(*), S.SubstanceName "+ 
					"FROM Allergic_To A, Substance S "+
					"WHERE A.SubstanceId = S.SubstanceId " +
					"GROUP BY S.SubstanceName";

			st = con.createStatement();
			rs = st.executeQuery(query);

			while(rs.next()) {
				countNum = rs.getString("COUNT(*)");
				SubstanceName = rs.getString("SubstanceName");

				if (SubstanceName != null) {
					System.out.println(countNum + " patients are allergic to " + SubstanceName);
				}
				else
				{
					System.out.println(countNum + " patients are not allergic to any substance");
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	private static void processAdminOp2(Connection con) {

		try {
			Statement st = null;
			ResultSet rs = null;
			String patientId;
			String GivenName;
			String FamilyName;

			String query =
					"SELECT DISTINCT P.patientId, P.GivenName, P.FamilyName " +
					"FROM Patient P, Allergic_To A " +
					"WHERE P.patientId = A.patientId " +
					"GROUP BY P.patientId " +
					"HAVING COUNT(*)>1";

			st = con.createStatement();
			rs = st.executeQuery(query);

			int count = 0;
			while(rs.next()) {
				++count;
				patientId = rs.getString("patientId");
				GivenName = rs.getString("GivenName");
				FamilyName = rs.getString("FamilyName");

				System.out.println(GivenName + " " + FamilyName + " has more than one allergy");	
			}

			if (count == 0) {
				System.out.println("No patients have more than one allergy");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	private static void processAdminOp3(Connection con) {

		System.out.println("Enter plan name to search for");
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String planName = "";
		try {
			planName = br.readLine();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		String pn = planName;
		planName = "'"+planName.replace("'","\\'")+"'";
		String PlanDate;
		String GivenName;
		String FamilyName;
		String recievedData = new String();

		Date today = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(today);
		int todayYear = cal.get(Calendar.YEAR);
		int todayMonth = cal.get(Calendar.MONDAY) + 1;
		int todayDay = cal.get(Calendar.DAY_OF_MONTH);
		int planYear;
		int planMonth;
		int planDay;
		String[] data;
		String newDay;
		String newMonth;
		String newYear;

		try {
			Statement st = null;
			ResultSet rs = null;

			String query =
					"SELECT DISTINCT A.DateScheduled, P.GivenName, P.FamilyName " +
					"FROM Patient P, Plan_Scheduled_For A " +
					"WHERE P.patientId = A.patientId AND A.PlanName=" + planName;
			
			st = con.createStatement();
			rs = st.executeQuery(query);

			int count = 0;
			while(rs.next()) {
				GivenName = rs.getString("GivenName");
				FamilyName = rs.getString("FamilyName");
				PlanDate = rs.getString("DateScheduled");

				recievedData = PlanDate;
				data = recievedData.split("/", 3);
				newMonth = data[0];
				newDay = data[1];
				data = data[2].split(" ", 2);
				newYear = data[0];

				planYear = Integer.parseInt(newYear);
				planMonth = Integer.parseInt(newMonth);
				planDay = Integer.parseInt(newDay);

				if (PlanDate == null) {
					continue;
				}

				if (todayYear==planYear && todayMonth==planMonth && todayDay==planDay) {
					++count;
					System.out.println(GivenName + " " + FamilyName + " is planned for " + pn + " today");
				}
			}

			if (count == 0) {
				System.out.println("There are no patients who are scheduled for " + pn + " today");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	private static void processAdminOp4(Connection con) {

		try {
			Statement st = null;
			ResultSet rs = null;
			String AuthorTitle;
			String AuthorFirstName;
			String AuthorLastName;

			String query =
					"SELECT DISTINCT A.AuthorTitle, A.AuthorFirstName, A.AuthorLastName " +
					"FROM Assigned_To T, Author A " +
					"WHERE T.AuthorId = A.AuthorId " +
					"GROUP BY T.AuthorId " +
					"HAVING COUNT(*)>1";

			st = con.createStatement();
			rs = st.executeQuery(query);

			int count = 0;
			while(rs.next()) {
				++count;
				AuthorTitle = rs.getString("AuthorTitle");
				AuthorFirstName = rs.getString("AuthorFirstName");
				AuthorLastName = rs.getString("AuthorLastName");

				System.out.println(AuthorFirstName + " " + AuthorLastName + " has more than one patient");	
			}

			if (count == 0) {
				System.out.println("No authors have more than one patient");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}
	
	private static boolean checkIsValidPatientId(String id, Connection con) {
		
		try {
			Statement st = null;
			ResultSet rs = null;

			String query =
					"SELECT * " +
					"FROM Patient " +
					"WHERE PatientId = " + id;

			st = con.createStatement();
			rs = st.executeQuery(query);

			return rs.next();

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
		
	}
	
	private static void processPatientOp1(Connection con, String patientID) {

		try {
			Statement st = null;
			ResultSet rs = null;
			
			// patient table
			
			String query =
					"SELECT * " +
					"FROM Patient " +
					"WHERE PatientId = " + patientID;

			st = con.createStatement();
			rs = st.executeQuery(query);
			rs.next();

			String PatientId = patientID;
			String FamilyName = rs.getString("FamilyName");
			String GivenName = rs.getString("GivenName");
			String Suffix = rs.getString("Suffix");
			String BirthTime = rs.getString("BirthTime");
			String Gender = rs.getString("Gender");
			String ProviderId = rs.getString("ProviderId");
			String xmlHealthCreationDateTime = rs.getString("xmlHealthCreationDateTime");
			String GuardianNo = rs.getString("GuardianNo");
			String PayerId = rs.getString("PayerId");
			String PatientRole = rs.getString("PatientRole");
			String PolicyType = rs.getString("PolicyType");
			String PolicyHolder = rs.getString("PolicyHolder");
			String Purpose = rs.getString("Purpose");
			
			Patient patient = new Patient(PatientId, FamilyName, GivenName, null, BirthTime, null, ProviderId, xmlHealthCreationDateTime, GuardianNo, PayerId, PatientRole, PolicyType, PolicyHolder, Purpose);
			
			System.out.println("~~~~~~~~~~~~~~~~~~~~~Patient Data~~~~~~~~~~~~~~~~~~~~~");
			String colPrint = "";
			String valPrint = "";
			if (patient.PatientId != null) {
				colPrint = "PatientId";
				valPrint = PatientId;
				System.out.println(colPrint + ": " + valPrint);
			}
			if (patient.GivenName != null) {
				colPrint = "GivenName";
				valPrint = GivenName;
				System.out.println(colPrint + ": " + valPrint);
			}
			if (patient.FamilyName != null) {
				colPrint = "FamilyName";
				valPrint = FamilyName;
				System.out.println(colPrint + ": " + valPrint);
			}
			if (patient.Suffix != null) {
				colPrint = "Suffix";
				valPrint = Suffix;
				System.out.println(colPrint + ": " + valPrint);
			}
			if (patient.BirthTime != null) {
				colPrint = "BirthTime";
				valPrint = BirthTime;
				System.out.println(colPrint + ": " + valPrint);
			}
			if (patient.Gender != null) {
				colPrint = "Gender";
				valPrint = Gender;
				System.out.println(colPrint + ": " + valPrint);
			}
			if (patient.ProviderId != null) {
				colPrint = "ProviderId";
				valPrint = ProviderId;
				System.out.println(colPrint + ": " + valPrint);
			}
			if (patient.xmlHealthCreationDateTime != null) {
				colPrint = "xmlHealthCreationDateTime";
				valPrint = xmlHealthCreationDateTime;
				System.out.println(colPrint + ": " + valPrint);
			}
			if (patient.GuardianNo != null) {
				colPrint = "GuardianNo";
				valPrint = GuardianNo;
				System.out.println(colPrint + ": " + valPrint);
			}
			if (patient.PayerId != null) {
				colPrint = "PayerId";
				valPrint = PayerId;
				System.out.println(colPrint + ": " + valPrint);
			}
			if (patient.PatientRole != null) {
				colPrint = "PatientRole";
				valPrint = PatientRole;
				System.out.println(colPrint + ": " + valPrint);
			}
			if (patient.PolicyType != null) {
				colPrint = "PolicyType";
				valPrint = PolicyType;
				System.out.println(colPrint + ": " + valPrint);
			}
			if (patient.PolicyHolder != null) {
				colPrint = "PolicyHolder";
				valPrint = PolicyHolder;
				System.out.println(colPrint + ": " + valPrint);
			}
			if (patient.Purpose != null) {
				colPrint = "Purpose";
				valPrint = Purpose;
				System.out.println(colPrint + ": " + valPrint);
			}
			
			// guardian table
			
			query =
				"SELECT * " +
				"FROM Guardian G, Patient P " +
				"WHERE G.GuardianNo = " + GuardianNo;

			st = con.createStatement();
			rs = st.executeQuery(query);
			rs.next();

			String Phone = rs.getString("Phone");
			String Address = rs.getString("Address");
			String State = rs.getString("State");
			String GGivenName = rs.getString("GivenName");
			String GFamilyName = rs.getString("FamilyName");
			String City = rs.getString("City");
			String Zip = rs.getString("Zip");
			
			Guardian guardian = new Guardian(GuardianNo, Phone, Address, State, GGivenName, GFamilyName, City, Zip);
			
			System.out.println("~~~~~~~~~~~~~~~~~~~~~Guardian Data~~~~~~~~~~~~~~~~~~~~~");
			colPrint = "";
			valPrint = "";
			if (guardian.GuardianNo != null) {
				colPrint = "GuardianNo";
				valPrint = GuardianNo;
				System.out.println(colPrint + ": " + valPrint);
			}
			if (guardian.Phone != null) {
				colPrint = "Phone";
				valPrint = Phone;
				System.out.println(colPrint + ": " + valPrint);
			}
			if (guardian.Address != null) {
				colPrint = "Address";
				valPrint = Address;
				System.out.println(colPrint + ": " + valPrint);
			}
			if (guardian.State != null) {
				colPrint = "State";
				valPrint = State;
				System.out.println(colPrint + ": " + valPrint);
			}
			if (guardian.GivenName != null) {
				colPrint = "GivenName";
				valPrint = GGivenName;
				System.out.println(colPrint + ": " + valPrint);
			}
			if (guardian.FamilyName != null) {
				colPrint = "FamilyName";
				valPrint = GFamilyName;
				System.out.println(colPrint + ": " + valPrint);
			}
			if (guardian.City != null) {
				colPrint = "City";
				valPrint = City;
				System.out.println(colPrint + ": " + valPrint);
			}
			if (guardian.Zip != null) {
				colPrint = "Zip";
				valPrint = Zip;
				System.out.println(colPrint + ": " + valPrint);
			}
			
			// insurance table
			
			query =
				"SELECT * " +
				"FROM Insurance I, Patient P " +
				"WHERE I.PayerId = " + PayerId;

			st = con.createStatement();
			rs = st.executeQuery(query);
			rs.next();

			String Name = rs.getString("Name");
			Insurance insurance = new Insurance(PayerId, Name);
			
			System.out.println("~~~~~~~~~~~~~~~~~~~~~Insurance Data~~~~~~~~~~~~~~~~~~~~~");
			if (insurance.PayerId != null) {
				colPrint = "PayerId";
				valPrint = PayerId;
				System.out.println(colPrint + ": " + valPrint);
			}
			if (insurance.Name != null) {
				colPrint = "Name";
				valPrint = Name;
				System.out.println(colPrint + ": " + valPrint);
			}
			
			// lab test table
			
			query =
				"SELECT * " +
				"FROM Lab_Test_Report_Of L, Patient P " +
				"WHERE L.PatientId = " + PatientId;

			st = con.createStatement();
			rs = st.executeQuery(query);
			
			String LabTestResultId = "";
			String LabTestType = "";
			String ReferenceRangeHigh = "";
			String PatientVisitId = "";
			String LabTestPerformedDate = "";
			String TestResultValue = "";
			String ReferenceRangeLow = "";
			
			System.out.println("~~~~~~~~~~~~~~~~~~~~~Lab Test Data~~~~~~~~~~~~~~~~~~~~~");
			int count = 1;
			LabTestReport labTestReport;
			while(rs.next()) {
				System.out.println("~~~~~~~~~Test " + Integer.valueOf(count).toString() + "~~~~~~~~~");
				LabTestResultId = rs.getString("LabTestResultId");
				LabTestType = rs.getString("LabTestType");
				ReferenceRangeHigh = rs.getString("ReferenceRangeHigh");
				PatientVisitId = rs.getString("PatientVisitId");
				LabTestPerformedDate = rs.getString("LabTestPerformedDate");
				TestResultValue = rs.getString("TestResultValue");
				ReferenceRangeLow = rs.getString("ReferenceRangeLow");
				
				labTestReport = new LabTestReport(LabTestResultId, LabTestType, ReferenceRangeHigh, PatientVisitId, LabTestPerformedDate, TestResultValue, ReferenceRangeLow, PatientId);
				
				if (labTestReport.LabTestResultId != null) {
					colPrint = "LabTestResultId";
					valPrint = LabTestResultId;
					System.out.println(colPrint + ": " + valPrint);
				}
				if (labTestReport.LabTestType != null) {
					colPrint = "LabTestType";
					valPrint = LabTestType;
					System.out.println(colPrint + ": " + valPrint);
				}
				if (labTestReport.ReferenceRangeHigh != null) {
					colPrint = "ReferenceRangeHigh";
					valPrint = ReferenceRangeHigh;
					System.out.println(colPrint + ": " + valPrint);
				}
				if (labTestReport.PatientVisitId != null) {
					colPrint = "PatientVisitId";
					valPrint = PatientVisitId;
					System.out.println(colPrint + ": " + valPrint);
				}
				if (labTestReport.LabTestPerformedDate != null) {
					colPrint = "LabTestPerformedDate";
					valPrint = LabTestPerformedDate;
					System.out.println(colPrint + ": " + valPrint);
				}
				if (labTestReport.TestResultValue != null) {
					colPrint = "TestResultValue";
					valPrint = TestResultValue;
					System.out.println(colPrint + ": " + valPrint);
				}
				if (labTestReport.ReferenceRangeLow != null) {
					colPrint = "ReferenceRangeLow";
					valPrint = ReferenceRangeLow;
					System.out.println(colPrint + ": " + valPrint);
				}
				if (labTestReport.PatientId != null) {
					colPrint = "PatientId";
					valPrint = PatientId;
					System.out.println(colPrint + ": " + valPrint);
				}
				++count;
			}
			
			if (count == 1) {
				System.out.println("No lab test data");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}
	
	private static void processPatientOp2(Connection con) {

		try {
			Statement st = null;
			ResultSet rs = null;
			String patientId;
			String GivenName;
			String FamilyName;

			String query =
					"SELECT DISTINCT P.patientId, P.GivenName, P.FamilyName " +
					"FROM Patient P, Allergic_To A " +
					"WHERE P.patientId = A.patientId " +
					"GROUP BY P.patientId " +
					"HAVING COUNT(*)>1";

			st = con.createStatement();
			rs = st.executeQuery(query);

			int count = 0;
			while(rs.next()) {
				++count;
				patientId = rs.getString("patientId");
				GivenName = rs.getString("GivenName");
				FamilyName = rs.getString("FamilyName");

				System.out.println(GivenName + " " + FamilyName + " has more than one allergy");	
			}

			if (count == 0) {
				System.out.println("No patients have more than one allergy");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

}
