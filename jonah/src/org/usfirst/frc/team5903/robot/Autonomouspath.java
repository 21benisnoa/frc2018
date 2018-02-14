package org.usfirst.frc.team5903.robot;

import org.usfirst.frc.team5903.robot.FieldCalculations;

public class Autonomouspath {

	String Pathid;
	private FieldCalculations m_FieldCalculations = new FieldCalculations(); //declares a Field Calculations object so the Pathid can be retrieved
	
	
	public LeftDrive() {//Declares the LeftDrive method
		Pathid = m_FieldCalculations.Path; // Retrieves the Path id from Field Calculations
		if (Pathid == "11") {//checks for pathid being 11
			
		}
		else if (Pathid == "14") {//checks for pathid being 14
			
		}
		else if (Pathid == "22") {//checks for pathid being 22
			
		}
		else if (Pathid == "23") {//checks for Pathid being 23
			
		}
		return;
	}
	public MiddleDrive() {//declares the MiddleDrive method
		return;	
	}
	public RightDrive() {//declares the Rightdrive method
		Pathid = m_FieldCalculations.Path; // Retrieves the Path id from Field Calculations
		if (Pathid == "11") {//checks for pathid being 11
			
		}
		else if (Pathid == "14") {//checks for pathid being 14
			
		}
		else if (Pathid == "22") {//checks for pathid being 22
			
		}
		else if (Pathid == "23") {//checks for Pathid being 23
			
		
		return;
	}
}
