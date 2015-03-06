import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

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

				String accessTimeOfMessages = "'"+Long.valueOf(System.currentTimeMillis()).toString()+"'";

				String queryAttribute, queryAttribute1;

				// insert entire tuple from messages into HealthInformationSystem's table because tuple is new
				if (Last_Accessed == null) {

					Statement statement = connMessage.createStatement();
					ResultSet result = null;

					if(PayerId!=null) {
						insurance = new Insurance(PayerId, Name);

						statement = connHealth.createStatement();
						queryAttribute = "'"+PayerId.replace("'","\\'")+"'";
						result = statement.executeQuery("SELECT * FROM Insurance WHERE PayerId="+queryAttribute);
						if(result.next()) {
							insertOrUpdateInsurance(insurance, connHealth, false);

						}
						else {
							insertOrUpdateInsurance(insurance, connHealth, true);
						}
					}

					if(GuardianNo!=null) {
						guardian = new Guardian(GuardianNo, phone, address, state, FirstName, LastName, city, zip);	

						statement = connHealth.createStatement();
						queryAttribute = "'"+GuardianNo.replace("'","\\'")+"'";
						result = statement.executeQuery("SELECT * FROM Guardian WHERE GuardianNo="+queryAttribute);
						if(result.next()) {
							insertOrUpdateGuardian(guardian, connHealth, false);
						}
						else {
							insertOrUpdateGuardian(guardian, connHealth, true);
						}

					}

					if(patientId!=null && GuardianNo!=null && PayerId!=null) {
						patient = new Patient(patientId, FamilyName, GivenName, null, BirthTime, null, providerId, accessTimeOfMessages, GuardianNo, PayerId, Relationship, PolicyType, PolicyHolder, Purpose);

						statement = connHealth.createStatement();
						queryAttribute = "'"+patientId.replace("'","\\'")+"'";
						result = statement.executeQuery("SELECT * FROM Patient WHERE PatientId="+queryAttribute);

						if(result.next()) {
							insertOrUpdatePatient(patient, connHealth, false);
						}
						else {
							insertOrUpdatePatient(patient, connHealth, true);
						}
					}

					if(LabTestResultId!=null && patientId!=null) {
						labTestReport = new LabTestReport(LabTestResultId, LabTestType, ReferenceRangeHigh, PatientVisitId, LabTestPerformedDate, TestResultValue, ReferenceRangeLow, patientId);

						statement = connHealth.createStatement();
						queryAttribute = "'"+LabTestResultId.replace("'","\\'")+"'";
						queryAttribute1 = "'"+patientId.replace("'","\\'")+"'";
						result = statement.executeQuery("SELECT * FROM Lab_Test_Report_Of WHERE LabTestResultId="+queryAttribute +" AND PatientId="+queryAttribute1);
						if(result.next()) {
							insertOrUpdateLabTestReport(labTestReport, connHealth, false);
						}
						else {
							insertOrUpdateLabTestReport(labTestReport, connHealth, true);
						}

					}

					if(RelativeId!=null && patientId!=null) {
						familyMember = new FamilyMember(RelativeId, age, Relation, Diagnosis, patientId);

						statement = connHealth.createStatement();
						queryAttribute = "'"+RelativeId.replace("'","\\'")+"'";
						queryAttribute1 = "'"+patientId.replace("'","\\'")+"'";
						result = statement.executeQuery("SELECT * FROM Family_Member_Of_Patient WHERE Id="+queryAttribute + " AND PatientId="+queryAttribute1);
						if(result.next()) {
							insertOrUpdateFamilyMember(familyMember, connHealth, false);
						}
						else {
							insertOrUpdateFamilyMember(familyMember, connHealth, true);
						}
					}

					if(AuthorId!=null) {
						author = new Author(AuthorId, AuthorFirstName, AuthorTitle, AuthorLastName);

						statement = connHealth.createStatement();
						queryAttribute = "'"+AuthorId.replace("'","\\'")+"'";
						System.out.println("SELECT * FROM Author WHERE AuthorId="+AuthorId);
						result = statement.executeQuery("SELECT * FROM Author WHERE AuthorId="+queryAttribute);
						if(result.next()) {
							insertOrUpdateAuthor(author, connHealth, false);
						}
						else {
							insertOrUpdateAuthor(author, connHealth, true);
						}
					}

					if(AuthorId!=null && patientId!=null) {
						assignedTo = new AssignedTo(AuthorId, patientId, ParticipatingRole);

						statement = connHealth.createStatement();
						queryAttribute = "'"+AuthorId.replace("'","\\'")+"'";
						queryAttribute1 = "'"+patientId.replace("'","\\'")+"'";
						result = statement.executeQuery("SELECT * FROM Assigned_To WHERE AuthorId="+queryAttribute + " AND PatientId="+queryAttribute1);
						if(result.next()) {
							insertOrUpdateAssignedTo(assignedTo, connHealth, false);
						}
						else {
							insertOrUpdateAssignedTo(assignedTo, connHealth, true);
						}
					}

					if(Id!=null) {
						substance = new Substance(Id, Substance);

						statement = connHealth.createStatement();
						queryAttribute = "'"+Id.replace("'","\\'")+"'";
						result = statement.executeQuery("SELECT * FROM Substance WHERE SubstanceId="+queryAttribute);
						if(result.next()) {
							insertOrUpdateSubstance(substance, connHealth, false);
						}
						else {
							insertOrUpdateSubstance(substance, connHealth, true);
						}
					}

					if(Id!=null && patientId!=null) {
						allergicTo = new AllergicTo(Reaction, Status, Id, patientId);

						statement = connHealth.createStatement();
						queryAttribute = "'"+Id.replace("'","\\'")+"'";
						queryAttribute1 = "'"+patientId.replace("'","\\'")+"'";
						result = statement.executeQuery("SELECT * FROM Allergic_To WHERE SubstanceId="+queryAttribute + " AND PatientId="+queryAttribute1);
						if(result.next()) {
							insertOrUpdateAllergicTo(allergicTo, connHealth, false);
						}
						else {
							insertOrUpdateAllergicTo(allergicTo, connHealth, true);
						}
					}

					if(PlanId!=null && patientId!=null) {
						planScheduledFor = new PlanScheduledFor(PlanId, Activity, patientId, ScheduledDate);

						statement = connHealth.createStatement();
						queryAttribute = "'"+Id.replace("'","\\'")+"'";
						queryAttribute1 = "'"+patientId.replace("'","\\'")+"'";
						result = statement.executeQuery("SELECT * FROM Plan_Scheduled_For WHERE PlanId="+queryAttribute + " AND PatientId="+queryAttribute1);
						if(result.next()) {
							insertOrUpdatePlanScheduledFor(planScheduledFor, connHealth, false);
						}
						else {
							insertOrUpdatePlanScheduledFor(planScheduledFor, connHealth, true);
						}
					}

				}
				// either update tables in HealthInformationSystem's table because tuple in messages is more recent or ignore tuple if Last_Accessed time is older/equal to the patient's XmlHealthCreationDateTime
				else {
					// get creation time of patient
					String time;
					Statement statement = connHealth.createStatement();;
					ResultSet result = null;
					queryAttribute = "'"+patientId.replace("'","\\'")+"'";
					result = statement.executeQuery("SELECT xmlHealthCreationDateTime FROM Patient WHERE PatientId="+queryAttribute);
					boolean patientAlreadyExists = result.next();

					if (patientAlreadyExists) {
						time= result.getString("xmlHealthCreationDateTime");
					}
					else {
						time= "0";
					}

					if(Long.valueOf(Last_Accessed).longValue() > Long.valueOf(time).longValue()) {
						if(PayerId!=null) {
							insurance = new Insurance(PayerId, Name);

							statement = connHealth.createStatement();
							queryAttribute = "'"+PayerId.replace("'","\\'")+"'";
							result = statement.executeQuery("SELECT * FROM Insurance WHERE PayerId="+queryAttribute);
							if(result.next()) {
								insertOrUpdateInsurance(insurance, connHealth, false);

							}
							else {
								insertOrUpdateInsurance(insurance, connHealth, true);
							}
						}

						if(GuardianNo!=null) {
							guardian = new Guardian(GuardianNo, phone, address, state, FirstName, LastName, city, zip);	

							statement = connHealth.createStatement();
							queryAttribute = "'"+GuardianNo.replace("'","\\'")+"'";
							result = statement.executeQuery("SELECT * FROM Guardian WHERE GuardianNo="+queryAttribute);
							if(result.next()) {
								insertOrUpdateGuardian(guardian, connHealth, false);
							}
							else {
								insertOrUpdateGuardian(guardian, connHealth, true);
							}

						}

						if(patientId!=null && GuardianNo!=null && PayerId!=null) {
							patient = new Patient(patientId, FamilyName, GivenName, null, BirthTime, null, providerId, accessTimeOfMessages, GuardianNo, PayerId, Relationship, PolicyType, PolicyHolder, Purpose);

							statement = connHealth.createStatement();
							queryAttribute = "'"+patientId.replace("'","\\'")+"'";
							result = statement.executeQuery("SELECT * FROM Patient WHERE PatientId="+queryAttribute);

							if(result.next()) {
								insertOrUpdatePatient(patient, connHealth, false);
							}
							else {
								insertOrUpdatePatient(patient, connHealth, true);
							}
						}

						if(LabTestResultId!=null && patientId!=null) {
							labTestReport = new LabTestReport(LabTestResultId, LabTestType, ReferenceRangeHigh, PatientVisitId, LabTestPerformedDate, TestResultValue, ReferenceRangeLow, patientId);

							statement = connHealth.createStatement();
							queryAttribute = "'"+LabTestResultId.replace("'","\\'")+"'";
							queryAttribute1 = "'"+patientId.replace("'","\\'")+"'";
							result = statement.executeQuery("SELECT * FROM Lab_Test_Report_Of WHERE LabTestResultId="+queryAttribute +" AND PatientId="+queryAttribute1);
							if(result.next()) {
								insertOrUpdateLabTestReport(labTestReport, connHealth, false);
							}
							else {
								insertOrUpdateLabTestReport(labTestReport, connHealth, true);
							}

						}

						if(RelativeId!=null && patientId!=null) {
							familyMember = new FamilyMember(RelativeId, age, Relation, Diagnosis, patientId);

							statement = connHealth.createStatement();
							queryAttribute = "'"+RelativeId.replace("'","\\'")+"'";
							queryAttribute1 = "'"+patientId.replace("'","\\'")+"'";
							result = statement.executeQuery("SELECT * FROM Family_Member_Of_Patient WHERE Id="+queryAttribute + " AND PatientId="+queryAttribute1);
							if(result.next()) {
								insertOrUpdateFamilyMember(familyMember, connHealth, false);
							}
							else {
								insertOrUpdateFamilyMember(familyMember, connHealth, true);
							}
						}

						if(AuthorId!=null) {
							author = new Author(AuthorId, AuthorFirstName, AuthorTitle, AuthorLastName);

							statement = connHealth.createStatement();
							queryAttribute = "'"+AuthorId.replace("'","\\'")+"'";
//							System.out.println("SELECT * FROM Author WHERE AuthorId="+AuthorId);
							result = statement.executeQuery("SELECT * FROM Author WHERE AuthorId="+queryAttribute);
							if(result.next()) {
								insertOrUpdateAuthor(author, connHealth, false);
							}
							else {
								insertOrUpdateAuthor(author, connHealth, true);
							}
						}

						if(AuthorId!=null && patientId!=null) {
							assignedTo = new AssignedTo(AuthorId, patientId, ParticipatingRole);

							statement = connHealth.createStatement();
							queryAttribute = "'"+AuthorId.replace("'","\\'")+"'";
							queryAttribute1 = "'"+patientId.replace("'","\\'")+"'";
							result = statement.executeQuery("SELECT * FROM Assigned_To WHERE AuthorId="+queryAttribute + " AND PatientId="+queryAttribute1);
							if(result.next()) {
								insertOrUpdateAssignedTo(assignedTo, connHealth, false);
							}
							else {
								insertOrUpdateAssignedTo(assignedTo, connHealth, true);
							}
						}

						if(Id!=null) {
							substance = new Substance(Id, Substance);

							statement = connHealth.createStatement();
							queryAttribute = "'"+Id.replace("'","\\'")+"'";
							result = statement.executeQuery("SELECT * FROM Substance WHERE SubstanceId="+queryAttribute);
							if(result.next()) {
								insertOrUpdateSubstance(substance, connHealth, false);
							}
							else {
								insertOrUpdateSubstance(substance, connHealth, true);
							}
						}

						if(Id!=null && patientId!=null) {
							allergicTo = new AllergicTo(Reaction, Status, Id, patientId);

							statement = connHealth.createStatement();
							queryAttribute = "'"+Id.replace("'","\\'")+"'";
							queryAttribute1 = "'"+patientId.replace("'","\\'")+"'";
							result = statement.executeQuery("SELECT * FROM Allergic_To WHERE SubstanceId="+queryAttribute + " AND PatientId="+queryAttribute1);
							if(result.next()) {
								insertOrUpdateAllergicTo(allergicTo, connHealth, false);
							}
							else {
								insertOrUpdateAllergicTo(allergicTo, connHealth, true);
							}
						}

						if(PlanId!=null && patientId!=null) {
							planScheduledFor = new PlanScheduledFor(PlanId, Activity, patientId, ScheduledDate);

							statement = connHealth.createStatement();
							queryAttribute = "'"+Id.replace("'","\\'")+"'";
							queryAttribute1 = "'"+patientId.replace("'","\\'")+"'";
							result = statement.executeQuery("SELECT * FROM Plan_Scheduled_For WHERE PlanId="+queryAttribute + " AND PatientId="+queryAttribute1);
							if(result.next()) {
								insertOrUpdatePlanScheduledFor(planScheduledFor, connHealth, false);
							}
							else {
								insertOrUpdatePlanScheduledFor(planScheduledFor, connHealth, true);
							}
						}
					}
					else {
						// do nothing because value in HealthInformationSystem is more recent than value in messages
					}
				}
				
				// update access time of messages table
				String currMsgId = "'"+MsgId+"'";
				String updateMessage = "UPDATE messages "
						+ "SET Last_Accessed=" + accessTimeOfMessages + " "
						+ "WHERE MsgId=" + currMsgId;
				try {
					executeUpdate(connMessage, updateMessage);
				} catch (SQLException e) {
					e.printStackTrace();
				}
				
			}
