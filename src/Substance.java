
public class Substance {
	
	public String SubstanceId;
	public String SubstanceName;
	
	public Substance(String substanceId, String substanceName) {
		SubstanceId = substanceId;
		SubstanceName = substanceName;
	}
	
	public String toString() {
		return "SubstanceId: " + SubstanceId + "\n"
				+ "SubstanceName: " + SubstanceName;
	}

}
