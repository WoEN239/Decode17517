package org.woen.Config;

import com.acmerobotics.dashboard.config.Config;

import org.woen.Util.Pid.PidStatus;

@Config
public class ControlSystemConstant {
    public static double T = 0.020; //sec

    public static PidStatus xPid = new PidStatus(0,0,0,0,0,0,0);
    public static PidStatus hPid = new PidStatus(10,10,0,0,0,5 ,0);

    public static Double xFeedforwardKV = 0.8d;
    public static Double xFeedforwardKA = 0.125d;

    public static Double hSlip = 1.6d;
    public static Double staticVoltageOffset = 1d;

    public static Double transAccel = 15d;
    public static Double angleAccel = 2d;

    public static Double transVel = 15d;
    public static Double angleVel = 2d;

    //sm
    public static double lx = 6.3;
    public static double ly = 16.4375;

    public static double B = ly*2;

    public static double wheelR = 9.6;

}
