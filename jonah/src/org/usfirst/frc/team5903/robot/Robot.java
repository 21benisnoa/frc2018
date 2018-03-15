/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.usfirst.frc.team5903.robot;

import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot {
	private Timer m_timer = new Timer();
	private FieldCalculations m_FieldCalculations = new FieldCalculations(); // declares a Field Calculations object so
																				// the Pathid can be retrieved
	private ControlMethods m_ControlMethods = new ControlMethods(); // declares a ControlMethods object so methods can
																	// be called
	FieldInfo m_teamInfo = new FieldInfo();
	private String m_teamLoc;
	// LimeLight camera access and manipulation: these will be retrieved in
	// teleopInit() and used in teleopPeriodic().
	private NetworkTable llTable;
	private NetworkTableEntry tv;
	private NetworkTableEntry tx;
	private NetworkTableEntry ty;
	private NetworkTableEntry ta;
	private NetworkTableEntry ts;
	private double x;
	private double y;
	private double area;
	private int HasLoc = 0;// This variable is here to be set to 1 when the location is retrieved from the
							// smartdashboard, so autonomous doesn't run the wrong location code for a
							// second.
	// For getting Dashboard autonomous choice
	private String m_autoSelected; // formerly the mode variable
	private SendableChooser<String> chooser = new SendableChooser<>();
	private static final String kDefault = "Default";
	private static final String kLeft = "Auto mode for left position";
	private static final String kMiddle = "Auto mode for middle position";
	private static final String kRight = "Auto mode for right position";
	private String Pathid;
	private double lastTimerValue;

	/**
	 * This function is run when the robot is first started up and should be used
	 * for any initialization code.
	 */
	@Override
	public void robotInit() {
		UsbCamera cam0;
		UsbCamera cam1;
		// Set dashboard options for autonomous mode.
		chooser.addDefault("Default", kDefault);
		chooser.addObject("Left", kLeft);
		chooser.addObject("Middle", kMiddle);
		chooser.addObject("Right", kRight);
		SmartDashboard.putData("Autonomous Selector", chooser);

		// Starts camera feeds
		cam0 = CameraServer.getInstance().startAutomaticCapture(0);
		cam1 = CameraServer.getInstance().startAutomaticCapture(1);
	}

	/**
	 * This function is run once each time the robot enters autonomous mode.
	 */
	@Override
	public void autonomousInit() {
		// Get information from the Field Management System (FMS)

		m_teamLoc = m_teamInfo.getFieldInfo();
		// Pathid = m_FieldCalculations.Pathid(); // Retrieves the Path id from Field
		System.out.println("I'm in autonomous: Alliance color: " + m_teamLoc.charAt(0)
				+ " Plate Locations: Near switch: " + m_teamLoc.charAt(1) + " Scale: " + m_teamLoc.charAt(2)
				+ " Far switch: " + m_teamLoc.charAt(3));
		m_autoSelected = chooser.getSelected(); // Get our field position from the Shuffleboard Network Tables
		System.out.println("autonomous init: auto selected is: " + m_autoSelected);
		HasLoc = 1; // sets HasLoc to one because we have retrieved the location from the
		// smartdashboard by now.
		Pathid = m_FieldCalculations.Pathid();
		// Let's go!
		m_timer.reset();
		m_timer.start();
	}

	/**
	 * This function is called periodically during autonomous.
	 */
	@Override
	public void autonomousPeriodic() {
		double currentTimer = m_timer.get();
		// BEGIN DRIVE CODE
		if (HasLoc == 1) {
			switch (m_autoSelected) {
			case kLeft:
				// System.out.println("Position 1 selected, we are left side.");
				if (Pathid == "11") {
					// System.out.println("location 1, pathid 11");
					if (currentTimer > 0 && currentTimer < 3.7) { // move forward and grip cube
						m_ControlMethods.Forwards(.75);
						m_ControlMethods.Closeclaw();
						m_ControlMethods.Raisearm();
					} else if (currentTimer > 3.7 && currentTimer < 4.1) { // turn right past the switch
						m_ControlMethods.Stop();
						m_ControlMethods.Stoparm();
						m_ControlMethods.Right(.75);
					} else if (currentTimer > 4.1 && currentTimer < 4.6) { // move forward
						m_ControlMethods.Forwards(.75);
					} else if (currentTimer > 4.6 && currentTimer < 6.2) { // turn right
						m_ControlMethods.Right(.7);
					} else if (currentTimer > 6.2 && currentTimer < 6.7) { // move forward to switch and raise arm
						m_ControlMethods.Forwards(.75);
						m_ControlMethods.Raisearm();
					} else if (currentTimer > 6.7 && currentTimer < 7.0) { // stop robot at switch and open claw
						m_ControlMethods.Openclaw();
						m_ControlMethods.Stop();
						m_ControlMethods.Stoparm();
						// } else if (currentTimer > 7.0 &&
						// currentTimer < 7.4) { // stop claw pneumatics and put robot in reverse
						// m_ControlMethods.Stopclaw();
						// m_ControlMethods.Backwards(.75);
						// } else if (currentTimer > 7.4 &&
						// currentTimer < 7.6) { // stop robot
						// m_ControlMethods.Stop();
						// m_ControlMethods.Lowerarm();
						// } else if (currentTimer > 7.6 &&
						// currentTimer < 7.8) { // stop arm and grab cube
						// m_ControlMethods.Stoparm();
						// m_ControlMethods.Closeclaw();
						// } else if (currentTimer > 7.8 &&
						// currentTimer < 8.3) { // put robot in reverse and start raising arm
						// m_ControlMethods.Raisearm();
						// m_ControlMethods.Backwards(.75);
						// } else if (currentTimer > 8.3 &&
						// currentTimer < 8.6) { // stop robot and arm
						// m_ControlMethods.Stop();
						// m_ControlMethods.Stoparm();
						// } else if (currentTimer > 8.6 &&
						// currentTimer < 8.9) { // open claw
						// m_ControlMethods.Openclaw();
						// } else if (currentTimer > 8.9 &&
						// currentTimer < 9.5) { // stop claw
						// m_ControlMethods.Stopclaw();
					}
				} // END PATHID 11 CODE
				else if (Pathid == "14") {
					// System.out.println("location 1, pathid 14");
					if (currentTimer > 0 && currentTimer < 3.7) { // move forward and grip cube
						m_ControlMethods.Forwards(.75);
						m_ControlMethods.Closeclaw();
						m_ControlMethods.Raisearm();
					} else if (currentTimer > 3.7 && currentTimer < 4.1) { // turn right past the switch
						m_ControlMethods.Stop();
						m_ControlMethods.Stoparm();
						m_ControlMethods.Right(.75);
					} else if (currentTimer > 4.1 && currentTimer < 4.6) { // move forward
						m_ControlMethods.Forwards(.75);
					} else if (currentTimer > 4.6 && currentTimer < 6.2) { // turn right
						m_ControlMethods.Right(.7);
					} else if (currentTimer > 6.2 && currentTimer < 6.7) { // move forward to switch and raise arm
						m_ControlMethods.Forwards(.75);
						m_ControlMethods.Raisearm();
					} else if (currentTimer > 6.7 && currentTimer < 7.0) { // stop robot at switch and open claw
						m_ControlMethods.Openclaw();
						m_ControlMethods.Stop();
						m_ControlMethods.Stoparm();
						// } else if (currentTimer > 7.5 && currentTimer < 8) {// stops claw pneumatics
						// and puts robot in reverse
						// m_ControlMethods.Stopclaw();
						// m_ControlMethods.Backwards(.75);
						// } else if (currentTimer > 8 && currentTimer < 9) { // stops robot
						// m_ControlMethods.Stop();
						// m_ControlMethods.Lowerarm();
						// } else if (currentTimer > 9 && currentTimer < 9.3) { // stops arm and grabs
						// cube
						// m_ControlMethods.Stoparm();
						// m_ControlMethods.Closeclaw();
						// } else if (currentTimer > 9.3 && currentTimer < 10) { // puts robot in
						// reverse and starts raising arm
						// m_ControlMethods.Raisearm();
						// m_ControlMethods.Backwards(.75);
						// } else if (currentTimer > 10 && currentTimer < 10.5) { // turns robot left
						// m_ControlMethods.Left(.75);
						// } else if (currentTimer > 10.5 && currentTimer < 10.7) { // moves robot
						// forward
						// m_ControlMethods.Forwards(.75);
						// } else if (currentTimer > 12 && currentTimer < 12.5) { // stops arm and turns
						// robot right
						// m_ControlMethods.Stoparm();
						// m_ControlMethods.Right(.75);
						// } else if (currentTimer > 12.5 && currentTimer < 13.4) { // moves robot
						// backwards
						// m_ControlMethods.Backwards(.75);
						// } else if (currentTimer > 13.4 && currentTimer < 13.6) { // opens claw
						// m_ControlMethods.Openclaw();
						// } else if (currentTimer > 13.6 && currentTimer < 13.8) { // stops claw
						// m_ControlMethods.Stopclaw();
					}
				} // END PATHID 14 CODE
				else if (Pathid == "22") {
					// System.out.println("location 1, pathid 22");
					if (currentTimer > 0 && currentTimer < 3.7) { // move forward and grip cube
						m_ControlMethods.Forwards(.75);
						m_ControlMethods.Closeclaw();
						m_ControlMethods.Raisearm();
					} else if (currentTimer > 3.7 && currentTimer < 4.1) { // turn right past the switch
						m_ControlMethods.Stop();
						m_ControlMethods.Stoparm();
						m_ControlMethods.Right(.75);
					} else if (currentTimer > 4.1 && currentTimer < 4.6) { // move forward
						m_ControlMethods.Forwards(.75);
					} else if (currentTimer > 6.2 && currentTimer < 6.7) { // move forward to switch and raise arm
						m_ControlMethods.Forwards(.75);
						m_ControlMethods.Raisearm();
					} else if (currentTimer > 6.7 && currentTimer < 7.0) { // stop robot at switch and open claw
						m_ControlMethods.Openclaw();
						m_ControlMethods.Stop();
						m_ControlMethods.Stoparm();
						// } else if (currentTimer > 9.5 && currentTimer < 10) { // stops claw
						// pneumatics and puts robot in reverse
						// m_ControlMethods.Stopclaw();
						// m_ControlMethods.Backwards(.75);
						// } else if (currentTimer > 10 && currentTimer < 11) { // stops robot
						// m_ControlMethods.Stop();
						// m_ControlMethods.Lowerarm();
						// } else if (currentTimer > 11 && currentTimer < 11.3) { // stops arm and grabs
						// cube
						// m_ControlMethods.Stoparm();
						// m_ControlMethods.Closeclaw();
						// } else if (currentTimer > 11.3 && currentTimer < 12) { // puts robot in
						// reverse and starts raising arm
						// m_ControlMethods.Raisearm();
						// m_ControlMethods.Backwards(-1);
						// } else if (currentTimer > 14 && currentTimer < 14.2) { // stops robot and arm
						// m_ControlMethods.Stop();
						// m_ControlMethods.Stoparm();
						// } else if (currentTimer > 14.2 && currentTimer < 14.4) { // opens claw
						// m_ControlMethods.Openclaw();
						// } else if (currentTimer > 14.4 && currentTimer < 14.6) {// stops claw
						// m_ControlMethods.Stopclaw();
					}
				} // END PATHID 22 CODE
				else if (Pathid == "23") {
					// System.out.println("location 1, pathid 23");
					if (currentTimer > 0 && currentTimer < 3.7) { // move forward and grip cube
						m_ControlMethods.Forwards(.75);
						m_ControlMethods.Closeclaw();
						m_ControlMethods.Raisearm();
					} else if (currentTimer > 3.7 && currentTimer < 4.1) { // turn right past the switch
						m_ControlMethods.Stop();
						m_ControlMethods.Stoparm();
						m_ControlMethods.Right(.75);
					} else if (currentTimer > 4.1 && currentTimer < 4.6) { // move forward
						m_ControlMethods.Forwards(.75);
					} else if (currentTimer > 4.6 && currentTimer < 6.2) { // turn right
						m_ControlMethods.Right(.7);
					} else if (currentTimer > 6.2 && currentTimer < 6.7) { // move forward to switch and raise arm
						m_ControlMethods.Forwards(.75);
						m_ControlMethods.Raisearm();
					} else if (currentTimer > 6.7 && currentTimer < 7.0) { // stop robot at switch and open claw
						m_ControlMethods.Openclaw();
						m_ControlMethods.Stop();
						m_ControlMethods.Stoparm();
						// } else if (currentTimer > 9.5 && currentTimer < 10) { // stops claw
						// pneumatics and puts robot in reverse
						// m_ControlMethods.Stopclaw();
						// m_ControlMethods.Backwards(.75);
						// } else if (currentTimer > 10 && currentTimer < 11) { // stops robot
						// m_ControlMethods.Stop();
						// m_ControlMethods.Lowerarm();
						// } else if (currentTimer > 11 && currentTimer < 11.3) { // stops arm and grabs
						// cube
						// m_ControlMethods.Stoparm();
						// m_ControlMethods.Closeclaw();
						// } else if (currentTimer > 11.3 && currentTimer < 12) { // puts robot in
						// reverse and starts raising arm
						// m_ControlMethods.Raisearm();
						// m_ControlMethods.Backwards(.75);
						// } else if (currentTimer > 12 && currentTimer < 12.5) { // turns robot left
						// m_ControlMethods.Left(.75);
						// } else if (currentTimer > 12.5 && currentTimer < 12.7) {// moves robot
						// backwards
						// m_ControlMethods.Backwards(.75);
						// } else if (currentTimer > 14 && currentTimer < 14.5) { // stops arm and turns
						// robot right
						// m_ControlMethods.Stoparm();
						// m_ControlMethods.Right(.75);
						// } else if (currentTimer > 16 && currentTimer < 16.2) { // stops robot and arm
						// m_ControlMethods.Stop();
						// m_ControlMethods.Stoparm();
						// } else if (currentTimer > 16.2 && currentTimer < 16.4) { // opens claw
						// m_ControlMethods.Openclaw();
						// } else if (currentTimer > 16.4 && currentTimer < 16.6) { // stops claw
						// m_ControlMethods.Stopclaw();
					}
				} // END PATHID 23 CODE
					// End LEFT position code
				break;
			case kMiddle:
				System.out.println("Position 2 selected, we are in the middle.");
				if (Pathid == "11" || Pathid == "14") {
					if (currentTimer > 0 && currentTimer < 0.7) {
						m_ControlMethods.Forwards(.6);
						m_ControlMethods.Closeclaw();
						m_ControlMethods.Raisearm();
					} else if (currentTimer > 0.7 && currentTimer < 1.3) {
						m_ControlMethods.Left(.6);
						m_ControlMethods.Stopclaw();
					} else if (currentTimer > 1.3 && currentTimer < 2.7) {
						m_ControlMethods.Forwards(0.7);
						m_ControlMethods.Stoparm();
					} else if (currentTimer > 2.7 && currentTimer < 3.2) {
						m_ControlMethods.Right(0.7);
					} else if (currentTimer > 3.2 && currentTimer < 3.5) {
						m_ControlMethods.Forwards(.5);
					} else if (currentTimer > 3.5 && currentTimer < 3.6) {
						m_ControlMethods.Openclaw();
					} else if (currentTimer > 3.6 && currentTimer < 3.7) {
						m_ControlMethods.Stopclaw();
					}
				} else if (Pathid == "22" || Pathid == "23") {
					System.out.println("Right Switch");
					if (currentTimer > 0 && currentTimer < 0.7) {
						m_ControlMethods.Forwards(.6);
						m_ControlMethods.Closeclaw();
						m_ControlMethods.Raisearm();
					} else if (currentTimer > 0.7 && currentTimer < 1.3) {
						m_ControlMethods.Right(.6);
						m_ControlMethods.Stopclaw();
					} else if (currentTimer > 1.3 && currentTimer < 2.7) {
						m_ControlMethods.Forwards(0.7);
						m_ControlMethods.Stoparm();
					} else if (currentTimer > 2.7 && currentTimer < 3.2) {
						m_ControlMethods.Left(0.7);
					} else if (currentTimer > 3.2 && currentTimer < 3.5) {
						m_ControlMethods.Forwards(.5);
					} else if (currentTimer > 3.5 && currentTimer < 3.6) {
						m_ControlMethods.Openclaw();
					} else if (currentTimer > 3.6 && currentTimer < 3.7) {
						m_ControlMethods.Stopclaw();
					}
				}
				break;

			/*
			 * This is the code we used at Duluth as a simple "catchall" to move past the
			 * 10-foot line.
			 *
			 * case kMiddle:
			 * System.out.println("Position 2 selected, we are in the middle."); if
			 * (current_timer > 0 && current_timer < 3) { m_ControlMethods.Forwards(.5); }
			 * else { m_ControlMethods.Stop(); } break;
			 */
			case kRight:
				System.out.println("Position 3 selected, we are right side.");
				if (Pathid == "11") {
					if (currentTimer > 0 && currentTimer < 3.7) { // move forward and grip cube
						m_ControlMethods.Forwards(.75);
						m_ControlMethods.Closeclaw();
						m_ControlMethods.Raisearm();
					} else if (currentTimer > 3.7 && currentTimer < 4.1) { // turn left past the switch
						m_ControlMethods.Stop();
						m_ControlMethods.Stoparm();
						m_ControlMethods.Left(.75);
					} else if (currentTimer > 4.1 && currentTimer < 5.6) { // move forward
						m_ControlMethods.Forwards(.75);
					} else if (currentTimer > 5.6 && currentTimer < 7.2) { // turn left
						m_ControlMethods.Left(.75);
					} else if (currentTimer > 7.2 && currentTimer < 7.7) { // move forward to switch and raise arm
						m_ControlMethods.Forwards(.75);
						m_ControlMethods.Raisearm();
					} else if (currentTimer > 7.7 && currentTimer < 8.0) { // stop robot at switch and open claw
						m_ControlMethods.Openclaw();
						m_ControlMethods.Stop();
						m_ControlMethods.Stoparm();
					}
				} // END PATHID 11 CODE
				else if (Pathid == "14") {
					if (currentTimer > 0 && currentTimer < 3.7) { // move forward and grip cube
						m_ControlMethods.Forwards(.75);
						m_ControlMethods.Closeclaw();
						m_ControlMethods.Raisearm();
					} else if (currentTimer > 3.7 && currentTimer < 4.1) { // turn left past the switch
						m_ControlMethods.Stop();
						m_ControlMethods.Stoparm();
						m_ControlMethods.Left(.75);
					} else if (currentTimer > 4.1 && currentTimer < 5.6) { // move forward
						m_ControlMethods.Forwards(.75);
					} else if (currentTimer > 5.6 && currentTimer < 7.2) { // turn left
						m_ControlMethods.Left(.75);
					} else if (currentTimer > 7.2 && currentTimer < 7.7) { // move forward to switch and raise arm
						m_ControlMethods.Forwards(.75);
						m_ControlMethods.Raisearm();
					} else if (currentTimer > 7.7 && currentTimer < 8.0) { // stop robot at switch and open claw
						m_ControlMethods.Openclaw();
						m_ControlMethods.Stop();
						m_ControlMethods.Stoparm();
					}
				} // END PATHID 14
				else if (Pathid == "22") {
					if (currentTimer > 0 && currentTimer < 3.7) { // move forward and grip cube
						m_ControlMethods.Forwards(.75);
						m_ControlMethods.Closeclaw();
						m_ControlMethods.Raisearm();
					} else if (currentTimer > 3.7 && currentTimer < 4.1) { // turn left past the switch
						m_ControlMethods.Stop();
						m_ControlMethods.Stoparm();
						m_ControlMethods.Left(.75);
					} else if (currentTimer > 4.1 && currentTimer < 5.6) { // move forward
						m_ControlMethods.Forwards(.75);
					} else if (currentTimer > 6.2 && currentTimer < 7.2) { // turn left
						m_ControlMethods.Left(.75);
					} else if (currentTimer > 7.2 && currentTimer < 7.7) { // move forward to switch and raise arm
						m_ControlMethods.Forwards(.75);
						m_ControlMethods.Raisearm();
					} else if (currentTimer > 7.7 && currentTimer < 8.0) { // stop robot at switch and open claw
						m_ControlMethods.Openclaw();
						m_ControlMethods.Stop();
						m_ControlMethods.Stoparm();
					}
				} // END PATHID 22 CODE
				else if (Pathid == "23") {
					if (currentTimer > 0 && currentTimer < 3.7) { // move forward and grip cube
						m_ControlMethods.Forwards(.75);
						m_ControlMethods.Closeclaw();
						m_ControlMethods.Raisearm();
					} else if (currentTimer > 3.7 && currentTimer < 4.1) { // turn left past the switch
						m_ControlMethods.Stop();
						m_ControlMethods.Stoparm();
						m_ControlMethods.Right(.75);
					} else if (currentTimer > 4.1 && currentTimer < 4.6) { // move forward
						m_ControlMethods.Forwards(.75);
					} else if (currentTimer > 4.6 && currentTimer < 6.2) { // turn left
						m_ControlMethods.Right(.7);
					} else if (currentTimer > 6.2 && currentTimer < 6.7) { // move forward to switch and raise arm
						m_ControlMethods.Forwards(.75);
						m_ControlMethods.Raisearm();
					} else if (currentTimer > 6.7 && currentTimer < 7.0) { // stop robot at switch and open claw
						m_ControlMethods.Openclaw();
						m_ControlMethods.Stop();
						m_ControlMethods.Stoparm();
					}
				} // END PATHID 23 CODE
					// End RIGHT position code
				break;
			/*
			 * This is the code we used at Duluth as a simple "catchall" to move past the
			 * 10-foot line.
			 *
			 * case kRight: System.out.println("Position 1 selected, we are right side.");
			 * if (current_timer > 0 && current_timer < 3) { m_ControlMethods.Forwards(.5);
			 * } else { m_ControlMethods.Stop(); } break;
			 */
			case kDefault:
			default:
				System.out.println("You messed up. Bad.");
				break;
			}
		}
	}

	/**
	 * This function is called once each time the robot enters teleoperated mode.
	 */
	@Override
	public void teleopInit() {
		System.out.println("I'm in teleop");
	}

	/**
	 * This function is called periodically during teleoperated mode.
	 */
	@Override
	public void teleopPeriodic() {
		m_ControlMethods.Joystickcontrol();
		m_ControlMethods.Triggercontrol();
		m_ControlMethods.Clawcontrol();
		m_ControlMethods.Climbcontrol();
	}

	/**
	 * This function is called periodically during test mode.
	 */
	@Override
	public void testPeriodic() {
	}
}
