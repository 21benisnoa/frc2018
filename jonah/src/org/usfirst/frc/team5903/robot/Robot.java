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
import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.Timer;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot {
	private Preferences m_prefs; // dklann: added for SmartDashboard functionality
	private Timer m_timer = new Timer();
	private static final double kAngleSetpoint = 0.0;
	private static final double kP = 0.005; // proportional turning constant
	private String Location;
	private FieldCalculations m_FieldCalculations = new FieldCalculations(); // declares a Field Calculations object so
	// the Pathid can be retrieved
	private ControlMethods m_ControlMethods = new ControlMethods(); // declares a ControlMethods object so methods can
	// be called
	FieldInfo m_teamInfo = new FieldInfo();

	// gyro calibration constant, may need to be adjusted;
	// gyro value of 360 is set to correspond to one full revolution
	private ADXRS450_Gyro m_gyro = new ADXRS450_Gyro(SPI.Port.kOnboardCS0);
	String m_teamLoc;
	NetworkTable table = NetworkTableInstance.getDefault().getTable("limelight");
	NetworkTableEntry tx = table.getEntry("tx");
	NetworkTableEntry ty = table.getEntry("ty");
	NetworkTableEntry ta = table.getEntry("ta");
	NetworkTableEntry ts = table.getEntry("ts");
	double x = tx.getDouble(0);
	double y = ty.getDouble(0);
	double area = ta.getDouble(0);

	/**
	 * This function is run when the robot is first started up and should be used
	 * for any initialization code.
	 */
	@Override
	public void robotInit() {
		UsbCamera cam1;
		UsbCamera cam2;
		// Starts camera feeds
		cam1 = CameraServer.getInstance().startAutomaticCapture(0);
		cam2 = CameraServer.getInstance().startAutomaticCapture(1);
		m_gyro.calibrate();
	}

	/**
	 * This function is run once each time the robot enters autonomous mode.
	 */
	@Override

	public void autonomousInit() {
		m_timer.reset();
		m_timer.start();
		// Get information from the Field Management System (FMS)

		m_teamLoc = m_teamInfo.getFieldInfo();
		// Pathid = m_FieldCalculations.Pathid(); // Retrieves the Path id from Field
		System.out.println("I'm in autonomous: Alliance color: " + m_teamLoc.charAt(0)
				+ " Plate Locations: Near switch: " + m_teamLoc.charAt(1) + " Scale: " + m_teamLoc.charAt(2)
				+ " Far switch: " + m_teamLoc.charAt(3));

		// will be one of: "Left" "Middle" "Right" or null
		// String StartingPosition = (String) positionChooser.getSelected();
		// TODO: Location = StartingPosition;
		Location = "Left";
	}

	/**
	 * This function is called periodically during autonomous.
	 */
	@Override
	public void autonomousPeriodic() {
		// double Gx = m_gyro.getAngle(); // gyro x
		// System.out.println("Angle= " + Gx); // print gyro to console
		// double turningValue = (kAngleSetpoint - m_gyro.getAngle()) * kP;
		if (m_timer.get() < 6.0) {
			m_ControlMethods.Forwards(.5);
		} else if (m_timer.get() >= 6.0) {
			m_ControlMethods.Stop();
		}
		// BEGIN DRIVE CODE
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
		NetworkTable table = NetworkTableInstance.getDefault().getTable("limelight");

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
