
public class FamilyMember {
	
	public String Id;
	public String Age;
	public String Relationship;
	public String Diagnosis;
	public String PatientId;
	
	public FamilyMember(String id, String age, String relationship, String diagnosis, String patientId) {
		Id = id;
		Age = age;
		Relationship = relationship;
		Diagnosis = diagnosis;
		PatientId = patientId;
	}
	
	public String toString() {
		return "Id: " + Id + "\n"
				+ "Age: " + Age + "\n"
				+ "Relationship: " + Relationship + "\n"
				+ "Diagnosis: " + Diagnosis + "\n"
				+ "PatientId: " + PatientId;
	}

}
