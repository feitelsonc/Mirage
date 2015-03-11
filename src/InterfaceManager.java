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
//		System.out.println("Entering patient mode.");
		System.out.println("Enter your patient ID.");
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String in = "";
		String patientId = "";
		boolean isValidId = false;
		while (!isValidId) {
			try {
				in = br.readLine();
				patientId = in;
				
				if (in.toLowerCase().equals("exit")) {
					return;
				}
				
				isValidId = checkIsValidPatientId(patientId, con);
				
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			if (!isValidId) {
				System.out.println(patientId + " is not a valid patient ID. Try again or enter \"exit\" to quit.");	
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
					processPatientOp2(con, patientId);
				}
				else if (in.equals("3") || in.toLowerCase().equals("logout")) {
					System.out.println("Logged out");
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
		System.out.println("Enter a patient ID.");
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
				System.out.println(patientId + " is not a valid patient ID. Try again or enter \"exit\" to quit.");	
			}
		}
		
		boolean quit = false;
		try {
			while (!quit) {
				System.out.println("Enter (1) to view all of patient's data");
				System.out.println("Enter (2) to edit patient's data");
				System.out.println("Enter (3) to logout");

				in = br.readLine();

				if (in.equals("1")) {
					quit = false;
					processPatientOp1(con, patientId);
				}
				else if (in.equals("2")) {
					quit = false;
					processDoctorOp2(con, patientId);
				}
				else if (in.equals("3") || in.toLowerCase().equals("logout")) {
					System.out.println("Logged out");
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

	private static void administratorInterface(Connection con) {
//		System.out.println("Entering administrator mode.");
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
					System.out.println("Logged out");
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
					if (Integer.valueOf(countNum).intValue() == 1) {
						System.out.println(countNum + " patient is allergic to " + SubstanceName);
					}
					else {
						System.out.println(countNum + " patients are allergic to " + SubstanceName);
					}
				}
				else {
					if (Integer.valueOf(countNum).intValue() == 1) {
						System.out.println(countNum + " patient is not allergic to any substance");
					}
					else {
						System.out.println(countNum + " patients are not allergic to any substance");
					}
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

				System.out.println(GivenName + " " + FamilyName);	
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
			return false;
		}
		
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
			
			Patient patient = new Patient(PatientId, FamilyName, GivenName, Suffix, BirthTime, Gender, ProviderId, xmlHealthCreationDateTime, GuardianNo, PayerId, PatientRole, PolicyType, PolicyHolder, Purpose);
			
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
				"FROM Lab_Test_Report_Of " +
				"WHERE PatientId = " + PatientId;

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
				++count;
			}
			
			if (count == 1) {
				System.out.println("No lab test data");
			}
			
			// family member table
			
			query =
				"SELECT * " +
				"FROM Family_Member_Of_Patient F " +
				"WHERE F.PatientId = " + PatientId;

			st = con.createStatement();
			rs = st.executeQuery(query);
			
			String Id = "";
			String Age = "";
			String Relationship = "";
			String Diagnosis = "";
			
			System.out.println("~~~~~~~~~~~~~~~~~~~~~Family History Data~~~~~~~~~~~~~~~~~~~~~");
			count = 1;
			FamilyMember familyMember;
			while(rs.next()) {
				System.out.println("~~~~~~~~~Family Member " + Integer.valueOf(count).toString() + "~~~~~~~~~");
				Id = rs.getString("Id");
				Age = rs.getString("Age");
				Relationship = rs.getString("Relationship");
				Diagnosis = rs.getString("Diagnosis");
				
				familyMember = new FamilyMember(Id, Age, Relationship, Diagnosis, PatientId);
				
				if (familyMember.Id != null) {
					colPrint = "Id";
					valPrint = Id;
					System.out.println(colPrint + ": " + valPrint);
				}
				if (familyMember.Age != null) {
					colPrint = "Age";
					valPrint = Age;
					System.out.println(colPrint + ": " + valPrint);
				}
				if (familyMember.Relationship != null) {
					colPrint = "Relationship";
					valPrint = Relationship;
					System.out.println(colPrint + ": " + valPrint);
				}
				if (familyMember.Diagnosis != null) {
					colPrint = "Diagnosis";
					valPrint = Diagnosis;
					System.out.println(colPrint + ": " + valPrint);
				}
				
				++count;
			}
			
			if (count == 1) {
				System.out.println("No family history data");
			}
			
			// author table
			
			query =
				"SELECT * " +
				"FROM Assigned_To AT, Author A " +
				"WHERE AT.PatientId = " + PatientId + " AND AT.AuthorId = A.AuthorId";

				st = con.createStatement();
				rs = st.executeQuery(query);

			String AuthorId = "";
			String AuthorFirstName = "";
			String AuthorTitle = "";
			String AuthorLastName = "";
			String ParticipatingRole = "";
			
			System.out.println("~~~~~~~~~~~~~~~~~~~~~Author Data~~~~~~~~~~~~~~~~~~~~~");
			count = 1;
			Author author;
			while(rs.next()) {
				System.out.println("~~~~~~~~~Author " + Integer.valueOf(count).toString() + "~~~~~~~~~");
				AuthorId = rs.getString("AuthorId");
				AuthorFirstName = rs.getString("AuthorFirstName");
				AuthorTitle = rs.getString("AuthorTitle");
				AuthorLastName = rs.getString("AuthorLastName");
				ParticipatingRole = rs.getString("ParticipatingRole");
				
				author = new Author(AuthorId, AuthorFirstName, AuthorTitle, AuthorLastName);
				
				if (author.AuthorId != null) {
					colPrint = "AuthorId";
					valPrint = AuthorId;
					System.out.println(colPrint + ": " + valPrint);
				}
				if (author.AuthorFirstName != null) {
					colPrint = "AuthorFirstName";
					valPrint = AuthorFirstName;
					System.out.println(colPrint + ": " + valPrint);
				}
				if (author.AuthorTitle != null) {
					colPrint = "AuthorTitle";
					valPrint = AuthorTitle;
					System.out.println(colPrint + ": " + valPrint);
				}
				if (author.AuthorLastName != null) {
					colPrint = "AuthorLastName";
					valPrint = AuthorLastName;
					System.out.println(colPrint + ": " + valPrint);
				}
				if (ParticipatingRole != null) {
					colPrint = "ParticipatingRole";
					valPrint = ParticipatingRole;
					System.out.println(colPrint + ": " + valPrint);
				}
				
				++count;
			}
			
			if (count == 1) {
				System.out.println("No author data");
			}
			
			// allergic to table

			query =
				"SELECT * " +
				"FROM Allergic_To A, Substance S " +
				"WHERE A.PatientId = " + PatientId + " AND S.SubstanceId = A.SubstanceId";

			st = con.createStatement();
			rs = st.executeQuery(query);

			String Reaction = "";
			String Active = "";
			String SubstanceId = "";
			String SubstanceName = "";

			System.out.println("~~~~~~~~~~~~~~~~~~~~~Allergy Data~~~~~~~~~~~~~~~~~~~~~");
			count = 1;
			AllergicTo allergicTo;
			while(rs.next()) {
				System.out.println("~~~~~~~~~Allergy " + Integer.valueOf(count).toString() + "~~~~~~~~~");
				Reaction = rs.getString("Reaction");
				Active = rs.getString("Active");
				SubstanceId = rs.getString("SubstanceId");
				SubstanceName = rs.getString("SubstanceName");

				allergicTo = new AllergicTo(Reaction, Active, SubstanceId, PatientId);

				if (allergicTo.Reaction != null) {
					colPrint = "Reaction";
					valPrint = Reaction;
					System.out.println(colPrint + ": " + valPrint);
				}
				if (allergicTo.Active != null) {
					colPrint = "Active";
					valPrint = Active;
					System.out.println(colPrint + ": " + valPrint);
				}
				if (allergicTo.SubstanceId != null) {
					colPrint = "SubstanceId";
					valPrint = SubstanceId;
					System.out.println(colPrint + ": " + valPrint);
				}
				if (SubstanceName != null) {
					colPrint = "SubstanceName";
					valPrint = SubstanceName;
					System.out.println(colPrint + ": " + valPrint);
				}

				++count;
			}

			if (count == 1) {
				System.out.println("No allergy data");
			}
			
			// plan table

			query =
				"SELECT * " +
				"FROM Plan_Scheduled_For P " +
				"WHERE P.PatientId = " + PatientId;

			st = con.createStatement();
			rs = st.executeQuery(query);

			String PlanId = "";
			String PlanName = "";
			String DateScheduled = "";

			System.out.println("~~~~~~~~~~~~~~~~~~~~~Plan Data~~~~~~~~~~~~~~~~~~~~~");
			count = 1;
			PlanScheduledFor planScheduledFor;
			while(rs.next()) {
				System.out.println("~~~~~~~~~Plan " + Integer.valueOf(count).toString() + "~~~~~~~~~");
				PlanId = rs.getString("PlanId");
				PlanName = rs.getString("PlanName");
				DateScheduled = rs.getString("DateScheduled");

				planScheduledFor = new PlanScheduledFor(PlanId, PlanName, PatientId, DateScheduled);

				if (planScheduledFor.PlanId != null) {
					colPrint = "PlanId";
					valPrint = PlanId;
					System.out.println(colPrint + ": " + valPrint);
				}
				if (planScheduledFor.PlanName != null) {
					colPrint = "PlanName";
					valPrint = PlanName;
					System.out.println(colPrint + ": " + valPrint);
				}
				if (planScheduledFor.DateScheduled != null) {
					colPrint = "DateScheduled";
					valPrint = DateScheduled;
					System.out.println(colPrint + ": " + valPrint);
				}

				++count;
			}

			if (count == 1) {
				System.out.println("No plan data");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}
	
	private static void processPatientOp2(Connection con,  String patientId) {

		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String in;
		boolean quit = false;
		try {
			while (!quit) {
				System.out.println("Enter (1) to edit Patient Data");
				System.out.println("Enter (2) to edit Guardian Data");
				System.out.println("Enter (3) to exit");

				in = br.readLine();

				if (in.equals("1")) {
					quit = false;
					processPatientOp2_1(con, patientId);
				}
				else if (in.equals("2")) {
					quit = false;
					processPatientOp2_2(con, patientId);
				}
				else if (in.equals("3") || in.toLowerCase().equals("exit")) {
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

	private static void processPatientOp2_2(Connection con, String patientId) {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String in = "";
		String updateMessage = "";
		String guardianNo = "";
		String query = "";
		Statement st;
		ResultSet rs;
		
		boolean quit = false;
		try {
			
			query = "SELECT * FROM Patient WHERE PatientId = " + patientId;
			
			try {
				st = con.createStatement();
				rs = st.executeQuery(query);
				
				rs.next();
				
				guardianNo = rs.getString("GuardianNo");
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			
			
			
			while (!quit) {
				System.out.println("Enter (1) to edit Phone");
				System.out.println("Enter (2) to edit Address");
				System.out.println("Enter (3) to edit State");
				System.out.println("Enter (4) to edit Given Name");
				System.out.println("Enter (5) to edit Family Name");
				System.out.println("Enter (6) to edit City");
				System.out.println("Enter (7) to edit Zip");
				System.out.println("Enter (8) to exit");

				in = br.readLine();

				if (in.equals("1")) {
					quit = false;
					System.out.println("Enter new Phone value");
					in = br.readLine();
					if (!isNull(in)) {
						in = "'"+in.replace("'","\\'")+"'";
					}
					else
					{
						in = "NULL";
					}
					updateMessage = "UPDATE Guardian " +
							"SET Phone = " + in + " " +
							"WHERE GuardianNo = " + guardianNo;
					try {
						
						Parser.executeUpdate(con,updateMessage);
					} catch (SQLException e) {
						e.printStackTrace();
					}
					
					
				}
				else if (in.equals("2")) {
					quit = false;
					System.out.println("Enter new Address value");
					in = br.readLine();
					if (!isNull(in)) {
						in = "'"+in.replace("'","\\'")+"'";
					}
					else
					{
						in = "NULL";
					}
					updateMessage = "UPDATE Guardian " +
							"SET Address = " + in + " " +
							"WHERE GuardianNo = " + guardianNo;
					try {
						
						Parser.executeUpdate(con,updateMessage);
					} catch (SQLException e) {
						e.printStackTrace();
					}		
					
				}
				else if (in.equals("3")) {
					quit = false;
					System.out.println("Enter new State value");
					in = br.readLine();
					if (!isNull(in)) {
						in = "'"+in.replace("'","\\'")+"'";
					}
					else
					{
						in = "NULL";
					}
					
					updateMessage = "UPDATE Guardian " +
							"SET State = " + in + " " +
							"WHERE GuardianNo = " + guardianNo;
					try {
						
						Parser.executeUpdate(con,updateMessage);
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
				else if (in.equals("4")) {
					quit = false;
					System.out.println("Enter new Given Name value");
					in = br.readLine();
					if (!isNull(in)) {
						in = "'"+in.replace("'","\\'")+"'";
					}
					else
					{
						in = "NULL";
					}
					updateMessage = "UPDATE Guardian " +
							"SET GivenName = " + in + " " +
							"WHERE GuardianNo = " + guardianNo;
					try {
						
						Parser.executeUpdate(con,updateMessage);
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
				else if (in.equals("5")) {
					quit = false;
					System.out.println("Enter new Family Name value");
					in = br.readLine();
					if (!isNull(in)) {
						in = "'"+in.replace("'","\\'")+"'";
					}
					else
					{
						in = "NULL";
					}
					updateMessage = "UPDATE Guardian " +
							"SET FamilyName = " + in + " " +
							"WHERE GuardianNo = " + guardianNo;
					try {
						
						Parser.executeUpdate(con,updateMessage);
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
				else if (in.equals("6")) {
					quit = false;
					System.out.println("Enter new City value");
					in = br.readLine();
					if (!isNull(in)) {
						in = "'"+in.replace("'","\\'")+"'";
					}
					else {
						
						in = "NULL";
						
					}
					updateMessage = "UPDATE Guardian " +
							"SET City = " + in + " " +
							"WHERE GuardianNo = " + guardianNo;
					try {
						
						Parser.executeUpdate(con,updateMessage);
					} catch (SQLException e) {
						e.printStackTrace();
					}
		
				}
				else if (in.equals("7")) {
					quit = false;
					System.out.println("Enter new Zip value");
					in = br.readLine();
					if (!isNull(in)) {
						in = "'"+in.replace("'","\\'")+"'";
						
					}
					else
					{
						in = "NULL";
					}
					updateMessage = "UPDATE Guardian " +
							"SET Zip = " + in + " " +
							"WHERE GuardianNo = " + guardianNo;
					try {
						
						Parser.executeUpdate(con,updateMessage);
					} catch (SQLException e) {
						e.printStackTrace();
					}
					
				}
				else if (in.equals("8") || in.toLowerCase().equals("exit")) {
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
	

	private static void processPatientOp2_1(Connection con, String patientId) {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String in;
		String updateMessage;
		boolean quit = false;
		try {
			while (!quit) {
				System.out.println("Enter (1) to edit Family Name");
				System.out.println("Enter (2) to edit Given Name");
				System.out.println("Enter (3) to edit Suffix");
				System.out.println("Enter (4) to edit Birth Time");
				System.out.println("Enter (5) to edit Gender");
				System.out.println("Enter (6) to edit Patient Role");
				System.out.println("Enter (7) to edit Purpose");
				System.out.println("Enter (8) to exit");

				in = br.readLine();

				if (in.equals("1")) {
					quit = false;
					System.out.println("Enter new Family Name value");
					in = br.readLine();
					if (!isNull(in)) {
						in = "'"+in.replace("'","\\'")+"'";
					}
					else
					{
						in = "NULL";
					}
					updateMessage = "UPDATE Patient " +
							"SET FamilyName = " + in + " " +
							"WHERE PatientId = " + patientId;
					try {
						
						Parser.executeUpdate(con,updateMessage);
					} catch (SQLException e) {
						e.printStackTrace();
					}
					
					
				}
				else if (in.equals("2")) {
					quit = false;
					System.out.println("Enter new Given Name value");
					in = br.readLine();
					if (!isNull(in)) {
						in = "'"+in.replace("'","\\'")+"'";
					}
					else
					{
						in = "NULL";
					}
					updateMessage = "UPDATE Patient " +
							"SET GivenName = " + in + " " +
							"WHERE PatientId = " + patientId;
					try {
						
						Parser.executeUpdate(con,updateMessage);
					} catch (SQLException e) {
						e.printStackTrace();
					}		
					
				}
				else if (in.equals("3")) {
					quit = false;
					System.out.println("Enter new Suffix value");
					in = br.readLine();
					if (!isNull(in)) {
						in = "'"+in.replace("'","\\'")+"'";
					}
					else
					{
						in = "NULL";
					}
					
					updateMessage = "UPDATE Patient " +
							"SET Suffix = " + in + " " +
							"WHERE PatientId = " + patientId;
					try {
						
						Parser.executeUpdate(con,updateMessage);
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
				else if (in.equals("4")) {
					quit = false;
					System.out.println("Enter new Birth Time value");
					in = br.readLine();
					if (!isNull(in)) {
						in = "'"+in.replace("'","\\'")+"'";
					}
					else
					{
						in = "NULL";
					}
					updateMessage = "UPDATE Patient " +
							"SET BirthTime = " + in + " " +
							"WHERE PatientId = " + patientId;
					try {
						
						Parser.executeUpdate(con,updateMessage);
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
				else if (in.equals("5")) {
					quit = false;
					System.out.println("Enter new Gender value");
					in = br.readLine();
					if (!isNull(in)) {
						in = "'"+in.replace("'","\\'")+"'";
						System.out.println("value not NULLLLLLLLLLL");
					}
					else
					{
						System.out.println("value is NULLLLLLLLLLL");
						in = "NULL";
					}
					updateMessage = "UPDATE Patient " +
							"SET Gender = " + in + " " +
							"WHERE PatientId = " + patientId;
					try {
						
						Parser.executeUpdate(con,updateMessage);
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
				else if (in.equals("6")) {
					quit = false;
					System.out.println("Enter new Patient Role value");
					in = br.readLine();
					if (!isNull(in)) {
						in = "'"+in.replace("'","\\'")+"'";
						updateMessage = "UPDATE Patient " +
								"SET PatientRole = " + in + " " +
								"WHERE PatientId = " + patientId;
						try {
							
							Parser.executeUpdate(con,updateMessage);
						} catch (SQLException e) {
							e.printStackTrace();
						}
					}
					else {
						quit = false;
						System.out.println("PatientRole cannot be NULL or empty");
						
					}
		
				}
				else if (in.equals("7")) {
					quit = false;
					System.out.println("Enter new Purpose value");
					in = br.readLine();
					if (!isNull(in)) {
						in = "'"+in.replace("'","\\'")+"'";
						updateMessage = "UPDATE Patient " +
								"SET Purpose = " + in + " " +
								"WHERE PatientId = " + patientId;
						try {
							
							Parser.executeUpdate(con,updateMessage);
						} catch (SQLException e) {
							e.printStackTrace();
						}
					}
					else
					{
						quit = false;
						System.out.println("Purpose cannot be NULL or empty");
					}
					
				}
				else if (in.equals("8") || in.toLowerCase().equals("exit")) {
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
	
	
	private static void processDoctorOp2(Connection con, String patientId) {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String in = "";
		String query = "";
		Statement st;
		ResultSet rs;
		String planId;
		boolean quit = false;
		try {
			
			while (!quit) {
				System.out.println("Enter (1) to edit Plan data");
				System.out.println("Enter (2) to edit Allergy data");
				System.out.println("Enter (3) to exit");

				in = br.readLine();

				if (in.equals("1")) {
					quit = false;
					System.out.println("Enter plan ID");
					in = br.readLine();
					processDoctorOp2_1(con, patientId, in);
				}
				else if (in.equals("2")) {
					quit = false;
					System.out.println("Enter substance ID");
					in = br.readLine();
					processDoctorOp2_2(con, patientId, in);
				}
				else if (in.equals("3") || in.toLowerCase().equals("exit")) {
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
	
	
	private static void processDoctorOp2_1(Connection con, String patientId, String planId) {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String in = "";
		String updateMessage;
		Statement st;
		ResultSet rs;
		String query;
		boolean quit = false;
		try {
			
			while (!quit) {
				query = "SELECT * FROM Plan_Scheduled_For WHERE PlanId = " + planId + " AND PatientId = " + patientId;
				try {
					st = con.createStatement();
					rs = st.executeQuery(query);

					if (!rs.next()) {
						quit = false;
						System.out.println("Invalid Plan Id for Patient Id. Please Try Again or type \"exit\" to exit");
						in = br.readLine();
						if (in.toLowerCase().equals("exit"))
						{
							return;
						}
						planId = in;
					}
					else {
						quit = true;
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
				
			}
			
			quit = false;
			
			while (!quit) {
				System.out.println("Enter (1) to edit Plan Name");
				System.out.println("Enter (2) to edit Date Scheduled");
				System.out.println("Enter (3) to exit");
				// TODO: Maybe add delete option

				in = br.readLine();

				if (in.equals("1")) {
					quit = false;
					System.out.println("Enter new Plan Name value");
					in = br.readLine();
					if (!isNull(in)) {
						in = "'"+in.replace("'","\\'")+"'";
						updateMessage = "UPDATE Plan_Scheduled_For " +
								"SET PlanName = " + in + " " +
								"WHERE PlanId = " + planId;
						try {
							
							Parser.executeUpdate(con,updateMessage);
						} catch (SQLException e) {
							e.printStackTrace();
						}
					}
					else
					{
						quit = false;
						System.out.println("Plan Name cannot be NULL or empty");
					}
					
					
					
				}
				else if (in.equals("2")) {
					quit = false;
					System.out.println("Enter new Date Scheduled value");
					in = br.readLine();
					if (!isNull(in)) {
						in = "'"+in.replace("'","\\'")+"'";
						updateMessage = "UPDATE Plan_Scheduled_For " +
								"SET DateScheduled = " + in + " " +
								"WHERE PlanId = " + planId;
						try {
							
							Parser.executeUpdate(con,updateMessage);
						} catch (SQLException e) {
							e.printStackTrace();
						}	
					}
					else
					{
						quit = false;
						System.out.println("Date Scheduled cannot be NULL or empty");
					}	
					
				}
				else if (in.equals("3") || in.toLowerCase().equals("exit")) {
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

	private static void processDoctorOp2_2(Connection con, String patientId, String substanceId) {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String in = "";
		String updateMessage;
		Statement st;
		ResultSet rs;
		String query;
		boolean quit = false;
		try {
			while (!quit) {
				query = "SELECT * FROM Allergic_To WHERE SubstanceId = " + substanceId + " AND PatientId = " + patientId;
				try {
					st = con.createStatement();
					rs = st.executeQuery(query);

					if (!rs.next()) {
						quit = false;
						System.out.println("Invalid Substance Id for Patient Id. Please Try Again or type \"exit\" to exit");
						in = br.readLine();
						if (in.toLowerCase().equals("exit"))
						{
							return;
						}
						substanceId = in;
					}
					else {
						quit = true;
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
				
			}
			
			quit = false;
			
			
			while (!quit) {
				System.out.println("Enter (1) to edit Reaction");
				System.out.println("Enter (2) to edit Active");
				System.out.println("Enter (3) to edit SubstanceName");
				System.out.println("Enter (4) to exit");
				// TODO: Maybe add delete option
				
				in = br.readLine();

				if (in.equals("1")) {
					quit = false;
					System.out.println("Enter new Reaction value");
					in = br.readLine();
					if (!isNull(in)) {
						in = "'"+in.replace("'","\\'")+"'";
					}
					else
					{
						in = "NULL";
					}
					updateMessage = "UPDATE Allergic_To " +
							"SET Reaction = " + in + " " +
							"WHERE PatientId = " + patientId + " AND SubstanceId = " + substanceId;
					try {
						
						Parser.executeUpdate(con,updateMessage);
					} catch (SQLException e) {
						e.printStackTrace();
					}
					
					
				}
				else if (in.equals("2")) {
					quit = false;
					System.out.println("Enter new Active value");
					in = br.readLine();
					if (!isNull(in)) {
						in = "'"+in.replace("'","\\'")+"'";
					}
					else
					{
						in = "NULL";
					}
					updateMessage = "UPDATE Allergic_To " +
							"SET Active = " + in + " " +
							"WHERE PatientId = " + patientId + " AND SubstanceId = " + substanceId;
					try {
						
						Parser.executeUpdate(con,updateMessage);
					} catch (SQLException e) {
						e.printStackTrace();
					}		
					
				}
				else if (in.equals("3")) {
					quit = false;
					System.out.println("Enter new Substance name value");
					in = br.readLine();
					if (!isNull(in)) {
						in = "'"+in.replace("'","\\'")+"'";
					}
					else
					{
						in = "NULL";
					}
					
					updateMessage = "UPDATE Substance " +
							"SET SubstanceName = " + in + " " +
							"WHERE SubstanceId = " + substanceId;
					try {
						
						Parser.executeUpdate(con,updateMessage);
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
				else if (in.equals("4") || in.toLowerCase().equals("exit")) {
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
	
	private static boolean isNull(String val) {
		return val.toLowerCase().equals("null") || val.toLowerCase().equals("");
	}


}
