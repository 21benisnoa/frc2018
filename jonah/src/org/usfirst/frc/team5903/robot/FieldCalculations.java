package org.usfirst.frc.team5903.robot;

import org.usfirst.frc.team5903.robot.FieldInfo;

public class FieldCalculations {
	String Follow;
	String m_teamLoc;
	public String Path;
	String Switch;
	String Scale;

	public String Pathid() {
		char Charswitch;
		char Charscale;
		// Get information from the Field Management System (FMS)
		FieldInfo m_teamInfo = new FieldInfo();
		m_teamLoc = m_teamInfo.getFieldInfo();
		Charswitch = m_teamLoc.charAt(1);
		Charscale = m_teamLoc.charAt(2);
		String Switch = String.valueOf(Charswitch); //convert Charswitch to Switch, because chars and strings dont like each other
		String Scale = String.valueOf(Charscale); //above but with Scale
			if (Switch == "L") { //check for switch being L
				if (Scale == "L") { //check scale being L
					Path = "11";
				}
				else if (Scale == "R") {//check scale being R
					Path = "14";
				}
			}
			else if (Switch == "R") {//check switch being R
				if (Scale == "L") {//check scale being L
					Path = "23";
				}
				else if (Scale == "R") {//check scale being R
					Path = "22";
				}
			}
		return Path;
	}

}