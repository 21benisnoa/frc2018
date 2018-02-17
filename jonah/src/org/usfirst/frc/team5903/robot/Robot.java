/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.usfirst.frc.team5903.robot;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.Sendable;
import edu.wpi.first.wpilibj.Spark;
//import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.networktables.NetworkTable;
import org.usfirst.frc.team5903.robot.FieldInfo;
import org.usfirst.frc.team5903.robot.ControlMethods;

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
	private FieldCalculations m_FieldCalculations = new FieldCalculations(); //declares a Field Calculations object so the Pathid can be retrieved
	private ControlMethods m_ControlMethods = new ControlMethods(); //declares a ControlMethods object so methods can be called

	// gyro calibration constant, may need to be adjusted;
	// gyro value of 360 is set to correspond to one full revolution
	private static final double kVoltsPerDegreePerSecond = 0.0128;
	private ADXRS450_Gyro m_gyro = new ADXRS450_Gyro(SPI.Port.kOnboardCS0);
	private int STAHP = 0;
	private int Target = 0;
	private String Pathid;
	String m_teamLoc;

	private SmartDashboard m_dash = new SmartDashboard();
	//private final SendableChooser<String> positionChooser = new SendableChooser<>();
	public SendableChooser positionChooser = new SendableChooser();
	private String dashData;

	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	@Override
	public void robotInit() {
		UsbCamera cam1;
		UsbCamera cam2;
		//Starts camera feeds
		cam1 = CameraServer.getInstance().startAutomaticCapture(0);
		cam2 = CameraServer.getInstance().startAutomaticCapture(1);
		m_gyro.calibrate(); //Calibrate gyro

		try
		{

			positionChooser.addDefault("Failsafe: Drive straight", "Failsafe");
			positionChooser.addObject("Starting on the Left", "Left");
			positionChooser.addObject("Starting in the Middle", "Middle");
			positionChooser.addObject("Starting on the Right", "Right");

			SmartDashboard.putData("StartingPosition", positionChooser);
		}
		catch (Exception e)
		{
			System.out.printf("StartingPosition is %s\n", e.toString());
		}

	}
	/**
	 * This function is run once each time the robot enters autonomous mode.
	 */
	@Override
	public void autonomousInit() {
		// Get information from the Field Management System (FMS)
		FieldInfo m_teamInfo = new FieldInfo();
		m_teamLoc = m_teamInfo.getFieldInfo();
		Pathid = m_FieldCalculations.Pathid(); // Retrieves the Path id from Field Calculations

		try {
			dashData = (String) positionChooser.getSelected();
			System.out.printf("dashData is: %s\n", dashData.toString());
		}
		catch (Exception e) {
			System.out.printf("Exception encountered %s\n", e.toString());
		}

		System.out.println("I'm in autonomous: Alliance color: " + 
				m_teamLoc.charAt(0) +
				" Plate Locations: Near switch: " +
				m_teamLoc.charAt(1) +
				" Scale: " +
				m_teamLoc.charAt(2) +
				" Far switch: " +
				m_teamLoc.charAt(3));

		//will be one of: "Left" "Middle" "Right" or null
		String StartingPosition = (String) positionChooser.getSelected();
		// TODO: Location = StartingPosition;
		Location = "Left";

		m_timer.reset();
		m_timer.start();

		// autonomousCommand = (Command) autoChooser.getSelected(); // dklann: added for SmartDashboard functionality
		// autonomousCommand.start(); // dklann: added for SmartDashboard functionality

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
		double Gx = m_gyro.getAngle(); //gyro x
		//System.out.println("Angle= " + Gx); //print gyro to console
		//System.out.println(area); //prints area of limelight view target occupies to console, also used to tell robot to stop
		double turningValue = (kAngleSetpoint - m_gyro.getAngle()) * kP;

		// Invert the direction of the turn if we are going backwards

		//		turningValue = Math.copySign(turningValue, m_stick.getY());
		//		m_robotDrive.arcadeDrive(m_stick.getY(), turningValue);

		//BEGIN DRIVE CODE


		if (Location == "Left") {//LEFT POSITION CODE
			if (Pathid == "11") {//checks for pathid being 11
				System.out.println("location 1, pathid 11");
				if (m_timer.get() > 0) {
					if (m_timer.get() < 4) {//moves robot forwards and grips cube
						m_ControlMethods.Forwards();
						m_ControlMethods.Closeclaw();
					}
				}
				else if (m_timer.get() > 4) {//turns robot right
					if (m_timer.get() < 4.3) {
						m_ControlMethods.Right();
					}
				}
				else if (m_timer.get() > 4.3) {//moves robot forwards
					if (m_timer.get() < 6) {
						m_ControlMethods.Forwards();
					}
				}
				else if (m_timer.get() > 6) {//turns robot right
					if (m_timer.get() < 6.3) {
						m_ControlMethods.Right();
					}
				}
				else if (m_timer.get() > 6.3) { //moves robot forwards to switch and raises arm
					if (m_timer.get() < 7) {
						m_ControlMethods.Forwards();
						m_ControlMethods.Raisearm();
					}
				}
				else if (m_timer.get() > 7) {// stops robot at switch and opens claw
					if (m_timer.get() < 7.5) {
						m_ControlMethods.Openclaw();
						m_ControlMethods.Stop();
						m_ControlMethods.Stoparm();
					}
				}
				else if (m_timer.get() > 7.5) {//stops claw pneumatics and puts robot in reverse
					if (m_timer.get() < 8) {
						m_ControlMethods.Stopclaw();
						m_ControlMethods.Backwards();
					}
				}
				else if (m_timer.get() > 8) {//stops robot
					if (m_timer.get() < 9) {
						m_ControlMethods.Stop();
						m_ControlMethods.Lowerarm();
					}
				}
				else if (m_timer.get() > 9) {//stops arm and grabs cube
					if (m_timer.get() <  9.3) {
						m_ControlMethods.Stoparm();
						m_ControlMethods.Closeclaw();
					}
				}
				else if (m_timer.get() > 9.3) {// puts robot in reverse and starts raising arm
					if (m_timer.get() < 10) {
						m_ControlMethods.Raisearm();
						m_ControlMethods.Backwards();
					}
				}
				else if (m_timer.get() > 12) {//stops robot and arm
					if (m_timer.get() < 12.2) {
						m_ControlMethods.Stop();
						m_ControlMethods.Stoparm();
					}
				}
				else if (m_timer.get() > 12.2) {//opens claw
					if (m_timer.get() < 12.4) {
						m_ControlMethods.Openclaw();
					}
				}
				else if (m_timer.get() > 12.4) {//stops claw
					if (m_timer.get() < 12.6) {
						m_ControlMethods.Stopclaw();
					}
				}
			}//END PATHID 11 CODE

			else if (Pathid == "14") {//checks for pathid being 14
				System.out.println("location 1, pathid 14");
				if (m_timer.get() > 0) {
					if (m_timer.get() < 4) {//moves robot forwards and grips cube
						m_ControlMethods.Forwards();
						m_ControlMethods.Closeclaw();
					}
				}
				else if (m_timer.get() > 4) {//turns robot right
					if (m_timer.get() < 4.3) {
						m_ControlMethods.Right();
					}
				}
				else if (m_timer.get() > 4.3) {//moves robot forwards
					if (m_timer.get() < 6) {
						m_ControlMethods.Forwards();
					}
				}
				else if (m_timer.get() > 6) {//turns robot right
					if (m_timer.get() < 6.3) {
						m_ControlMethods.Right();
					}
				}
				else if (m_timer.get() > 6.3) { //moves robot forwards to switch and raises arm
					if (m_timer.get() < 7) {
						m_ControlMethods.Forwards();
						m_ControlMethods.Raisearm();
					}
				}
				else if (m_timer.get() > 7) {// stops robot at switch and opens claw
					if (m_timer.get() < 7.5) {
						m_ControlMethods.Openclaw();
						m_ControlMethods.Stop();
						m_ControlMethods.Stoparm();
					}
				}
				else if (m_timer.get() > 7.5) {//stops claw pneumatics and puts robot in reverse
					if (m_timer.get() < 8) {
						m_ControlMethods.Stopclaw();
						m_ControlMethods.Backwards();
					}
				}
				else if (m_timer.get() > 8) {//stops robot
					if (m_timer.get() < 9) {
						m_ControlMethods.Stop();
						m_ControlMethods.Lowerarm();
					}
				}
				else if (m_timer.get() > 9) {//stops arm and grabs cube
					if (m_timer.get() <  9.3) {
						m_ControlMethods.Stoparm();
						m_ControlMethods.Closeclaw();
					}
				}
				else if (m_timer.get() > 9.3) {// puts robot in reverse and starts raising arm
					if (m_timer.get() < 10) {
						m_ControlMethods.Raisearm();
						m_ControlMethods.Backwards();
					}
				}
				else if (m_timer.get() > 10) {//turns robot left
					if (m_timer.get() < 10.5) {
						m_ControlMethods.Left();
					}
				}
				else if (m_timer.get() > 10.5) {//moves robot forwards
					if (m_timer.get() < 10.7) {
						m_ControlMethods.Forwards();
					}
				}
				else if (m_timer.get() > 12) {//stops arm and turns robot right
					if (m_timer.get() < 12.5) {
						m_ControlMethods.Stoparm();
						m_ControlMethods.Right();
					}
				}
				else if (m_timer.get() > 12.5) {//moves robot backwards
					if (m_timer.get() < 13.4) {
						m_ControlMethods.Backwards();
					}
				}
				else if (m_timer.get() > 13.4) {//opens claw
					if (m_timer.get() < 13.6) {
						m_ControlMethods.Openclaw();
					}
				}
				else if (m_timer.get() > 13.6) {//stops claw
					if (m_timer.get() < 13.8) {
						m_ControlMethods.Stopclaw();
					}
				}
			}//END PATHID 14 CODE
			else if (Pathid == "22") {//checks for pathid being 22
				System.out.println("location 1, pathid 22");
				if (m_timer.get() < 4) {//moves robot forwards and grips cube
					m_ControlMethods.Forwards();
					m_ControlMethods.Closeclaw();
				}
			}
			else if (m_timer.get() > 4) {//turns robot right
				if (m_timer.get() < 4.3) {
					m_ControlMethods.Right();
				}
			}
			else if (m_timer.get() > 4.3) {//moves robot forwards
				if (m_timer.get() < 8) {
					m_ControlMethods.Forwards();
				}
			}
			else if (m_timer.get() > 6) {//turns robot right
				if (m_timer.get() < 8.3) {
					m_ControlMethods.Right();
				}
			}
			else if (m_timer.get() > 8.3) { //moves robot forwards to switch and raises arm
				if (m_timer.get() < 9) {
					m_ControlMethods.Forwards();
					m_ControlMethods.Raisearm();
				}
			}
			else if (m_timer.get() > 9) {// stops robot at switch and opens claw
				if (m_timer.get() < 9.5) {
					m_ControlMethods.Openclaw();
					m_ControlMethods.Stop();
					m_ControlMethods.Stoparm();
				}
			}
			else if (m_timer.get() > 9.5) {//stops claw pneumatics and puts robot in reverse
				if (m_timer.get() < 10) {
					m_ControlMethods.Stopclaw();
					m_ControlMethods.Backwards();
				}
			}
			else if (m_timer.get() > 10) {//stops robot
				if (m_timer.get() < 11) {
					m_ControlMethods.Stop();
					m_ControlMethods.Lowerarm();
				}
			}
			else if (m_timer.get() > 11) {//stops arm and grabs cube
				if (m_timer.get() <  11.3) {
					m_ControlMethods.Stoparm();
					m_ControlMethods.Closeclaw();
				}
			}
			else if (m_timer.get() > 11.3) {// puts robot in reverse and starts raising arm
				if (m_timer.get() < 12) {
					m_ControlMethods.Raisearm();
					m_ControlMethods.Backwards();
				}
			}
			else if (m_timer.get() > 14) {//stops robot and arm
				if (m_timer.get() < 14.2) {
					m_ControlMethods.Stop();
					m_ControlMethods.Stoparm();
				}
			}
			else if (m_timer.get() > 14.2) {//opens claw
				if (m_timer.get() < 14.4) {
					m_ControlMethods.Openclaw();
				}
			}
			else if (m_timer.get() > 14.4) {//stops claw
				if (m_timer.get() < 14.6) {
					m_ControlMethods.Stopclaw();
				}
			}//END PATHID 22 CODE

			else if (Pathid == "23") {//checks for Pathid being 23
				System.out.println("location 1, pathid 23");
				if (m_timer.get() < 4) {//moves robot forwards and grips cube
					m_ControlMethods.Forwards();
					m_ControlMethods.Closeclaw();
				}
			}
			else if (m_timer.get() > 4) {//turns robot right
				if (m_timer.get() < 4.3) {
					m_ControlMethods.Right();
				}
			}
			else if (m_timer.get() > 4.3) {//moves robot forwards
				if (m_timer.get() < 8) {
					m_ControlMethods.Forwards();
				}
			}
			else if (m_timer.get() > 6) {//turns robot right
				if (m_timer.get() < 8.3) {
					m_ControlMethods.Right();
				}
			}
			else if (m_timer.get() > 8.3) { //moves robot forwards to switch and raises arm
				if (m_timer.get() < 9) {
					m_ControlMethods.Forwards();
					m_ControlMethods.Raisearm();
				}
			}
			else if (m_timer.get() > 9) {// stops robot at switch and opens claw
				if (m_timer.get() < 9.5) {
					m_ControlMethods.Openclaw();
					m_ControlMethods.Stop();
					m_ControlMethods.Stoparm();
				}
			}
			else if (m_timer.get() > 9.5) {//stops claw pneumatics and puts robot in reverse
				if (m_timer.get() < 10) {
					m_ControlMethods.Stopclaw();
					m_ControlMethods.Backwards();
				}
			}
			else if (m_timer.get() > 10) {//stops robot
				if (m_timer.get() < 11) {
					m_ControlMethods.Stop();
					m_ControlMethods.Lowerarm();
				}
			}
			else if (m_timer.get() > 11) {//stops arm and grabs cube
				if (m_timer.get() <  11.3) {
					m_ControlMethods.Stoparm();
					m_ControlMethods.Closeclaw();
				}
			}
			else if (m_timer.get() > 11.3) {// puts robot in reverse and starts raising arm
				if (m_timer.get() < 12) {
					m_ControlMethods.Raisearm();
					m_ControlMethods.Backwards();
				}
			}
			else if (m_timer.get() > 12) {//turns robot left
				if (m_timer.get() < 12.5) {
					m_ControlMethods.Left();
				}
			}
			else if (m_timer.get() > 12.5) {//moves robot backwards
				if (m_timer.get() < 12.7) {
					m_ControlMethods.Backwards();
				}
			}
			else if (m_timer.get() > 14) {//stops arm and turns robot right
				if (m_timer.get() < 14.5) {
					m_ControlMethods.Stoparm();
					m_ControlMethods.Right();
				}
				else if (m_timer.get() > 16) {//stops robot and arm
					if (m_timer.get() < 16.2) {
						m_ControlMethods.Stop();
						m_ControlMethods.Stoparm();
					}
				}
				else if (m_timer.get() > 16.2) {//opens claw
					if (m_timer.get() < 16.4) {
						m_ControlMethods.Openclaw();
					}
				}
				else if (m_timer.get() > 16.4) {//stops claw
					if (m_timer.get() < 16.6) {
						m_ControlMethods.Stopclaw();
					}
				}
			}//END PATHID 23 CODE

			if (Location == "Middle") {//MIDDLE POSITION CODE
				System.out.println("location 2");
				if (m_timer.get() > 0) {//moves robot forwards and gribs cube
					if (m_timer.get() < 3) {
						m_ControlMethods.Forwards();
						m_ControlMethods.Closeclaw();
					}
				}
				else if (m_timer.get() > 3) {//turns robot left
					if (m_timer.get() < 3.4) {
						m_ControlMethods.Left();
					}
				}
				else if (m_timer.get() > 3.4) {//moves robot forwards
					if (m_timer.get() < 5.4) {
						m_ControlMethods.Forwards();
					}
				}
				else if (m_timer.get() > 5.4) {//turns robot right
					if (m_timer.get() < 5.8) {
						m_ControlMethods.Right();
					}
				}
				else if (m_timer.get() > 5.8) {//moves robot forwards
					if (m_timer.get() < 8) {
						m_ControlMethods.Forwards();
					}
				}
				else if (m_timer.get() > 8) {//turns robot right
					if (m_timer.get() < 8.4) {
						m_ControlMethods.Right();
					}
				}
				else if (m_timer.get() > 8.4) {//moves robot forwards
					if (m_timer.get() < 10) {
						m_ControlMethods.Forwards();
					}
				}
				else if (m_timer.get() > 10) {//turns robot right to face switch
					if (m_timer.get() < 10.4) {
						m_ControlMethods.Right();
					}
				}
				else if (m_timer.get() > 10.4) {//moves robot forwards to switch and raises arm
					if (m_timer.get() < 11) {
						m_ControlMethods.Forwards();
						m_ControlMethods.Raisearm();
					}
				}
				else if (m_timer.get() > 11) {//opens claw and stops arm
					if (m_timer.get() < 11.2) {
						m_ControlMethods.Openclaw();
						m_ControlMethods.Stoparm();
					}
				}
				else if (m_timer.get() > 11.2) {//stops claw
					if (m_timer.get() < 11.4) {
						m_ControlMethods.Stopclaw();
					}
				}
			}//END MIDDLE POSITION CODE

			if (Location == "Right") {//RIGHT POSITION CODE
				if (Pathid == "11") {//checks for pathid being 11
					System.out.println("location 3, pathid 11");
				}
				else if (Pathid == "14") {//checks for pathid being 14
					System.out.println("location 3, pathid 14");
				}
				else if (Pathid == "22") {//checks for pathid being 22
					System.out.println("location 3, pathid 22");
				}
				else if (Pathid == "23") {//checks for Pathid being 23
					System.out.println("location 3, pathid 23");
				}
			}
		}
		/*		int Target = 0;
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
		}*/
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
		/*		if (m_stick.getRawButton(4)) {
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
		 */
		m_ControlMethods.Joystickcontrol();//joystick control call
		m_ControlMethods.Triggercontrol();//trigger control call
		m_ControlMethods.Clawcontrol();//Claw control call
		m_ControlMethods.Climbcontrol();//Climb control call

		//		if (m_stick.getRawButton(3)) { //chase toggle ON
		//			System.out.println("Button 3");
		//			System.out.println("Entering Target Chase");
		//			Target = 1;		
		//		}
		//		if(m_stick.getRawButton(4)) { //chase toggle OFF
		//			System.out.println("Button 4");
		//			System.out.println("Exiting Target Chase");
		//			Target = 0;
		//		}
		//		if (Target == 1) {
		//			System.out.println(area);
		//			if(area >= 12.5) {
		//				m_robotDrive.tankDrive(0, 0);
		//				STAHP = 1; //stops the robot
		//			}
		//			else if (area <= 12.5) {
		//				STAHP = 0;
		//			}
		//			if(STAHP == 0) {
		//				m_robotDrive.tankDrive(.6, .6); //sets motors to .6 speed in target mod
		//				if(x < -10) {
		//					m_robotDrive.tankDrive(0.5, 0.6); // checks for Target being to the left, if so, turns robot left
		//				else if(x > 10) {
		//					m_robotDrive.tankDrive(0.6, 0.5); //checks for Target being to the right, if so, turns robot right
	}		
	/**
	 * This function is called periodically during test mode.
	 */
	@Override
	public void testPeriodic() {
	}
}