//			System.out.println("Parsing Succeeded");

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
	
	/*******************************************************************/
	
	private static void insertOrUpdateAllergicTo(AllergicTo allergicTo, Connection conn, boolean isInsert) {
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
		
		if (isInsert){
			String insertMessage = "INSERT INTO " + HealthInformationSystem.TableNameAllergicTo + " VALUES (" + Reaction + "," + Active + "," + SubstanceId + "," + PatientId + ")";
//			System.out.println(insertMessage);
			try {
				executeUpdate(conn, insertMessage);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		else {
			String updateMessage = "UPDATE " + HealthInformationSystem.TableNameAllergicTo + 
					" SET Reaction=" + Reaction + " ," +
					" Active=" + PatientId + " ," +
					" SubstanceId=" + SubstanceId + " ," +
					" PatientId=" + PatientId +
					" WHERE SubstanceId=" + SubstanceId;
			try {
				
				executeUpdate(conn,updateMessage);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	private static void insertOrUpdateAssignedTo(AssignedTo assignedTo, Connection conn, boolean isInsert) {
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
		
		if (isInsert) {
			String insertMessage = "INSERT INTO " + HealthInformationSystem.TableNameAssignedTo + " VALUES (" + AuthorId + "," + PatientId + "," + ParticipatingRole + ")";
//			System.out.println(insertMessage);
			try {
				executeUpdate(conn, insertMessage);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		else {
			String updateMessage = "UPDATE " + HealthInformationSystem.TableNameAssignedTo + 
					" SET AuthorId=" + AuthorId + " ," +
					" PatientId=" + PatientId + " ," +
					" SubstanceId=" + ParticipatingRole +
					" WHERE AuthorId=" + AuthorId + " AND" +
					" PatientId=" + PatientId;
			try {
				
				executeUpdate(conn,updateMessage);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	private static void insertOrUpdateAuthor(Author author, Connection conn, boolean isInsert) {
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
		
		if (isInsert) {
			String insertMessage = "INSERT INTO " + HealthInformationSystem.TableNameAuthor + " VALUES (" + AuthorId + "," + AuthorFirstName + "," + AuthorTitle + "," + AuthorLastName + ")";
//			System.out.println(insertMessage);
			try {
				executeUpdate(conn, insertMessage);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		else {
			String updateMessage = "UPDATE " + HealthInformationSystem.TableNameAuthor + 
					" SET AuthorId=" + AuthorId + " ," +
					" AuthorFirstName=" + AuthorFirstName + " ," +
					" AuthorTitle=" + AuthorTitle + " ," +
					" AuthorLastName=" + AuthorLastName +
					" WHERE AuthorId=" + AuthorId;
			try {
				
				executeUpdate(conn,updateMessage);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	private static void insertOrUpdateFamilyMember(FamilyMember familyMember, Connection conn, boolean isInsert) {
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
		
		if (isInsert) {
			String insertMessage = "INSERT INTO " + HealthInformationSystem.TableNameFamilyMemberOfPatient + " VALUES (" + Id + "," + Age + "," + Relationship + "," + Diagnosis + "," + PatientId + ")";
//			System.out.println(insertMessage);
			try {
				executeUpdate(conn, insertMessage);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		else {
			String updateMessage = "UPDATE " + HealthInformationSystem.TableNameFamilyMemberOfPatient + 
					" SET Id=" + Id + " ," +
					" Age=" + Age + " ," +
					" Relationship=" + Relationship + " ," +
					" PatientId=" + PatientId + " ," +
					" Diagnosis=" + Diagnosis +
					" WHERE PatientId=" + PatientId + " AND" +
					" Id=" + Id;
			try {
				
				executeUpdate(conn,updateMessage);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	private static void insertOrUpdateGuardian(Guardian guardian, Connection conn, boolean isInsert) {
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
		
		if (isInsert) {
			String insertMessage = "INSERT INTO " + HealthInformationSystem.TableNameGuardian + " VALUES (" + GuardianNo + "," + Phone + "," + Address + "," + State + "," + GivenName + "," + FamilyName + "," + City + "," + Zip + ")";
//			System.out.println(insertMessage);
			try {
				executeUpdate(conn, insertMessage);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		else {
			String updateMessage = "UPDATE " + HealthInformationSystem.TableNameGuardian + 
					" SET GuardianNo=" + GuardianNo + " ," +
					" Phone=" + Phone + " ," +
					" Address=" + Address + " ," +
					" State=" + State + " ," +
					" GivenName=" + GivenName + " ," +
					" FamilyName=" + FamilyName + " ," +
					" Zip=" + Zip + " ," +
					" City=" + City +
					" WHERE GuardianNo=" + GuardianNo;
			try {
				
				executeUpdate(conn,updateMessage);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	private static void insertOrUpdateInsurance(Insurance insurance, Connection conn, boolean isInsert) {
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
		
		if (isInsert) {
			String insertMessage = "INSERT INTO " + HealthInformationSystem.TableNameInsurance + " VALUES (" + PayerId + "," + Name + ")";
//			System.out.println(insertMessage);
			try {
				executeUpdate(conn, insertMessage);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		else {
			String updateMessage = "UPDATE " + HealthInformationSystem.TableNameInsurance + 
					" SET PayerId=" + PayerId + " ," +
					" Name=" + Name +
					" WHERE PayerId=" + PayerId;
			try {
				
				executeUpdate(conn,updateMessage);
			} catch (SQLException e) {
				e.printStackTrace();
			}		
		}
	}
	
	private static void insertOrUpdateLabTestReport(LabTestReport labTestReport, Connection conn, boolean isInsert) {
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
		
		if (isInsert) {
			String insertMessage = "INSERT INTO " + HealthInformationSystem.TableNameLabTestReportOf + " VALUES (" + LabTestResultId + "," + LabTestType + "," + ReferenceRangeHigh + "," + PatientVisitId + "," + LabTestPerformedDate + "," + TestResultValue + "," + ReferenceRangeLow + "," + PatientId + ")";
//			System.out.println(insertMessage);
			try {
				executeUpdate(conn, insertMessage);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		else {
			String updateMessage = "UPDATE " + HealthInformationSystem.TableNameLabTestReportOf + 
					" SET LabTestResultId=" + LabTestResultId + " ," +
					" LabTestType=" + LabTestType + " ," +
					" ReferenceRangeHigh=" + ReferenceRangeHigh + " ," +
					" PatientVisitId=" + PatientVisitId + " ," +
					" LabTestPerformedDate=" + LabTestPerformedDate + " ," +
					" TestResultValue=" + TestResultValue + " ," +
					" ReferenceRangeLow=" + ReferenceRangeLow + " ," +
					" PatientId=" + PatientId +
					" WHERE LabTestResultId=" + LabTestResultId;
			try {
				
				executeUpdate(conn,updateMessage);
			} catch (SQLException e) {
				e.printStackTrace();
			}		
		}
	}
	
	private static void insertOrUpdatePatient(Patient patient, Connection conn, boolean isInsert) {
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

		if (isInsert) {
			String insertMessage = "INSERT INTO " + HealthInformationSystem.TableNamePatient + " VALUES (" + PatientId + "," + FamilyName + "," + GivenName + "," + Suffix + "," + BirthTime + "," + Gender + "," + ProviderId + "," + xmlHealthCreationDateTime + "," + GuardianNo + "," + PayerId + "," + PatientRole + "," + PolicyType + "," + PolicyHolder + "," + Purpose + ")";
//			System.out.println(insertMessage);
			try {
				executeUpdate(conn, insertMessage);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		else {
			String updateMessage = "UPDATE " + HealthInformationSystem.TableNamePatient + 
					" SET PatientId=" + PatientId + " ," +
					" FamilyName=" + FamilyName + " ," +
					" GivenName=" + GivenName + " ," +
					" Suffix=" + Suffix + " ," +
					" BirthTime=" + BirthTime + " ," +
					" Gender=" + Gender + " ," +
					" ProviderId=" + ProviderId + " ," +
					" GuardianNo=" + GuardianNo + " ," +
					" PayerId=" + PayerId + " ," +
					" PatientRole=" + PatientRole + " ," +
					" PolicyType=" + PolicyType + " ," +
					" PolicyHolder=" + PolicyHolder + " ," +
					" Purpose=" + Purpose + " ," +
					" xmlHealthCreationDateTime=" + xmlHealthCreationDateTime +
					" WHERE PatientId=" + PatientId;
			try {
				
				executeUpdate(conn,updateMessage);
			} catch (SQLException e) {
				e.printStackTrace();
			}				
		}
	}
	
	private static void insertOrUpdatePlanScheduledFor(PlanScheduledFor planScheduledFor, Connection conn, boolean isInsert) {
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

		if (isInsert) {
			String insertMessage = "INSERT INTO " + HealthInformationSystem.TableNamePlanScheduledFor + " VALUES (" + PlanId + "," + PlanName + "," + PatientId + "," + DateScheduled + ")";
//			System.out.println(insertMessage);
			try {
				executeUpdate(conn, insertMessage);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		else {
			String updateMessage = "UPDATE " + HealthInformationSystem.TableNamePlanScheduledFor + 
					" SET PlanId=" + PlanId + " ," +
					" PlanName=" + PlanName + " ," +
					" PatientId=" + PatientId + " ," +
					" DateScheduled=" + DateScheduled +
					" WHERE PlanId=" + PlanId;
			try {
				
				executeUpdate(conn,updateMessage);
			} catch (SQLException e) {
				e.printStackTrace();
			}	
		}
	}
	
	private static void insertOrUpdateSubstance(Substance substance, Connection conn, boolean isInsert) {
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
		
		if (isInsert) {
			String insertMessage = "INSERT INTO " + HealthInformationSystem.TableNameSubstance + " VALUES (" + SubstanceId + "," + SubstanceName + ")";
//			System.out.println(insertMessage);
			try {
				executeUpdate(conn, insertMessage);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		else {
			String updateMessage = "UPDATE " + HealthInformationSystem.TableNameSubstance + 
					" SET SubstanceId=" + SubstanceId + " ," +
					" SubstanceName=" + SubstanceName +
					" WHERE SubstanceId=" + SubstanceId;
			try {
				
				executeUpdate(conn,updateMessage);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
}

