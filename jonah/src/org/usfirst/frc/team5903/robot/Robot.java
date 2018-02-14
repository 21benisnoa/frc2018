/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.usfirst.frc.team5903.robot;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Spark;
//import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.networktables.NetworkTable;
import org.usfirst.frc.team5903.robot.FieldInfo;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot {
	private DifferentialDrive m_robotDrive
	= new DifferentialDrive(new Spark(0), new Spark(1));
	//SpeedController m_robotArm = new Spark(2);
	private Spark m_robotArm = new Spark(2);
	private Spark m_backMotor = new Spark(4);
	//Creates the robot arm motor control.
	private Joystick m_stick = new Joystick(0);
	private Timer m_timer = new Timer();
	private DoubleSolenoid m_doublesolenoid = new DoubleSolenoid(0,0,1);
	int STAHP = 0;
	int Target = 0;
	String m_teamLoc;

	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	@Override
	public void robotInit() {
		//Starts camera feeds
		CameraServer.getInstance().startAutomaticCapture();
		CameraServer.getInstance().startAutomaticCapture(1);
	}
	/**
	 * This function is run once each time the robot enters autonomous mode.
	 */
	@Override
	public void autonomousInit() {
		// Get information from the Field Management System (FMS)
		FieldInfo m_teamInfo = new FieldInfo();
		m_teamLoc = m_teamInfo.getFieldInfo();

		System.out.println("I'm in autonomous: Alliance color: " + 
				m_teamLoc.charAt(0) +
				" Plate Locations: Near switch: " +
				m_teamLoc.charAt(1) +
				" Scale: " +
				m_teamLoc.charAt(2) +
				" Far switch: " +
				m_teamLoc.charAt(3));

		m_timer.reset();
		m_timer.start();

	}
	/**
	 * This function is called periodically during autonomous.
	 */
	@Override	
	public void autonomousPeriodic() {

		//		System.out.println("I'm in auto Periodic");
		/*******  Original autonomous mode code
			if (m_timer.get()<6) {
				m_robotDrive.tankDrive(-.6, -.6);
			} else if (m_timer.get()<9){
				m_robotDrive.tankDrive(-.7, .7);
			} else if (m_timer.get()<15) {
				m_robotDrive.tankDrive(-.6, -.6);
			} else {
				m_robotDrive.stopMotor();
			}
			if (m_timer.get()<6) {
				m_robotArm.set(1.0);
			} else {
				m_robotArm.stopMotor();
			}
		 *******/
		NetworkTable table = NetworkTableInstance.getDefault().getTable("limelight");
		NetworkTableEntry tx = table.getEntry("tx");
		NetworkTableEntry ty = table.getEntry("ty");
		NetworkTableEntry ta = table.getEntry("ta");
		NetworkTableEntry ts = table.getEntry("ts");
		double x = tx.getDouble(0);
		double y = ty.getDouble(0);
		double area = ta.getDouble(0);
		System.out.println(area); //prints area of limelight view target occupies to console, also used to tell robot to stop
		if (m_timer.get() > 0) {
			if (m_timer.get() < 2) {
				m_robotDrive.tankDrive(.5, .5); //drives robot forwards for two seconds
			}
		}
		if (m_timer.get() > 4) {
			if (m_timer.get() < 4.05) {
				m_robotDrive.tankDrive(.8, -.8); //turns the robot right
			}
		}
		int Target = 0;
		if (Target == 1) {//Targeting Procedure
			if(area >= 12.5) {
				m_robotDrive.tankDrive(0, 0);
				STAHP = 1; //checks distance to target, if within distance, then stops robot
			}
			else if (area >= 12.5) {
				STAHP = 0;
			}
			if(STAHP == 0) {
				if(x < -10) {
					m_robotDrive.tankDrive(0.5, 0.6); //checks for target to the left, if so, turns robot left
				}
				else if(x > 10) {
					m_robotDrive.tankDrive(0.6, 0.5); //checks for target to the left, if so, turns robot
				}
				else {
					m_robotDrive.tankDrive(.6, .6);//default target chase speed
				}
			}
		}
		//autonomous target follow code
	}
	/**
	 * This function is called once each time the robot enters teleoperated mode.
	 */
	@Override
	public void teleopInit() {
		m_timer.reset();
		m_timer.start();
		System.out.println("I'm in teleop");
	}
	/**
	 * This function is called periodically during teleoperated mode.
	 */
	@Override
	public void teleopPeriodic() {
		NetworkTable table = NetworkTableInstance.getDefault().getTable("limelight");
		NetworkTableEntry tx = table.getEntry("tx");
		NetworkTableEntry ty = table.getEntry("ty");
		NetworkTableEntry ta = table.getEntry("ta");
		NetworkTableEntry ts = table.getEntry("ts");
		double x = tx.getDouble(0);
		double y = ty.getDouble(0);
		double area = ta.getDouble(0);
		double KpAim = -0.1f;
		double KpDistance = -0.1f;
		double min_aim_command = 0.05f;
		double left_command = 0.0f;
		double right_command = 0.0f;
		System.out.println("teleop");
		// Klann moved this from autonomousPeriodic to see if it works, and it does here!
		if (m_stick.getRawButton(4)) {
			System.out.println("Button Y");
			double heading_error = -x;
			double distance_error = -y;
			double steering_adjust = 0.0f;
			if (x > 1.0) {
				steering_adjust = KpAim*heading_error - min_aim_command;
			} else if (x < 1.0) {
				steering_adjust = KpAim*heading_error + min_aim_command;
			}

			double distance_adjust = KpDistance * distance_error;

			left_command += steering_adjust + distance_adjust;
			right_command -= steering_adjust + distance_adjust;
			m_robotDrive.tankDrive(left_command, right_command);
		}

		m_robotDrive.arcadeDrive( m_stick.getY(), m_stick.getX()); //get joystick axis


		if (m_stick.getRawAxis(2) > 0.0 && m_stick.getRawAxis(3)<= 0) {
			m_robotArm.setSpeed(-m_stick.getRawAxis(2));//Trigger Controls	
		}
		if (m_stick.getRawAxis(2) <= 0 && m_stick.getRawAxis(3) > 0) {
			m_robotArm.setSpeed(m_stick.getRawAxis(3));
		} 
		if (m_stick.getRawAxis(2) <= 0 && m_stick.getRawAxis(3) <=0) {
			m_robotArm.setSpeed(0.0);
		}


		if (m_stick.getRawButton(3)) { //chase toggle ON
			System.out.println("Button 3");
			System.out.println("Entering Target Chase");
			Target = 1;		
		}
		if(m_stick.getRawButton(4)) { //chase toggle OFF
			System.out.println("Button 4");
			System.out.println("Exiting Target Chase");
			Target = 0;
		}
		if (Target == 1) {
			System.out.println(area);
			if(area >= 12.5) {
				m_robotDrive.tankDrive(0, 0);
				STAHP = 1; //stops the robot
			}
			else if (area <= 12.5) {
				STAHP = 0;
			}
			if(STAHP == 0) {
				m_robotDrive.tankDrive(.6, .6); //sets motors to .6 speed in target mod
				if(x < -10) {
					m_robotDrive.tankDrive(0.5, 0.6); // checks for Target being to the left, if so, turns robot left
				}
				else if(x > 10) {
					m_robotDrive.tankDrive(0.6, 0.5); //checks for Target being to the right, if so, turns robot right
				}
			}
		}		
		//pneumatics code
		if (m_stick.getRawButton(1)) {
			m_doublesolenoid.set(DoubleSolenoid.Value.kForward);
			System.out.println("Button A");
		} else {
			m_doublesolenoid.set(DoubleSolenoid.Value.kOff);
		}
		if (m_stick.getRawButton(2)) {
			m_doublesolenoid.set(DoubleSolenoid.Value.kReverse);
			System.out.println("Button B");
		}
		if (m_stick.getRawButton(1) && m_stick.getRawButton(2))
			m_doublesolenoid.set(DoubleSolenoid.Value.kOff);
	}
	/**
	 * This function is called periodically during test mode.
	 */
	@Override
	public void testPeriodic() {
	}
}
