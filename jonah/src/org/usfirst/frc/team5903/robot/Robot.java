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
	private Timer Timer = new Timer();
	private FieldCalculations m_FieldCalculations = new FieldCalculations(); // declares a Field Calculations object so
																				// the Pathid can be retrieved
	private ControlMethods Control = new ControlMethods(); // declares a ControlMethods object so methods can
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
	private String Herd;
	private SendableChooser<String> chooser = new SendableChooser<>();
	private static final String kDefault = "Default";
	private static final String kLeft = "Auto mode for left position";
	private static final String kMiddle = "Auto mode for middle position";
	private static final String kRight = "Auto mode for right position";
	private String Pathid;
	private SendableChooser<String> chooser2 = new SendableChooser<>();
	private static final String kYes = "Herd cubes.";
	private static final String kNo = "don't herd cubes.";

	/**
	 * This function is run when the robot is first started up and should be used
	 * for any initialization code.
	 */
	@SuppressWarnings("unused")
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
		SmartDashboard.putData("Herd cubes?", chooser2);
		chooser2.addObject("Yes", kYes);
		chooser2.addObject("No", kNo);
		chooser2.addDefault("Default", kDefault);
		
		// Starts camera feeds
		cam0 = CameraServer.getInstance().startAutomaticCapture(0);
		cam1 = CameraServer.getInstance().startAutomaticCapture(1);
	}

	/**
	 * This function is run once each time the robot enters autonomous mode.
	 */
	@SuppressWarnings("static-access")
	@Override
	public void autonomousInit() {
		// Get information from the Field Management System (FMS)

		m_teamLoc = m_teamInfo.getFieldInfo();
		// Pathid = m_FieldCalculations.Pathid(); // Retrieves the Path id from Field
		System.out.println("I'm in autonomous: Alliance color: " + m_teamLoc.charAt(0)
				+ " Plate Locations: Near switch: " + m_teamLoc.charAt(1) + " Scale: " + m_teamLoc.charAt(2)
				+ " Far switch: " + m_teamLoc.charAt(3));
		try {
			m_autoSelected = chooser.getSelected(); // Get our field position from the Shuffleboard Network Tables
		} catch (Exception e) {
			m_autoSelected = kDefault;
			System.out.println("Shuffleboard error, did you forget to start the shuffleboard?");
		}
		try {
			Herd = chooser2.getSelected(); //Get herding yes/no from shuffleboard
		} catch (Exception e) {
			Herd = kDefault;
			System.out.println("Shuffleboard error, did you forget to start the shuffleboard?");
		}
		System.out.println("autonomous init: auto selected is: " + m_autoSelected);
		HasLoc = 1; // sets HasLoc to one because we have retrieved the location from the
		// smartdashboard by now.
		Pathid = m_FieldCalculations.Pathid();
		// Let's go!
		Timer.reset();
		Timer.start();
		Control.Setsafety(false);

		switch (m_autoSelected) {
		case kLeft:
			System.out.println("Position 1 selected, we are left side.");
			if (Pathid == "11" || Pathid == "21") {
				System.out.println("Path ID 11 confirmed, the Switch and Scale are Left.");
				Control.Openclaw();
				Timer.delay(.2);
				Control.Closeclaw();
				Timer.delay(.2);
				Control.Raisearm();
				Control.Closeclaw();
				Timer.delay(.6); // Raises arm for a second
				Control.Forwards(.75);
				Timer.delay(1.9); // moves robot forwards until it drifts
				Control.Right(.6);
				Control.Stoparm(); 
				Timer.delay(0.4);// counters drift
				Control.Forwards(0.75);
				Timer.delay(1.7);// moves robot forwards again
//stop arm was here
				Timer.delay(0.4); // stops arm so it doesnt raise too high
				Control.Right(.7);
				Control.Stopclaw();
				Timer.delay(1);// turns robot right so we can put a cube on the scale
				Control.Forwards(.6);
				Timer.delay(0.3);// moves robot forwards so the cube is above the scale
				Control.Stop();
				Timer.delay(.3);// stops robot so we don't run into the scale
				Control.Openclaw();
				Timer.delay(0.1);// opens the claw to drop the cube
				Control.Backwards(.7);
				Timer.delay(.4);// moves robot backwards so we don't end up stuck on the scale
				Control.Stop();
			} // END PATHID 11 CODE
			else if (Pathid == "12" || Pathid == "22") {
				//System.out.println("Path ID 14 confirmed, the Switch is Left and the Scale is Right.");
				Control.Openclaw();
				Timer.delay(.2);
				Control.Closeclaw();
				Timer.delay(.2);
				Control.Raisearm();
				Control.Closeclaw();
				Timer.delay(.6); // Raises arm for a second
				Control.Forwards(.75);
				Timer.delay(1.9); // moves robot forwards until it drifts
				Control.Right(.6);
				Timer.delay(0.4);// counters drift
				Control.Forwards(0.75);
				Control.Stoparm();
				Timer.delay(1.7);// moves robot forwards again
				switch (Herd) {
				case kYes:
					System.out.println("Herd is yes");
					Control.Right(.7);
					Control.Stopclaw();
					Timer.delay(1.3);// turns the robot right so we can push cubes,also stops the arm so it doesn't
					// raise too high
					Control.Forwards(.6);
					Timer.delay(1.5);// moves the robot forwards so we can push cubes, until it drifts too far to the
					// left again.
					Control.Right(.7);
					Timer.delay(.1);// turns the robot right to counter drift again
					Control.Forwards(.6);
					Timer.delay(1.5);// moves the robot forwards once more and then brings it to a stop
					Control.Stop();
					break;
				case kNo:
					Control.Stopclaw();
					Control.Stop();
					System.out.println("Herd is no");
				default:
					Control.Stop();
					System.out.println("Herd protocol undefined, assuming no.");
					break;
				}
			} // END PATHID 14
			// End LEFT position code
			break;
		case kMiddle:
			System.out.println("Position 2 selected, we are in the middle.");
			if (Pathid == "11" || Pathid == "12") {
				System.out.println("The Switch is left, performing left path code");
				Control.Raisearm();
				Control.Forwards(.6);
				Control.Closeclaw();
				System.out.println("Moving forwards...");
				Timer.delay(0.6);
				Control.Left(.6);
				Control.Stopclaw();
				System.out.println("Turning Left...");
				Timer.delay(0.6);
				Control.Forwards(.7);
				System.out.println("Moving forwards...");
				Timer.delay(1);
				Control.Stoparm();
				Timer.delay(0.4);
				Control.Right(.7);
				System.out.println("turning right...");
				Timer.delay(0.6);
				Control.Forwards(.6);
				System.out.println("Moving forwards to Switch...");
				Timer.delay(0.3);
				Control.Openclaw();
				System.out.println("Dropping cube");
				Timer.delay(.1);
				Control.Stopclaw();
				Control.Stop();
				System.out.println("Path Complete.");
			} else if (Pathid == "22" || Pathid == "21") {
				System.out.println("The Switch is right, performing right path code");
				Control.Forwards(.6);
				Control.Closeclaw();
				Control.Raisearm();
				System.out.println("Moving forwards...");
				Timer.delay(0.6);
				Control.Right(.6);
				Control.Stopclaw();
				System.out.println("Turning right...");
				Timer.delay(0.6);
				Control.Forwards(.7);
				System.out.println("Moving forwards...");
				Timer.delay(1);
				Control.Stoparm();
				Timer.delay(0.3);
				Control.Left(0.7);
				System.out.println("Turning left...");
				Timer.delay(0.6);
				Control.Forwards(.6);
				System.out.println("Moving forwards to Switch.");
				Timer.delay(0.3);
				Control.Openclaw();
				System.out.println("Dropping cube.");
				Timer.delay(0.1);
				Control.Stopclaw();
				Control.Stop();
				System.out.println("Path Complete.");
			}
			break;

		/*
		 * This is the code we used at Duluth as a simple "catchall" to move past the
		 * 10-foot line.
		 *
		 * case kMiddle:
		 * System.out.println("Position 2 selected, we are in the middle."); if
		 * (current_timer > 0 && current_timer < 3) { Control.Forwards(.5); } else {
		 * Control.Stop(); } break;
		 */
		case kRight:
			System.out.println("Position 3 selected, we are right side.");
			if (Pathid == "11" || Pathid == "21") {
				Control.Openclaw();
				Timer.delay(.2);
				Control.Closeclaw();
				Timer.delay(.2);
				Control.Raisearm();
				Control.Closeclaw();
				Timer.delay(.6); // Raises arm for a second
				Control.Forwards(.75);
				Timer.delay(1.9); // moves robot forwards until it drifts
				Control.Right(.6);
				Timer.delay(0.4);// counters drift
				Control.Forwards(0.75);
				Control.Stoparm();
				Timer.delay(1.7);// moves robot forwards again
				Control.Stopclaw();
				System.out.println("stopping arm.");
				switch (Herd) {
				case kYes:
					System.out.println("Herd is yes");
					Control.Left(.7);
					System.out.println("Turning left to face cubes.");
					Timer.delay(1.3);
					Control.Forwards(.6);
					System.out.println("Herding cubes...");
					Timer.delay(1.5);
					Control.Right(.7);
					System.out.println("Countering drift...");
					Timer.delay(.1);
					Control.Forwards(.6);
					System.out.println("Continuing to herd cubes...");
					Timer.delay(1.5);
					Control.Stop();
					System.out.println("Cubes herded, path complete.");
					break;
				case kNo:
					Control.Stop();
					System.out.println("Herd is no, path complete.");
					break;
				default:
					Control.Stop();
					System.out.println("Herd protocol undefined, assuming no.");
					System.out.println("Path complete.");
					break;
				}
			} // END PATHID 11 CODE
			else if (Pathid == "12" || Pathid == "22") {
				System.out.println("The Scale is right, putting cube on Scale.");
				Control.Openclaw();
				Timer.delay(.2);
				Control.Closeclaw();
				Timer.delay(.2);
				Control.Raisearm();
				Control.Closeclaw();
				Timer.delay(.6); // Raises arm for a second
				Control.Forwards(.75);
				Timer.delay(1.9); // moves robot forwards until it drifts
				Control.Right(.6);
				Control.Stoparm();
				Timer.delay(0.4);// counters drift
				Control.Forwards(0.75);
				Timer.delay(1.7);// moves robot forwards again
				//StopArm was here
				Timer.delay(0.4); // stops arm so it doesnt raise too high
				Control.Left(.7);
				Control.Stopclaw();
				Timer.delay(1);// turns robot right so we can put a cube on the scale
				Control.Forwards(.6);
				Timer.delay(0.3);// moves robot forwards so the cube is above the scale
				Control.Stop();
				Timer.delay(.3);// stops robot so we don't run into the scale
				Control.Openclaw();
				Timer.delay(0.1);// opens the claw to drop the cube
				Control.Backwards(.7);
				Timer.delay(.4);// moves robot backwards so we don't end up stuck on the scale
				Control.Stop();
			} // END PATHID 14
				// End RIGHT position code
			break;
		case kDefault:
		default:
			System.out.println("You messed up, Bad. Going to backup path.");
			Control.Raisearm();
			Timer.delay(.6);
			Control.Forwards(.7);
			Control.Closeclaw();
			Timer.delay(2);
			Control.Stop();
			Control.Stoparm();
			Control.Stopclaw();
			break;
		}
	}

	/**
	 * This function is called periodically during autonomous.
	 */
	@Override
	public void autonomousPeriodic() {
		double currentTimer = Timer.get();
		// BEGIN DRIVE CODE
		if (HasLoc == 1) {

		}
	}

	/**
	 * This function is called once each time the robot enters teleoperated mode.
	 */
	@Override
	public void teleopInit() {
		System.out.println("I'm in teleop");
		Control.Setsafety(false);
	}

	/**
	 * This function is called periodically during teleoperated mode.
	 */
	@Override
	public void teleopPeriodic() {
		Control.Drivecontrol();
		Control.Triggercontrol();
		Control.Clawcontrol();
		Control.Climbcontrol();
		Control.Punch();
		
	}

	@Override
	public void testInit() {
		Timer.reset();
		Timer.start();
		Timer.delay(2.0);
		Control.Right(.7);
		Timer.delay(0.5);
		Control.Stop();
	}

	/**
	 * This function is called periodically during test mode.
	 */
	@Override
	public void testPeriodic() {
		double currentTimer = Timer.get();
	}
}
