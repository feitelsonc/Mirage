
public class Guardian {
	public String GuardianNo;
	public String Phone;
	public String Address;
	public String State;
	public String GivenName;
	public String FamilyName;
	public String City;
	public String Zip;
	
	public Guardian(String GuardianNo, String Phone, String Address, String State, String GivenName, String FamilyName, String City, String Zip) {
		this.GuardianNo = GuardianNo;
		this.Phone = Phone;
		this.Address = Address;
		this.State = State;
		this.GivenName = GivenName;
		this.FamilyName = FamilyName;
		this.City = City;
		this.Zip = Zip;
	}
	
	public String toString() {
		return "GuardianNo: " + GuardianNo + "\n"
				+ "Phone: " + Phone + "\n"
				+ "Address: " + Address + "\n"
				+ "State: " + State + "\n"
				+ "GivenName: " + GivenName + "\n"
				+ "FamilyName: " + FamilyName + "\n"
				+ "City: " + City + "\n"
				+ "Zip: " + Zip;
	}
	
}
