
public class Insurance {
	
	public String PayerId;
	public String Name;
	
	public Insurance(String PayerId, String Name) {
		this.PayerId = PayerId;
		this.Name = Name;
	}
	
	public String toString() {
		return "PayerId: " + PayerId + "\n"
				+ "Name: " + Name;
	}

}
