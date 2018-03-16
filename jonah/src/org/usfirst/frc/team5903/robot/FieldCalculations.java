package org.usfirst.frc.team5903.robot;

public class FieldCalculations {
	String Follow;
	String m_teamLoc;
	public String Path;

	public String Pathid() {
		char Charswitch;
		char Charscale;
		String Switch;
		String Scale;
		// Get information from the Field Management System (FMS)
		FieldInfo m_teamInfo = new FieldInfo();
		m_teamLoc = m_teamInfo.getFieldInfo();
		Charswitch = m_teamLoc.charAt(1);
		Charscale = m_teamLoc.charAt(2);
		Switch = String.valueOf(Charswitch); // convert Charswitch to Switch, because chars and strings dont like each
												// other
		Scale = String.valueOf(Charscale); // above but with Scale
		if (Switch.equals("L")) { // check for switch being L
			System.out.println("Left Switch");
			if (Scale.equals("L")) { // check scale being L
				Path = "11";
				System.out.println("Left Scale");
			} else if (Scale.equals("R")) {// check scale being R
				Path = "14";
				System.out.println("Right Scale");
			}
		} else if (Switch.equals("R")) {// check switch being R
			System.out.println("Right Switch");
			if (Scale.equals("L")) {// check scale being L
				Path = "23";
				System.out.print("Left Scale");
				System.out.print(Path);
			} else if (Scale.equals("R")) {// check scale being R
				Path = "22";
				System.out.println("Right Scale");
			}
		}
		return Path;
	}
}