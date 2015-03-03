import java.util.Date;

public class Patient {
	
	public String PatientId;
	public String FamilyName;
	public String GivenName;
	public String Suffix;
	public String BirthTime;
	public String Gender;
	public String ProviderId;
	public String xmlHealthCreationDateTime;
	public String GuardianNo;
	public String PayerId;
	public String PatientRole;
	public String PolicyType;
	public String PolicyHolder;
	public String Purpose;
	
	public Patient(String patientId, String familyName, String givenName, String suffix, String birthTime, String gender, String providerId, String XmlHealthCreationDateTime, String guardianNo, String payerId, String patientRole, String policyType, String policyHolder, String purpose) {
		PatientId = patientId;
		FamilyName = familyName;
		GivenName = givenName;
		Suffix = suffix;
		BirthTime = birthTime;
		Gender = gender;
		ProviderId = providerId;
		xmlHealthCreationDateTime = XmlHealthCreationDateTime;
		GuardianNo = guardianNo;
		PayerId = payerId;
		PatientRole = patientRole;
		PolicyType = policyType;
		PolicyHolder = policyHolder;
		Purpose = purpose;
	}
	
	public String toString() {
		return "PatientId: " + PatientId + "\n"
				+ "FamilyName: " + FamilyName + "\n"
				+ "GivenName: " + GivenName + "\n"
				+ "Suffix: " + Suffix + "\n"
				+ "BirthTime: " + BirthTime + "\n"
				+ "Gender: " + Gender + "\n"
				+ "ProviderId: " + ProviderId + "\n"
				+ "xmlHealthCreationDateTime: " + xmlHealthCreationDateTime + "\n"
				+ "GuardianNo: " + GuardianNo + "\n"
				+ "PayerId: " + PayerId + "\n"
				+ "PatientRole: " + PatientRole + "\n"
				+ "PolicyType: " + PolicyType + "\n"
				+ "PolicyHolder: " + PolicyHolder + "\n"
				+ "Purpose: " + Purpose;
	}

}
