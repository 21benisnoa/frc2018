package org.usfirst.frc.team5903.robot;

import org.usfirst.frc.team5903.robot.FieldCalculations;
import edu.wpi.first.wpilibj.Timer;

public class Autonomouspath {

	String Pathid;
	private Timer m_timer = new Timer();
	private FieldCalculations m_FieldCalculations = new FieldCalculations(); //declares a Field Calculations object so the Pathid can be retrieved
	
	
	public LeftDrive() {//Declares the LeftDrive method
		Pathid = m_FieldCalculations.Path; // Retrieves the Path id from Field Calculations
		if (Pathid == "11") {//checks for pathid being 11
			m_timer.reset();
			m_timer.start();
		}
		else if (Pathid == "14") {//checks for pathid being 14
			m_timer.reset();
			m_timer.start();
		}
		else if (Pathid == "22") {//checks for pathid being 22
			m_timer.reset();
			m_timer.start();
		}
		else if (Pathid == "23") {//checks for Pathid being 23
			m_timer.reset();
			m_timer.start();
		}
		return;
	}
	public MiddleDrive() {//declares the MiddleDrive method
		return;	
	}
	public RightDrive() {//declares the Rightdrive method
		Pathid = m_FieldCalculations.Path; // Retrieves the Path id from Field Calculations
		if (Pathid == "11") {//checks for pathid being 11
			m_timer.reset();
			m_timer.start();
		}
		else if (Pathid == "14") {//checks for pathid being 14
			m_timer.reset();
			m_timer.start();
		}
		else if (Pathid == "22") {//checks for pathid being 22
			m_timer.reset();
			m_timer.start();
		}
		else if (Pathid == "23") {//checks for Pathid being 23
			m_timer.reset();
			m_timer.start();
		}
		return;
	}
}
