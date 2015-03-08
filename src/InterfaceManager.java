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


}
