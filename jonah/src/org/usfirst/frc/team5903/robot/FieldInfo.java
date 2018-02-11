package org.usfirst.frc.team5903.robot;

import edu.wpi.first.wpilibj.DriverStation;

public class FieldInfo {
	DriverStation.Alliance color;

	public String getFieldInfo() {
		String gameData;
		String Teamloc;
		color = DriverStation.getInstance().getAlliance();
		gameData = DriverStation.getInstance().getGameSpecificMessage();

		if ( color == DriverStation.Alliance.Blue ) {
			Teamloc = "B";
		} else {
			Teamloc = "R";
		}

		Teamloc = Teamloc + gameData;

		return Teamloc;
	}

//	public String processGameData(String TeamLoc) {
//
//		if (TeamLoc.length() > 1) {
//			if (TeamLoc.charAt(1) == 'L') {
//				//left code here, ally switch
//			} else {
//				//right code here, switch
//				if (TeamLoc.length() > 2) {
//					if (TeamLoc.charAt(2) == 'R') {
//						//right code here, ally scale
//					} else {
//						if (TeamLoc.length() > 3) {
//							if (TeamLoc.charAt(3) == 'L') {
//								//left code here, scale
//							} else {
//								if (TeamLoc.length() > 3) {
//									if (TeamLoc.charAt(3) == 'R') {
//										//right code here, opposing switch
//									} else {
//										if (TeamLoc.length() >2){
//											if (TeamLoc.charAt(2) == 'L') {
//												//left code here, opposing switch
//											}
//										}
//									}
//								}
//							}
//						}
//					}
//				}
//			}
//		}
//		return TeamLoc;
//	}
}