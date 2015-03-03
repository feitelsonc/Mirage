import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class Parser {

	public static void execute() {
		try {
			Connection connMessage = null;
			Connection connHealth = null;
			Statement st = null;
			ResultSet rs = null;
			String id;

			Properties connectionProps = new Properties();
			connectionProps.put("user", HealthInformationSystem.userName);
			connectionProps.put("password", HealthInformationSystem.password);

			connHealth = DriverManager.getConnection("jdbc:mysql://"
					+ HealthInformationSystem.serverName + ":" + HealthInformationSystem.portNumber + "/" + HealthInformationSystem.dbName,
					connectionProps);

			connMessage = DriverManager.getConnection("jdbc:mysql://"
					+ HealthInformationSystem.serverName + ":" + HealthInformationSystem.portNumber + "/" + "healthmessagesexchange",
					connectionProps);


			/*******************************************************************/

			AllergicTo allergicTo;
			AssignedTo assignedTo;
			Author author;
			FamilyMember familyMember;
			Guardian guardian;
			Insurance insurance;
			LabTestReport labTestReport;
			Patient patient;
			PlanScheduledFor planScheduledFor;
			Substance substance;

			String MsgId;
			String Last_Accessed;
			String patientId;
			String GivenName;
			String FamilyName;
			String BirthTime;
			String providerId;
			String GuardianNo;
			String Relationship;
			String FirstName;
			String LastName;
			String phone;
			String address;
			String city;
			String state;
			String zip;
			String AuthorId;
			String AuthorTitle;
			String AuthorFirstName;
			String AuthorLastName;
			String ParticipatingRole;
			String PayerId;
			String Name;
			String PolicyHolder;
			String PolicyType;
			String Purpose;
			String RelativeId;
			String Relation;
			String age;
			String Diagnosis;
			String Id;
			String Substance;
			String Reaction;
			String Status;
			String LabTestResultId;
			String PatientVisitId;
			String LabTestPerformedDate;
			String LabTestType;
			String TestResultValue;
			String ReferenceRangeHigh;
			String ReferenceRangeLow;
			String PlanId;
			String Activity;
			String ScheduledDate;
			String suffix;
			String gender;

			st = connMessage.createStatement();

			rs = st.executeQuery("SELECT * FROM messages;");

			//TODO: update Last_Accessed in healthmessagesexchange to current time

			while(rs.next()) {

				MsgId = rs.getString("MsgId");
				Last_Accessed = rs.getString("Last_Accessed");
				patientId = rs.getString("patientId");
				GivenName = rs.getString("GivenName");
				FamilyName = rs.getString("FamilyName");
				BirthTime = rs.getString("BirthTime");
				providerId = rs.getString("providerId");
				GuardianNo = rs.getString("GuardianNo");
				Relationship = rs.getString("Relationship");
				FirstName = rs.getString("FirstName");
				LastName = rs.getString("LastName");
				phone = rs.getString("phone");
				address = rs.getString("address");
				city = rs.getString("city");
				state = rs.getString("state");
				zip = rs.getString("zip");
				AuthorId = rs.getString("AuthorId");
				AuthorTitle = rs.getString("AuthorTitle");
				AuthorFirstName = rs.getString("AuthorFirstName");
				AuthorLastName = rs.getString("AuthorLastName");
				ParticipatingRole = rs.getString("ParticipatingRole");
				PayerId = rs.getString("PayerId");
				Name = rs.getString("Name");
				PolicyHolder = rs.getString("PolicyHolder");
				PolicyType = rs.getString("PolicyType");
				Purpose = rs.getString("Purpose");
				RelativeId = rs.getString("RelativeId");
				Relation = rs.getString("Relation");
				age = rs.getString("age");
				Diagnosis = rs.getString("Diagnosis");
				Id = rs.getString("Id");
				Substance = rs.getString("Substance");
				Reaction = rs.getString("Reaction");
				Status = rs.getString("Status");
				LabTestResultId = rs.getString("LabTestResultId");
				PatientVisitId = rs.getString("PatientVisitId");
				LabTestPerformedDate = rs.getString("LabTestPerformedDate");
				LabTestType = rs.getString("LabTestType");
				TestResultValue = rs.getString("TestResultValue");
				ReferenceRangeHigh = rs.getString("ReferenceRangeHigh");
				ReferenceRangeLow = rs.getString("ReferenceRangeLow");
				PlanId = rs.getString("PlanId");
				Activity = rs.getString("Activity");
				ScheduledDate = rs.getString("ScheduledDate");
//				suffix = rs.getString("suffix");
//				gender = rs.getString("gender");

				if(PayerId!=null) {
					insurance = new Insurance(PayerId, Name);
					st = connHealth.createStatement();
					insertInsurance(insurance, connHealth);
				}

				if(GuardianNo!=null) {
					guardian = new Guardian(GuardianNo, phone, address, state, FirstName, LastName, city, zip);	
					insertGuardian(guardian, connHealth);
				}

				if(patientId!=null) {
					patient = new Patient(patientId, FamilyName, GivenName, null, BirthTime, null, providerId, Last_Accessed, GuardianNo, PayerId, Relationship, PolicyType, PolicyHolder, Purpose);
					insertPatient(patient, connHealth);
				}

				if(LabTestResultId!=null)
				{
					labTestReport = new LabTestReport(LabTestResultId, LabTestType, ReferenceRangeHigh, PatientVisitId, LabTestPerformedDate, TestResultValue, ReferenceRangeLow, patientId);
					insertLabTestReport(labTestReport, connHealth);
				}

				if(RelativeId!=null && patientId!=null) {
					familyMember = new FamilyMember(RelativeId, age, Relation, Diagnosis, patientId);
					insertFamilyMember(familyMember, connHealth);
				}

				if(AuthorId!=null) {
					author = new Author(AuthorId, AuthorFirstName, AuthorTitle, AuthorLastName);
					insertAuthor(author, connHealth);
				}

				if(AuthorId!=null && patientId!=null)
				{
					assignedTo = new AssignedTo(AuthorId, patientId, ParticipatingRole);
					insertAssignedTo(assignedTo, connHealth);
				}

				if(Id!=null)
				{
					substance = new Substance(Id, Substance);
					insertSubstance(substance, connHealth);
				}

				if(Id!=null && patientId!=null) {
					allergicTo = new AllergicTo(Reaction, Status, Id, patientId);
					insertAllergicTo(allergicTo, connHealth);
				}

				if(PlanId!=null) {
					planScheduledFor = new PlanScheduledFor(PlanId, Activity, patientId, ScheduledDate);
					insertPlanScheduledFor(planScheduledFor, connHealth);
				}
				break;
			}

		}catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	// Method to execute a SQL statement
	private static boolean executeUpdate(Connection conn, String command) throws SQLException {
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
	
	
	private static void insertAllergicTo(AllergicTo allergicTo, Connection conn) {
		String Reaction = "NULL";
		String Active = "NULL";
		String SubstanceId = "NULL";
		String PatientId = "NULL";
		
		if (allergicTo.Reaction != null) {
			Reaction = allergicTo.Reaction;
			Reaction = "'"+Reaction.replace("'","\\'")+"'";
		}
		if (allergicTo.Active != null) {
			Active = allergicTo.Active;
			Active = "'"+Active.replace("'","\\'")+"'";
		}
		if (allergicTo.SubstanceId != null) {
			SubstanceId = allergicTo.SubstanceId;
			SubstanceId = "'"+SubstanceId.replace("'","\\'")+"'";
		}
		if (allergicTo.PatientId != null) {
			PatientId = allergicTo.PatientId;
			PatientId = "'"+PatientId.replace("'","\\'")+"'";
		}
		
		String insertMessage = "INSERT INTO " + HealthInformationSystem.TableNameAllergicTo + " VALUES ('" +Reaction+ "','" +Active+ "','" +SubstanceId+ "','" +PatientId+ "')";
		System.out.println(insertMessage);
		try {
			executeUpdate(conn, insertMessage);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	private static void insertAssignedTo(AssignedTo assignedTo, Connection conn) {
		String AuthorId = "NULL";
		String PatientId = "NULL";
		String ParticipatingRole = "NULL";
		
		if (assignedTo.AuthorId != null) {
			AuthorId = assignedTo.AuthorId;
			AuthorId = "'"+AuthorId.replace("'","\\'")+"'";
		}
		if (assignedTo.PatientId != null) {
			PatientId = assignedTo.PatientId;
			PatientId = "'"+PatientId.replace("'","\\'")+"'";
		}
		if (assignedTo.ParticipatingRole != null) {
			ParticipatingRole = assignedTo.ParticipatingRole;
			ParticipatingRole = "'"+ParticipatingRole.replace("'","\\'")+"'";
		}
		
		String insertMessage = "INSERT INTO " + HealthInformationSystem.TableNameAssignedTo + " VALUES ('" +AuthorId+ "','" +PatientId+ "','" +ParticipatingRole+ "')";
		System.out.println(insertMessage);
		try {
			executeUpdate(conn, insertMessage);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private static void insertAuthor(Author author, Connection conn) {
		String AuthorId = "NULL";
		String AuthorFirstName = "NULL";
		String AuthorTitle = "NULL";
		String AuthorLastName = "NULL";
		
		if (author.AuthorId != null) {
			AuthorId = author.AuthorId;
			AuthorId = "'"+AuthorId.replace("'","\\'")+"'";
		}
		if (author.AuthorFirstName != null) {
			AuthorFirstName = author.AuthorFirstName;
			AuthorFirstName = "'"+AuthorFirstName.replace("'","\\'")+"'";
		}
		if (author.AuthorTitle != null) {
			AuthorTitle = author.AuthorTitle;
			AuthorTitle = "'"+AuthorTitle.replace("'","\\'")+"'";
		}
		if (author.AuthorLastName != null) {
			AuthorLastName = author.AuthorLastName;
			AuthorLastName = "'"+AuthorLastName.replace("'","\\'")+"'";
		}
		
		String insertMessage = "INSERT INTO " + HealthInformationSystem.TableNameAuthor + " VALUES ('" +AuthorId+ "','" +AuthorFirstName+ "','" +AuthorTitle+ "','" +AuthorLastName+ "')";
		System.out.println(insertMessage);
		try {
			executeUpdate(conn, insertMessage);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private static void insertFamilyMember(FamilyMember familyMember, Connection conn) {
		String Id = "NULL";
		String Age = "NULL";
		String Relationship = "NULL";
		String Diagnosis = "NULL";
		String PatientId = "NULL";
		
		if (familyMember.Id != null) {
			Id = familyMember.Id;
			Id = "'"+Id.replace("'","\\'")+"'";
		}
		if (familyMember.Age != null) {
			Age = familyMember.Age;
			Age = "'"+Age.replace("'","\\'")+"'";
		}
		if (familyMember.Relationship != null) {
			Relationship = familyMember.Relationship;
			Relationship = "'"+Relationship.replace("'","\\'")+"'";
		}
		if (familyMember.Diagnosis != null) {
			Diagnosis = familyMember.Diagnosis;
			Diagnosis = "'"+Diagnosis.replace("'","\\'")+"'";
		}
		if (familyMember.PatientId != null) {
			PatientId = familyMember.PatientId;
			PatientId = "'"+PatientId.replace("'","\\'")+"'";
		}
		
		String insertMessage = "INSERT INTO " + HealthInformationSystem.TableNameFamilyMemberOfPatient + " VALUES ('" +Id+ "','" +Age+ "','" +Relationship+ "','" +Diagnosis+ "','" +PatientId+ "')";
		System.out.println(insertMessage);
		try {
			executeUpdate(conn, insertMessage);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private static void insertGuardian(Guardian guardian, Connection conn) {
		String GuardianNo = "NULL";
		String Phone = "NULL";
		String Address = "NULL";
		String State = "NULL";
		String GivenName = "NULL";
		String FamilyName = "NULL";
		String City = "NULL";
		String Zip = "NULL";
		
		if (guardian.GuardianNo != null) {
			GuardianNo = guardian.GuardianNo;
			GuardianNo = "'"+GuardianNo.replace("'","\\'")+"'";
		}
		if (guardian.Phone != null) {
			Phone = guardian.Phone;
			Phone = "'"+Phone.replace("'","\\'")+"'";
		}
		if (guardian.Address != null) {
			Address = guardian.Address;
			Address = "'"+Address.replace("'","\\'")+"'";
		}
		if (guardian.State != null) {
			State = guardian.State;
			State = "'"+State.replace("'","\\'")+"'";
		}
		if (guardian.GivenName != null) {
			GivenName = guardian.GivenName;
			GivenName = "'"+GivenName.replace("'","\\'")+"'";
		}
		if (guardian.FamilyName != null) {
			FamilyName = guardian.FamilyName;
			FamilyName = "'"+FamilyName.replace("'","\\'")+"'";
		}
		if (guardian.City != null) {
			City = guardian.City;
			City = "'"+City.replace("'","\\'")+"'";
		}
		if (guardian.Zip != null) {
			Zip = guardian.Zip;
			Zip = "'"+Zip.replace("'","\\'")+"'";
		}
		
		String insertMessage = "INSERT INTO " + HealthInformationSystem.TableNameGuardian + " VALUES ('" +GuardianNo+ "','" +Phone+ "','" +Address+ "','" +State+ "','" +GivenName+ "','" +FamilyName+ "','" +City+ "','" +Zip+ "')";
		System.out.println(insertMessage);
		try {
			executeUpdate(conn, insertMessage);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private static void insertInsurance(Insurance insurance, Connection conn) {
		String PayerId = "NULL";
		String Name = "NULL";
		
		if (insurance.PayerId != null) {
			PayerId = insurance.PayerId;
			PayerId = "'"+PayerId.replace("'","\\'")+"'";
		}
		if (insurance.Name != null) {
			Name = insurance.Name;
			Name = "'"+Name.replace("'","\\'")+"'";
		}
		
		String insertMessage = "INSERT INTO " + HealthInformationSystem.TableNameInsurance + " VALUES ('" +PayerId+ "','" +Name+ "')";
		System.out.println(insertMessage);
		try {
			executeUpdate(conn, insertMessage);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private static void insertLabTestReport(LabTestReport labTestReport, Connection conn) {
		String LabTestResultId = "NULL";
		String LabTestType = "NULL";
		String ReferenceRangeHigh = "NULL";
		String PatientVisitId = "NULL";
		String LabTestPerformedDate = "NULL";
		String TestResultValue = "NULL";
		String ReferenceRangeLow = "NULL";
		String PatientId = "NULL";
		
		if (labTestReport.LabTestResultId != null) {
			LabTestResultId = labTestReport.LabTestResultId;
			LabTestResultId = "'"+LabTestResultId.replace("'","\\'")+"'";
		}
		if (labTestReport.LabTestType != null) {
			LabTestType = labTestReport.LabTestType;
			LabTestType = "'"+LabTestType.replace("'","\\'")+"'";
		}
		if (labTestReport.ReferenceRangeHigh != null) {
			ReferenceRangeHigh = labTestReport.ReferenceRangeHigh;
			ReferenceRangeHigh = "'"+ReferenceRangeHigh.replace("'","\\'")+"'";
		}
		if (labTestReport.PatientVisitId != null) {
			PatientVisitId = labTestReport.PatientVisitId;
			PatientVisitId = "'"+PatientVisitId.replace("'","\\'")+"'";
		}
		if (labTestReport.LabTestPerformedDate != null) {
			LabTestPerformedDate = labTestReport.LabTestPerformedDate;
			LabTestPerformedDate = "'"+LabTestPerformedDate.replace("'","\\'")+"'";
		}
		if (labTestReport.TestResultValue != null) {
			TestResultValue = labTestReport.TestResultValue;
			TestResultValue = "'"+TestResultValue.replace("'","\\'")+"'";
		}
		if (labTestReport.ReferenceRangeLow != null) {
			ReferenceRangeLow = labTestReport.ReferenceRangeLow;
			ReferenceRangeLow = "'"+ReferenceRangeLow.replace("'","\\'")+"'";
		}
		if (labTestReport.PatientId != null) {
			PatientId = labTestReport.PatientId;
			PatientId = "'"+PatientId.replace("'","\\'")+"'";
		}
		
		String insertMessage = "INSERT INTO " + HealthInformationSystem.TableNameLabTestReportOf + " VALUES ('" +LabTestResultId+ "','" +LabTestType+ "','" +ReferenceRangeHigh+ "','" +PatientVisitId+ "','" +LabTestPerformedDate+ "','" +TestResultValue+ "','" +ReferenceRangeLow+ "','" +PatientId+ "')";
		System.out.println(insertMessage);
		try {
			executeUpdate(conn, insertMessage);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private static void insertPatient(Patient patient, Connection conn) {
		String PatientId = "NULL";
		String FamilyName = "NULL";
		String GivenName = "NULL";
		String Suffix = "NULL";
		String BirthTime = "NULL";
		String Gender = "NULL";
		String ProviderId = "NULL";
		String xmlHealthCreationDateTime = "NULL";
		String GuardianNo = "NULL";
		String PayerId = "NULL";
		String PatientRole = "NULL";
		String PolicyType = "NULL";
		String PolicyHolder = "NULL";
		String Purpose = "NULL";

		if (patient.PatientId != null) {
			PatientId = patient.PatientId;
			PatientId = "'"+PatientId.replace("'","\\'")+"'";
		}
		if (patient.FamilyName != null) {
			FamilyName = patient.FamilyName;
			FamilyName = "'"+FamilyName.replace("'","\\'")+"'";
		}
		if (patient.GivenName != null) {
			GivenName = patient.GivenName;
			GivenName = "'"+GivenName.replace("'","\\'")+"'";
		}
		if (patient.Suffix != null) {
			Suffix = patient.Suffix;
			Suffix = "'"+Suffix.replace("'","\\'")+"'";
		}
		if (patient.BirthTime != null) {
			BirthTime = patient.BirthTime;
			BirthTime = "'"+BirthTime.replace("'","\\'")+"'";
		}
		if (patient.Gender != null) {
			Gender = patient.Gender;
			Gender = "'"+Gender.replace("'","\\'")+"'";
		}
		if (patient.ProviderId != null) {
			ProviderId = patient.ProviderId;
			ProviderId = "'"+ProviderId.replace("'","\\'")+"'";
		}
		if (patient.xmlHealthCreationDateTime != null) {
			xmlHealthCreationDateTime = patient.xmlHealthCreationDateTime;
			xmlHealthCreationDateTime = "'"+xmlHealthCreationDateTime.replace("'","\\'")+"'";
		}
		if (patient.GuardianNo != null) {
			GuardianNo = patient.GuardianNo;
			GuardianNo = "'"+GuardianNo.replace("'","\\'")+"'";
		}
		if (patient.PayerId != null) {
			PayerId = patient.PayerId;
			PayerId = "'"+PayerId.replace("'","\\'")+"'";
		}
		if (patient.PatientRole != null) {
			PatientRole = patient.PatientRole;
			PatientRole = "'"+PatientRole.replace("'","\\'")+"'";
		}
		if (patient.PolicyType != null) {
			PolicyType = patient.PolicyType;
			PolicyType = "'"+PolicyType.replace("'","\\'")+"'";
		}
		if (patient.PolicyHolder != null) {
			PolicyHolder = patient.PolicyHolder;
			PolicyHolder = "'"+PolicyHolder.replace("'","\\'")+"'";
		}
		if (patient.Purpose != null) {
			Purpose = patient.Purpose;
			Purpose = "'"+Purpose.replace("'","\\'")+"'";
		}

		String insertMessage = "INSERT INTO " + HealthInformationSystem.TableNamePatient + " VALUES ('" +PatientId+ "','" +FamilyName+ "','" +GivenName+ "','" +Suffix+ "','" +BirthTime+ "','" +Gender+ "','" +ProviderId+ "','" +xmlHealthCreationDateTime+ "','" +GuardianNo+ "','" +PayerId+ "','" +PatientRole+ "','" +PolicyType+ "','" +PolicyHolder+ "','" +Purpose+ "')";
		System.out.println(insertMessage);
		try {
			executeUpdate(conn, insertMessage);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private static void insertPlanScheduledFor(PlanScheduledFor planScheduledFor, Connection conn) {
		String PlanId = "NULL";
		String PlanName = "NULL";
		String PatientId = "NULL";
		String DateScheduled = "NULL";
		
		if (planScheduledFor.PlanId != null) {
			PlanId = planScheduledFor.PlanId;
			PlanId = "'"+PlanId.replace("'","\\'")+"'";
		}
		if (planScheduledFor.PlanName != null) {
			PlanName = planScheduledFor.PlanName;
			PlanName = "'"+PlanName.replace("'","\\'")+"'";
		}
		if (planScheduledFor.PatientId != null) {
			PatientId = planScheduledFor.PatientId;
			PatientId = "'"+PatientId.replace("'","\\'")+"'";
		}
		if (planScheduledFor.DateScheduled != null) {
			DateScheduled = planScheduledFor.DateScheduled;
			DateScheduled = "'"+DateScheduled.replace("'","\\'")+"'";
		}

		String insertMessage = "INSERT INTO " + HealthInformationSystem.TableNamePlanScheduledFor + " VALUES ('" +PlanId+ "','" +PlanName+ "','" +PatientId+ "','" +DateScheduled+ "')";
		System.out.println(insertMessage);
		try {
			executeUpdate(conn, insertMessage);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private static void insertSubstance(Substance substance, Connection conn) {
		String SubstanceId = "NULL";
		String SubstanceName = "NULL";
		
		if (substance.SubstanceId != null) {
			SubstanceId = substance.SubstanceId;
			SubstanceId = "'"+SubstanceId.replace("'","\\'")+"'";
		}
		if (substance.SubstanceName != null) {
			SubstanceName = substance.SubstanceName;
			SubstanceName = "'"+SubstanceName.replace("'","\\'")+"'";
		}
		
		String insertMessage = "INSERT INTO " + HealthInformationSystem.TableNameSubstance + " VALUES ('" +SubstanceId+ "','" +SubstanceName+ "')";
		System.out.println(insertMessage);
		try {
			executeUpdate(conn, insertMessage);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
}

