package org.woen.Config;

import com.acmerobotics.dashboard.config.Config;

import org.woen.Util.Pid.PidStatus;

@Config
public class ControlSystemConstant {
    public static double T = 0.020; //sec

    public static PidStatus xPid = new PidStatus(0,0,0,0,0,0,0);
    public static PidStatus hPid = new PidStatus(0,0,0,0,0,0,0);

    public static Double feedforwardK = 1d;

    //sm
    public static double lx = 12.7;
    public static double ly = 13.9;
    public static double wheelR = 9.6;

}
