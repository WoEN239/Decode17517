package org.woen.RobotModule.Modules.Gun.Config;

import com.acmerobotics.dashboard.config.Config;

@Config
public class GunServoPositions {

    public static double eatRPos = 0.465;
    public static double shotRPos = 0.7;

    public static double eatCPos = 0.66;
    public static double shotCPos = 0.82;

    public static double eatLPos = 0.31;
    public static double shotLPos = 0.48;

    public static Double aimCFar = 0.3;
    public static Double aimCPat = 0.3;
    public static Double aimCNear = 0.6;
    public static Double aimCGoalNear = 0.85;

    public static Double servoDeltaR = 0.125;
    public static Double aimRFar = aimCFar+servoDeltaR;
    public static Double aimRPat = aimCPat+servoDeltaR;
    public static Double aimRNear = aimCNear+servoDeltaR;
    public static Double aimRGoalNear = aimCGoalNear+servoDeltaR;

    public static Double servoDeltaL = -0.125;
    public static Double aimLFar = aimCFar+servoDeltaL;
    public static Double aimLPat = aimCPat+servoDeltaL;
    public static Double aimLNear = aimCNear+servoDeltaL;
    public static Double aimLGoalNear = aimCGoalNear+servoDeltaL;

    public static double ptoROpen = 0.8;
    public static double ptoLOpen = 0;
    public static double brakePadOnPos = 0;
    public static double brakePadOffPos = 0.35;

    public static double ptoRClose = 0.5;
    public static double ptoLClose = 1;
}
