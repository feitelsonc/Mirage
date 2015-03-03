
public class AssignedTo {
	
	public String AuthorId;
	public String PatientId;
	public String ParticipatingRole;
	
	public AssignedTo(String authorId, String patientId, String participatingRole) {
		AuthorId = authorId;
		PatientId = patientId;
		ParticipatingRole = participatingRole;
	}
	
	public String toString() {
		return "AuthorId: " + AuthorId + "\n"
				+ "PatientId: " + PatientId + "\n"
				+ "ParticipatingRole: " + ParticipatingRole;
	}

}
