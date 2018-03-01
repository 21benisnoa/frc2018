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

	// For getting Dashboard autonomous choice
	private String m_autoSelected; // is named with m_ because just naming it mode gave an error in the switch
									// statement
	private SendableChooser<String> chooser = new SendableChooser<>();
	private static final String kDefault = "Default";
	private static final String kLeft = "Auto mode for left position";
	private static final String kMiddle = "Auto mode for middle position";
	private static final String kRight = "Auto mode for right position";

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
		chooser.addDefault("Default", "kDefault");
		chooser.addObject("Left", "kLeft");
		chooser.addObject("Middle", "kMiddle");
		chooser.addObject("Right", "kRight");
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
		m_autoSelected = chooser.getSelected();
		System.out.println("autonomous init: auto selected is: " + m_autoSelected);
		// Let's go!
		m_timer.reset();
		m_timer.start();
	}

	/**
	 * This function is called periodically during autonomous.
	 */
	@Override
	public void autonomousPeriodic() {
		// double Gx = m_gyro.getAngle(); // gyro x
		// System.out.println("Angle= " + Gx); // print gyro to console
		// double turningValue = (kAngleSetpoint - m_gyro.getAngle()) * kP;

		// BEGIN DRIVE CODE
		switch (m_autoSelected) {
		// 1 is Left
		case kLeft:
			System.out.println("Position 1 selected, we are left side.");
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
