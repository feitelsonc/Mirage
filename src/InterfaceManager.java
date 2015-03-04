import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


public class InterfaceManager {
	
	public static void execute() {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String in;
		boolean invalidInput = true;
		try {
			while (invalidInput) {
				System.out.println("Login as Patient, Doctor, or Adminstrator?");
				in = br.readLine();
				
				if (in.toLowerCase().equals("patient")) {
					invalidInput = false;
					patientInterface();
				}
				else if (in.toLowerCase().equals("doctor")) {
					invalidInput = false;
					doctorInterface();
				}
				else if (in.toLowerCase().equals("adminstrator")) {
					invalidInput = false;
					adminstratorInterface();
				}
				else {
					System.out.println("Login mode not recognized. Check spelling and try again.");
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void patientInterface() {
		System.out.println("Entering patient mode.");
		
	}
	
	public static void doctorInterface() {
		System.out.println("Entering doctor mode.");
		
	}
	
	public static void adminstratorInterface() {
		System.out.println("Entering adminstrator mode.");
		
	}

}
