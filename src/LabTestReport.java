public class LabTestReport {
	
	public String LabTestResultId;
	public String LabTestType;
	public String ReferenceRangeHigh;
	public String PatientVisitId;
	public String LabTestPerformedDate;
	public String TestResultValue;
	public String ReferenceRangeLow;
	public String PatientId;
	
	public LabTestReport(String labTestResultId, String labTestType, String referenceRangeHigh, String patientVisitId, String labTestPerformedDate, String testResultValue, String referenceRangeLow, String patientId) {
		LabTestResultId = labTestResultId;
		LabTestType = labTestType;
		ReferenceRangeHigh = referenceRangeHigh;
		PatientVisitId = patientVisitId;
		LabTestPerformedDate = labTestPerformedDate;
		TestResultValue = testResultValue;
		ReferenceRangeLow = referenceRangeLow;
		PatientId = patientId;
	}
	
	public String toString() {
		return "LabTestResultId: " + LabTestResultId + "\n"
				+ "LabTestType: " + LabTestType + "\n"
				+ "ReferenceRangeHigh: " + ReferenceRangeHigh + "\n"
				+ "PatientVisitId: " + PatientVisitId + "\n"
				+ "LabTestPerformedDate: " + LabTestPerformedDate + "\n"
				+ "TestResultValue: " + TestResultValue + "\n"
				+ "ReferenceRangeLow: " + ReferenceRangeLow + "\n"
				+ "PatientId: " + PatientId;
	}

}
