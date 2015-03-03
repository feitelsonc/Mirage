
public class PlanScheduledFor {
	
	public String PlanId;
	public String PlanName;
	public String PatientId;
	public String DateScheduled;
	
	public PlanScheduledFor(String planId, String planName, String patientId, String dateScheduled) {
		PlanId = planId;
		PlanName = planName;
		PatientId = patientId;
		DateScheduled = dateScheduled;
	}
	
	public String toString() {
		return "PlanId: " + PlanId + "\n"
				+ "PlanName: " + PlanName + "\n"
				+ "PatientId: " + PatientId + "\n"
				+ "DateScheduled: " + DateScheduled;
	}
	
}
