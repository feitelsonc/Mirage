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
				suffix = rs.getString("suffix");
				gender = rs.getString("gender");

				if(PayerId!=null) {
					insurance = new Insurance(PayerId, Name);
				}

				if(GuardianNo!=null) {
					guardian = new Guardian(GuardianNo, phone, address, state, FirstName, LastName, city, zip);	
				}

				if(patientId!=null) {
					patient = new Patient(patientId, FamilyName, GivenName, suffix, BirthTime, gender, providerId, Last_Accessed, GuardianNo, PayerId, Relationship, PolicyType, PolicyHolder, Purpose);
				}

				if(LabTestResultId!=null)
				{
					labTestReport = new LabTestReport(LabTestResultId, LabTestType, ReferenceRangeHigh, PatientVisitId, LabTestPerformedDate, TestResultValue, ReferenceRangeLow, patientId);
				}

				if(RelativeId!=null && patientId!=null) {
					familyMember = new FamilyMember(RelativeId, age, Relation, Diagnosis, patientId);
				}

				if(AuthorId!=null) {
					author = new Author(AuthorId, AuthorFirstName, AuthorTitle, AuthorLastName);
				}

				if(AuthorId!=null && patientId!=null)
				{
					assignedTo = new AssignedTo(AuthorId, patientId, ParticipatingRole);
				}

				if(Id!=null)
				{
					substance = new Substance(Id, Substance);
				}

				if(Id!=null && patientId!=null) {
					allergicTo = new AllergicTo(Reaction, Status, Id, patientId);
				}

				if(PlanId!=null) {
					planScheduledFor = new PlanScheduledFor(PlanId, Activity, patientId, ScheduledDate);
				}
			}

		}catch (SQLException e) {
			e.printStackTrace();
		}
	}
}

