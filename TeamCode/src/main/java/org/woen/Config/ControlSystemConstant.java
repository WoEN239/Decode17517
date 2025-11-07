package org.woen.Config;

import com.acmerobotics.dashboard.config.Config;

import org.woen.Util.Pid.PidStatus;

public class ControlSystemConstant {

    @Config
    public static class FeedbackConfig {
        public static double PPLocalR = 10;
        public static PidStatus xPid = new PidStatus(0.2,0,0,0,0,0,0);
        public static PidStatus hPid = new PidStatus(0.2,0,0,0,0,0 ,0);
    }

    @Config
    public static class RobotSizeConfig{ //sm
        public static double lx = 6.3;
        public static double ly = 16.4375;
        public static double B = ly*2;
        public static double wheelR = 75;
    }

    @Config
    public static class FeedforwardConfig{
        public static Double xFeedforwardKV = 3.5d;
        public static Double xFeedforwardKA = 1.d;
        public static Double hSlip = 2.45d;
        public static Double staticVoltageOffset = 2.63d;
    }

    public static class KinematicConstrainConfig {
        public static Double transAccel = 15d;
        public static Double angleAccel = 2d;
        public static Double transVel = 15d;
        public static Double angleVel = 2d;
    }
}
