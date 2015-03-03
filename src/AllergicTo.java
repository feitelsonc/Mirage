
public class AllergicTo {
	
	public String Reaction;
	public String Active;
	public String SubstanceId;
	public String PatientId;
	
	public AllergicTo(String reaction, String active, String substanceId, String patientId) {
		Reaction = reaction;
		Active = active;
		SubstanceId = substanceId;
		PatientId = patientId;
	}
	
	public String toString() {
		return "Reaction: " + Reaction + "\n"
				+ "Active: " + Active + "\n"
				+ "SubstanceId: " + SubstanceId + "\n"
				+ "PatientId: " + SubstanceId;
	}

}
