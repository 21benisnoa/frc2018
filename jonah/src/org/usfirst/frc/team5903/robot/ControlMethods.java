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


//Each method performs an action and is called to do that action as needed.

	public void Forwards() {//Declares the Forwards method
		m_robotDrive.tankDrive(1, 1);
	}
	public void Backwards() {//declares the Backwards method
		m_robotDrive.tankDrive(-1, -1);
	}
	public void Right() {//declares the Right method
		m_robotDrive.tankDrive(1, -1);
	}
	public void Left() {//declares the Left method
		m_robotDrive.tankDrive(-1, 1);
	}
	public void Stop() {//declares the Stop method
		m_robotDrive.tankDrive(0, 0);
	}
	public void Raisearm() {//declares the Raisearm method
		m_robotArm.setSpeed(1);
	}
	public void Lowerarm() {//declares the Lowerarm method
		m_robotArm.setSpeed(-1);
	}
	public void Stoparm() {//declares the Stoparm method
		m_robotArm.setSpeed(0);
	}
	public void Grab() {//declares the Grab method
		m_doublesolenoid.set(DoubleSolenoid.Value.kForward);
	}
	public void Release() {//declares the Release method
		m_doublesolenoid.set(DoubleSolenoid.Value.kReverse);
	}
	public void Stopclaw() {//declares the stop claw method
		m_doublesolenoid.set(DoubleSolenoid.Value.kOff);
	}
	public void Backmotorforwards() {
		m_backMotor.set(.5);
	}
	public void Backmotorbackwards() {
		m_backMotor.set(-.5);
	}
	public void Backmotorstop() {
		m_backMotor.set(0);
	}
	public void Joystickcontrol() {
		m_robotDrive.arcadeDrive( m_stick.getY(), m_stick.getX()); //get joystick axis and set the motors to that speed
	}
	public void Triggercontrol() {
		
	}
}
