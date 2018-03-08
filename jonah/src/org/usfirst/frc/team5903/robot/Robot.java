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
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.SPI;
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

	// gyro value of 360 is set to correspond to one full revolution
	private ADXRS450_Gyro m_gyro;
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
		UsbCamera cam1;
		UsbCamera cam2;
		m_gyro = new ADXRS450_Gyro(SPI.Port.kOnboardCS0);

		// Set dashboard options for autonomous mode.
		chooser.addDefault("Default", kDefault);
		chooser.addObject("Left", kLeft);
		chooser.addObject("Middle", kMiddle);
		chooser.addObject("Right", kRight);
		SmartDashboard.putData("Autonomous Selector", chooser);

		// Starts camera feeds
		cam1 = CameraServer.getInstance().startAutomaticCapture(0);
		cam2 = CameraServer.getInstance().startAutomaticCapture(1);

		// This seems to take about 5 seconds.
		// Do it here so we do not pause during competition.
		m_gyro.calibrate();
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
		// System.out.println(Pathid);
		// double Gx = m_gyro.getAngle(); // gyro x
		// System.out.println("Angle= " + Gx); // print gyro to console
		// double turningValue = (kAngleSetpoint - m_gyro.getAngle()) * kP;

		// BEGIN DRIVE CODE
		if (HasLoc == 1) {
			switch (m_autoSelected) {
			// 1 is Left
			case kLeft:
				// System.out.println("Position 1 selected, we are left side.");
				if (Pathid == "11") {
					// System.out.println("location 1, pathid 11");
					if (currentTimer > 0 &&
							currentTimer < 3.7) { // move forward and grip cube
						m_ControlMethods.Forwards(.75);
						m_ControlMethods.Closeclaw();
						m_ControlMethods.Raisearm();
					} else if (currentTimer > 3.7 &&
							currentTimer < 4.1) { // turn right past the switch
						m_ControlMethods.Stop();
						m_ControlMethods.Stoparm();
						m_ControlMethods.Right(.75);
					} else if (currentTimer > 4.1 &&
							currentTimer < 4.6) { // move forward
						m_ControlMethods.Forwards(.75);
					} else if (currentTimer > 4.6 &&
							currentTimer < 6.2) { // turn right
						m_ControlMethods.Right(.7);
					} else if (currentTimer > 6.2 &&
							currentTimer < 6.7) { // move forward to switch and raise arm
						m_ControlMethods.Forwards(.75);
						m_ControlMethods.Raisearm();
					} else if (currentTimer > 6.7 &&
							currentTimer < 7.0) { // stop robot at switch and open claw
						m_ControlMethods.Openclaw();
						m_ControlMethods.Stop();
						m_ControlMethods.Stoparm();
//					} else if (currentTimer > 7.0 &&
//							currentTimer < 7.4) { // stop claw pneumatics and put robot in reverse
//						m_ControlMethods.Stopclaw();
//						m_ControlMethods.Backwards(.75);
//					} else if (currentTimer > 7.4 &&
//							currentTimer < 7.6) { // stop robot
//						m_ControlMethods.Stop();
//						m_ControlMethods.Lowerarm();
//					} else if (currentTimer > 7.6 &&
//							currentTimer < 7.8) { // stop arm and grab cube
//						m_ControlMethods.Stoparm();
//						m_ControlMethods.Closeclaw();
//					} else if (currentTimer > 7.8 &&
//							currentTimer < 8.3) { // put robot in reverse and start raising arm
//						m_ControlMethods.Raisearm();
//						m_ControlMethods.Backwards(.75);
//					} else if (currentTimer > 8.3 &&
//							currentTimer < 8.6) { // stop robot and arm
//						m_ControlMethods.Stop();
//						m_ControlMethods.Stoparm();
//					} else if (currentTimer > 8.6 &&
//							currentTimer < 8.9) { // open claw
//						m_ControlMethods.Openclaw();
//					} else if (currentTimer > 8.9 &&
//							currentTimer < 9.5) { // stop claw
//						m_ControlMethods.Stopclaw();
//					}
				} // END PATHID 11 CODE
				else if (Pathid == "14") {
					// System.out.println("location 1, pathid 14");
					if (currentTimer > 0 &&
							currentTimer < 3.7) { // move forward and grip cube
						m_ControlMethods.Forwards(.75);
						m_ControlMethods.Closeclaw();
						m_ControlMethods.Raisearm();
					} else if (currentTimer > 3.7 &&
							currentTimer < 4.1) { // turn right past the switch
						m_ControlMethods.Stop();
						m_ControlMethods.Stoparm();
						m_ControlMethods.Right(.75);
					} else if (currentTimer > 4.1 &&
							currentTimer < 4.6) { // move forward
						m_ControlMethods.Forwards(.75);
					} else if (currentTimer > 6.2 &&
							currentTimer < 6.7) { // move forward to switch and raise arm
						m_ControlMethods.Forwards(.75);
						m_ControlMethods.Raisearm();
					} else if (currentTimer > 6.7 &&
							currentTimer < 7.0) { // stop robot at switch and open claw
						m_ControlMethods.Openclaw();
						m_ControlMethods.Stop();
						m_ControlMethods.Stoparm();
//					} else if (currentTimer > 7.5 && currentTimer < 8) {// stops claw pneumatics and puts robot in reverse
//							m_ControlMethods.Stopclaw();
//							m_ControlMethods.Backwards(.75);
//					} else if (currentTimer > 8 && currentTimer < 9) { // stops robot
//							m_ControlMethods.Stop();
//							m_ControlMethods.Lowerarm();
//					} else if (currentTimer > 9 && currentTimer < 9.3) { // stops arm and grabs cube
//							m_ControlMethods.Stoparm();
//							m_ControlMethods.Closeclaw();
//					} else if (currentTimer > 9.3 && currentTimer < 10) { // puts robot in reverse and starts raising arm
//							m_ControlMethods.Raisearm();
//							m_ControlMethods.Backwards(.75);
//					} else if (currentTimer > 10 && currentTimer < 10.5) { // turns robot left
//							m_ControlMethods.Left(.75);
//					} else if (currentTimer > 10.5 && currentTimer < 10.7) { // moves robot forward
//							m_ControlMethods.Forwards(.75);
//					} else if (currentTimer > 12 && currentTimer < 12.5) { // stops arm and turns robot right
//							m_ControlMethods.Stoparm();
//							m_ControlMethods.Right(.75);
//					} else if (currentTimer > 12.5 && currentTimer < 13.4) { // moves robot backwards
//							m_ControlMethods.Backwards(.75);
//					} else if (currentTimer > 13.4 && currentTimer < 13.6) { // opens claw
//							m_ControlMethods.Openclaw();
//					} else if (currentTimer > 13.6 && currentTimer < 13.8) { // stops claw
//							m_ControlMethods.Stopclaw();
//					}
				} // END PATHID 14 CODE
				else if (Pathid == "22") {
					// System.out.println("location 1, pathid 22");
					if (currentTimer < 4) { // moves robot forward and grips cube
						m_ControlMethods.Forwards(.75);
						m_ControlMethods.Closeclaw();
						m_ControlMethods.Raisearm();
					} else if (currentTimer > 4 && currentTimer < 4.3) { // turns robot right
							m_ControlMethods.Stop();
							m_ControlMethods.Stoparm();
					} else if (currentTimer > 4.3) {
						if (currentTimer < 8) { // moves robot forward
							m_ControlMethods.Forwards(.75);
						}
					} else if (currentTimer > 6) {
						if (currentTimer < 8.3) { // turns robot right
							m_ControlMethods.Right(1);
						}
					} else if (currentTimer > 8.3) {
						if (currentTimer < 9) { // moves robot forward to switch and raises arm
							m_ControlMethods.Forwards(1);
							m_ControlMethods.Raisearm();
						}
					} else if (currentTimer > 9) {
						if (currentTimer < 9.5) { // stops robot at switch and opens claw
							m_ControlMethods.Openclaw();
							m_ControlMethods.Stop();
							m_ControlMethods.Stoparm();
						}
					} else if (currentTimer > 9.5) { // stops claw pneumatics and puts robot in reverse
						if (currentTimer < 10) {
							m_ControlMethods.Stopclaw();
							m_ControlMethods.Backwards(-1);
						}
					} else if (currentTimer > 10) { // stops robot
						if (currentTimer < 11) {
							m_ControlMethods.Stop();
							m_ControlMethods.Lowerarm();
						}
					} else if (currentTimer > 11) { // stops arm and grabs cube
						if (currentTimer < 11.3) {
							m_ControlMethods.Stoparm();
							m_ControlMethods.Closeclaw();
						}
					} else if (currentTimer > 11.3) { // puts robot in reverse and starts raising arm
						if (currentTimer < 12) {
							m_ControlMethods.Raisearm();
							m_ControlMethods.Backwards(-1);
						}
					} else if (currentTimer > 14) { // stops robot and arm
						if (currentTimer < 14.2) {
							m_ControlMethods.Stop();
							m_ControlMethods.Stoparm();
						}
					} else if (currentTimer > 14.2) { // opens claw
						if (currentTimer < 14.4) {
							m_ControlMethods.Openclaw();
						}
					} else if (currentTimer > 14.4) { // stops claw
						if (currentTimer < 14.6) {
							m_ControlMethods.Stopclaw();
						}
					}
				} // END PATHID 22 CODE
				else if (Pathid == "23") {
					// System.out.println("location 1, pathid 23");
					if (currentTimer < 4) { // moves robot forward and grips cube
						m_ControlMethods.Forwards(1);
						m_ControlMethods.Closeclaw();
						m_ControlMethods.Raisearm();
					} else if (currentTimer > 4) { // turns robot right
						if (currentTimer < 4.3) {
							m_ControlMethods.Stop();
							m_ControlMethods.Stoparm();
						}
					} else if (currentTimer > 4.3) { // moves robot forward
						if (currentTimer < 8) {
							m_ControlMethods.Forwards(1);
						}
					} else if (currentTimer > 6) { // turns robot right
						if (currentTimer < 8.3) {
							m_ControlMethods.Right(1);
						}
					} else if (currentTimer > 8.3) { // moves robot forward to switch and raises arm
						if (currentTimer < 9) {
							m_ControlMethods.Forwards(1);
							m_ControlMethods.Raisearm();
						}
					} else if (currentTimer > 9) { // stops robot at switch and opens claw
						if (currentTimer < 9.5) {
							m_ControlMethods.Openclaw();
							m_ControlMethods.Stop();
							m_ControlMethods.Stoparm();
						}
					} else if (currentTimer > 9.5) { // stops claw pneumatics and puts robot in reverse
						if (currentTimer < 10) {
							m_ControlMethods.Stopclaw();
							m_ControlMethods.Backwards(-1);
						}
					} else if (currentTimer > 10) { // stops robot
						if (currentTimer < 11) {
							m_ControlMethods.Stop();
							m_ControlMethods.Lowerarm();
						}
					} else if (currentTimer > 11) { // stops arm and grabs cube
						if (currentTimer < 11.3) {
							m_ControlMethods.Stoparm();
							m_ControlMethods.Closeclaw();
						}
					} else if (currentTimer > 11.3) { // puts robot in reverse and starts raising arm
						if (currentTimer < 12) {
							m_ControlMethods.Raisearm();
							m_ControlMethods.Backwards(-1);
						}
					} else if (currentTimer > 12) { // turns robot left
						if (currentTimer < 12.5) {
							m_ControlMethods.Left(1);
						}
					} else if (currentTimer > 12.5) { // moves robot backwards
						if (currentTimer < 12.7) {
							m_ControlMethods.Backwards(-1);
						}
					} else if (currentTimer > 14) { // stops arm and turns robot right
						if (currentTimer < 14.5) {
							m_ControlMethods.Stoparm();
							m_ControlMethods.Right(1);
						}
					} else if (currentTimer > 16) { // stops robot and arm
						if (currentTimer < 16.2) {
							m_ControlMethods.Stop();
							m_ControlMethods.Stoparm();
						}
					} else if (currentTimer > 16.2) { // opens claw
						if (currentTimer < 16.4) {
							m_ControlMethods.Openclaw();
						}
					} else if (currentTimer > 16.4) { // stops claw
						if (currentTimer < 16.6) {
							m_ControlMethods.Stopclaw();
						}
					}
				} // END PATHID 23 CODE
					// End LEFT position code
				break;
			// 2 is Middle
			case kMiddle:
				System.out.println("Position 2 selected, we are in the middle.");
				break;
			// 3 is Right
			case kRight:
				System.out.println("Position 1 selected, we are right side.");
				break;
			case kDefault:
			default:
				System.out.println("you messed up somewhere.");
				break;
			}
		}
	}

	/**
	 * This function is called once each time the robot enters teleoperated mode.
	 */
	@Override

	public void teleopInit() {
		llTable = NetworkTableInstance.getDefault().getTable("limelight");
		tv = llTable.getEntry("tv"); // Whether the limelight has any valid targets (0 or 1)
		tx = llTable.getEntry("tx"); // Horizontal Offset From Crosshair To Target (-27 degrees to 27 degrees)
		ty = llTable.getEntry("ty"); // Vertical Offset From Crosshair To Target (-20.5 degrees to 20.5 degrees)
		ta = llTable.getEntry("ta"); // Target Area (0% of image to 100% of image)
		ts = llTable.getEntry("ts"); // Skew or rotation (-90 degrees to 0 degrees)

		// According to http://docs.limelightvision.io/en/latest/networktables_api.html
		// "camMode" set to 1 enables "Driver Camera (Increases exposure, disables
		// vision processing)"
		llTable.getEntry("camMode").setNumber(1);

		System.out.println("I'm in teleop");
	}

	/**
	 * This function is called periodically during teleoperated mode.
	 */
	@Override
	public void teleopPeriodic() {
		x = tx.getDouble(0);
		y = ty.getDouble(0);
		area = ta.getDouble(0);

		// Flash the LEDs if we have a target in our sight.
		if (llTable.getEntry("tv").getDouble(0) == 1) {
			// From http://docs.limelightvision.io/en/latest/networktables_api.html
			// "ledMode" 2 causes the LEDs to blink
			llTable.getEntry("ledMode").setNumber(2);
		} else {
			// "ledMode" 0 causes the LEDs to stay on steady
			llTable.getEntry("ledMode").setNumber(0);
		}

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
