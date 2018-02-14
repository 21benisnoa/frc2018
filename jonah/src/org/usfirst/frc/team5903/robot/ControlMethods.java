package org.usfirst.frc.team5903.robot;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;


public class ControlMethods {

private DifferentialDrive m_robotDrive = new DifferentialDrive(new Spark(0), new Spark(1));
//SpeedController m_robotArm = new Spark(2);
private Spark m_robotArm = new Spark(2);
private Spark m_backMotor = new Spark(4);
//Creates the robot arm motor control.
private Joystick m_stick = new Joystick(0);
private DoubleSolenoid m_doublesolenoid = new DoubleSolenoid(0,0,1);
private static final double kAngleSetpoint = 0.0;
private static final double kP = 0.005; // propotional turning constant

	public Forwards() {//Declares the Forwards method
		m_robotDrive.tankDrive(1, 1);
	}
	public Backwards() {//declares the Backwards method
		
		return;	
	}
	public Right() {//declares the Right method
		
	}
	public Left() {//declares the Left method
		
	}
}
