package org.usfirst.frc.team5903.robot;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;

/**
 * @author Austen
 *
 */
public class ControlMethods {

	private DifferentialDrive m_robotDrive = new DifferentialDrive(new VictorSP(0), new VictorSP(1));
	private Spark m_robotArm = new Spark(2);
	private Spark m_backMotor = new Spark(4);
	private VictorSP Claw = new VictorSP(3);

	// Creates the robot arm motor control.
	// swapped out in favor of the XboxController object//private Joystick m_stick =
	// new Joystick(1);
	private XboxController xb = new XboxController(0);
	private DoubleSolenoid m_doublesolenoid = new DoubleSolenoid(0, 0, 1);

	// Each method performs an action and is called to do that action as needed.

	public void Grab(double speed) {
		Claw.set(speed);
	}

	public void Release(double speed) {
		Claw.set(-speed);
	}

	public void StopGrabber() {
		Claw.stopMotor();
	}

	public void Teleopclaw() {
	}

	public void Setsafety(boolean state) {
		m_robotDrive.setSafetyEnabled(state);
		Claw.setSafetyEnabled(state);
	}

	public void Forwards(double speed) {// Declares the Forwards method
		if (speed > 0.0 && speed <= 1.0) {
			m_robotDrive.tankDrive(-speed, -speed);
		}
	}

	public void Backwards(double speed) {// declares the Backwards method
		if (speed > 0.0 && speed <= 1.0) {
			m_robotDrive.tankDrive(speed, speed);
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
		m_robotArm.setSpeed(-0.6);
	}

	public void Lowerarm() {// declares the Lowerarm method
		m_robotArm.setSpeed(0.6);
	}

	public void Stoparm() {// declares the Stoparm method
		m_robotArm.setSpeed(0);
	}

	public void Clawcontrol() {// declares the Clawcontrol method
		if (xb.getAButtonPressed()) {
			m_doublesolenoid.set(DoubleSolenoid.Value.kForward);
			System.out.println("Button A");
		}
		if (xb.getBButtonPressed()) {
			m_doublesolenoid.set(DoubleSolenoid.Value.kReverse);
			System.out.println("Button B");
		}
	}

	public void Climbcontrol() {
		if (xb.getBumper(Hand.kLeft)) {
			m_backMotor.set(1.0);
		} else if (xb.getBumper(Hand.kRight)) {// right bumper
			m_backMotor.set(-1.0);
		} else {
			m_backMotor.set(0);
			// These buttons are flipped. Just leave it this way because it works.
		}
	}

	public void Drivecontrol() {
		m_robotDrive.arcadeDrive(xb.getY(Hand.kLeft), xb.getX(Hand.kLeft)); // get joystick axis and set the
																			// motors to that speed
	}

	public void Grabbercontrol() {
		Claw.set(xb.getY(Hand.kRight));
	}

	public void Triggercontrol() {
		if (xb.getTriggerAxis(Hand.kLeft) > 0.0 && xb.getTriggerAxis(Hand.kRight) <= 0) {
			m_robotArm.setSpeed(xb.getTriggerAxis(Hand.kLeft));// Trigger Controls
		}
		if (xb.getTriggerAxis(Hand.kLeft) <= 0 && xb.getTriggerAxis(Hand.kRight) > 0) {
			m_robotArm.setSpeed(-xb.getTriggerAxis(Hand.kRight));
		}
		if (xb.getTriggerAxis(Hand.kLeft) <= 0 && xb.getTriggerAxis(Hand.kRight) <= 0) {
			m_robotArm.setSpeed(0.0);
		}
	}

	public void Openclaw() {
		m_doublesolenoid.set(DoubleSolenoid.Value.kReverse);
	}

	public void Closeclaw() {
		m_doublesolenoid.set(DoubleSolenoid.Value.kForward);
	}

	public void Stopclaw() {
		m_doublesolenoid.set(DoubleSolenoid.Value.kOff);
	}
}
