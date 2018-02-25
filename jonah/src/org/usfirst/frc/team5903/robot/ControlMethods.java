package org.usfirst.frc.team5903.robot;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;

/**
 * @author Austen
 *
 */
public class ControlMethods {

	private DifferentialDrive m_robotDrive = new DifferentialDrive(new Spark(0), new Spark(1));
	// SpeedController m_robotArm = new Spark(2);
	private Spark m_robotArm = new Spark(2);
	private Spark m_backMotor = new Spark(4);
	// Creates the robot arm motor control.
	private Joystick m_stick = new Joystick(0);
	private DoubleSolenoid m_doublesolenoid = new DoubleSolenoid(0, 0, 1);

	// Each method performs an action and is called to do that action as needed.

	public void Forwards(double speed) {// Declares the Forwards method
		if (speed > 0.0 && speed <= 1.0) {
			m_robotDrive.tankDrive(speed, speed);
		}
	}

	public void Backwards(double speed) {// declares the Backwards method
		if (speed > 0.0 && speed <= 1.0) {
			m_robotDrive.tankDrive(-speed, -speed);
		}
	}

	public void Right(double speed) {// declares the Right method
		if (speed >= 0.0 && speed <= 1.0) {
			m_robotDrive.tankDrive(speed, -speed);
		}
	}

	public void Left(double speed) {// declares the Left method
		if (speed >= 0.0 && speed <= 1.0) {
			m_robotDrive.tankDrive(-speed, speed);
		}
	}

	public void Stop() {// declares the Stop method
		m_robotDrive.tankDrive(0, 0);
	}

	public void Raisearm() {// declares the Raisearm method
		m_robotArm.setSpeed(1);
	}

	public void Lowerarm() {// declares the Lowerarm method
		m_robotArm.setSpeed(-1);
	}

	public void Stoparm() {// declares the Stoparm method
		m_robotArm.setSpeed(0);
	}

	public void Clawcontrol() {// declares the Clawcontrol method
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

	public void Climbcontrol() {
		if (m_stick.getRawButton(5)) {// left bumper
			m_backMotor.set(.5);
		} else if (m_stick.getRawButton(6)) {// right bumper
			m_backMotor.set(-.5);
		} else {
			m_backMotor.set(0);
		}
	}

	public void Joystickcontrol() {
		m_robotDrive.arcadeDrive(m_stick.getY(), m_stick.getX()); // get joystick axis and set the motors to that speed
	}

	public void Triggercontrol() {
		if (m_stick.getRawAxis(2) > 0.0 && m_stick.getRawAxis(3) <= 0) {
			m_robotArm.setSpeed(-m_stick.getRawAxis(2));// Trigger Controls
		}
		if (m_stick.getRawAxis(2) <= 0 && m_stick.getRawAxis(3) > 0) {
			m_robotArm.setSpeed(m_stick.getRawAxis(3));
		}
		if (m_stick.getRawAxis(2) <= 0 && m_stick.getRawAxis(3) <= 0) {
			m_robotArm.setSpeed(0.0);
		}
	}

	public void Openclaw() {
		m_doublesolenoid.set(DoubleSolenoid.Value.kForward);
	}

	public void Closeclaw() {
		m_doublesolenoid.set(DoubleSolenoid.Value.kReverse);
	}

	public void Stopclaw() {
		m_doublesolenoid.set(DoubleSolenoid.Value.kOff);
	}
}
