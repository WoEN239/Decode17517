package org.woen.Config;

import org.woen.RobotModule.Modules.DriveTrain.VoltageController.Architecture.WheelValueMap;
import org.woen.Util.Pid.PidStatus;

public class ControlSystemConstant {
    public static double T = 0.020; //sec

    public static PidStatus xPid = new PidStatus(0,0,0,0,0,0,0);
    public static PidStatus hPid = new PidStatus(0,0,0,0,0,0,0);

    public static WheelValueMap feedforwardK = new WheelValueMap(0,0,0,0);

    //sm
    public static double lx = 0;
    public static double ly = 0;
    public static double wheelR = 9.6;

}
