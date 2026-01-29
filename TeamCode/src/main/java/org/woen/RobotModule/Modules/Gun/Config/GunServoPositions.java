package org.woen.RobotModule.Modules.Gun.Config;

import com.acmerobotics.dashboard.config.Config;

@Config
public class GunServoPositions {
    public static double eatRPos = 0.456;
    public static double shotRPos = 0.64;

    public static double eatCPos = 0.49;
    public static double shotCPos = 0.7;

    public static double eatLPos = 0.418;
    public static double shotLPos = 0.6;

    public static Double aimCFar = 0.4;
    public static Double aimCPat = 0.66;
    public static Double aimCNear = 0.57;
    public static Double aimCGoalNear = 0.8;

    public static Double servoDeltaR = 0d;
    public static Double aimRFar = aimCFar+servoDeltaR;
    public static Double aimRPat = aimCPat+servoDeltaR;
    public static Double aimRNear = aimCNear+servoDeltaR;
    public static Double aimRGoalNear = aimCGoalNear+servoDeltaR;
    public static Double servoDeltaL = 0.05;
    public static Double aimLFar = aimCFar+servoDeltaL;
    public static Double aimLPat = aimCPat+servoDeltaL;
    public static Double aimLNear = aimCNear+servoDeltaL;
    public static Double aimLGoalNear = aimCGoalNear+servoDeltaL;

    public static double ptoROpen = 0.8;//0.6;
    public static double ptoRClose = 0.95;
    public static double ptoRBrakePad = 0.8;
    public static double ptoLOpen = 0.2;//0.45;
    public static double ptoLClose = 0.07;
    public static double ptoLBrakePad = 0.2;
}
