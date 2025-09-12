package org.woen.Config;

import org.woen.Util.Pid.PidStatus;

public class ControlSystemConstant {
    public static double T = 0.020; //sec

    public static PidStatus xPid = new PidStatus(0,0,0,0,0,0,0);
    public static PidStatus hPid = new PidStatus(0,0,0,0,0,0,0);

}
