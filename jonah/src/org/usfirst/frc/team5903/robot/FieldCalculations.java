package org.usfirst.frc.team5903.robot;

import org.usfirst.frc.team5903.robot.FieldInfo;

public class FieldCalculations {
	int Robotposition;
	String Alliancecolours;
	String Follow;
	String m_teamLoc;
	String Path;
	
	public String Getcolours() {
		// Get information from the Field Management System (FMS)
		FieldInfo m_teamInfo = new FieldInfo();
		m_teamLoc = m_teamInfo.getFieldInfo();
		return m_teamLoc;
	}
	public String Leftactions() {
		String leftActions;
//		if (m_teamLoc == RLLL) or if (m_teamLoc == BLLL) {
//		}
		return leftActions;
	}
}
