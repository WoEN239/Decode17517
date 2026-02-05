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

    public static Double servoDeltaR = 0.05;
    public static Double servoDeltaL = 0.;

    public static double ptoROpen = 0.67;
    public static double ptoRClose = 0.95;
    public static double ptoRBrakePad = 0.82;

    public static double ptoLOpen = 0.34;
    public static double ptoLClose = 0.03;
    public static double ptoLBrakePad = 0.18;
}
