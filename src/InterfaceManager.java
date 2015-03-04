import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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

		
		boolean invalidInput = true;
		try {
			while (invalidInput) {
				System.out.println("Login as Patient, Doctor, or Adminstrator?");
				in = br.readLine();
				
				if (in.toLowerCase().equals("patient")) {
					invalidInput = false;
					patientInterface(con);
				}
				else if (in.toLowerCase().equals("doctor")) {
					invalidInput = false;
					doctorInterface(con);
				}
				else if (in.toLowerCase().equals("adminstrator")) {
					invalidInput = false;
					adminstratorInterface(con);
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
	
	private static void adminstratorInterface(Connection con) {
		System.out.println("Entering adminstrator mode.");
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String in;
		boolean invalidInput = true;
		try {
			while (invalidInput) {
				System.out.println("Enter (1) to view number of patients for each type of allergy");
				System.out.println("Enter (2) to view the patients who have more than one allergy");
				System.out.println("Enter (3) to view the patients who have a plan for today");
				System.out.println("Enter (4) to view authors who have more than one patient");
				
				in = br.readLine();
				
				if (in.equals("1") || in.equals("(1)")) {
					invalidInput = false;
					processAdminOp1(con);
				}
				else if (in.equals("2") || in.equals("(2)")) {
					invalidInput = false;
				}
				else if (in.equals("3") || in.equals("(3)")) {
					invalidInput = false;
				}
				else if (in.equals("4") || in.equals("(4)")) {
					invalidInput = false;
				}
				else {
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
			
			String query = "SELECT COUNT(*),S.SubstanceName "+ 
					"FROM "+HealthInformationSystem.TableNameAllergicTo + " A, "+ HealthInformationSystem.TableNameSubstance + " S"+
					" WHERE A.SubstanceId = S.SubstanceId" +
					" GROUP BY A.SubstanceName ";
			
			st = con.createStatement();
			rs = st.executeQuery(query);
			
			while(rs.next()) {
				countNum = rs.getString("COUNT(*)");
				SubstanceName = rs.getString("SubstanceName");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		
	
		
	}

}
