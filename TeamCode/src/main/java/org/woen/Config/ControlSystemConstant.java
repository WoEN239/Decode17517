package org.woen.Config;

import com.acmerobotics.dashboard.config.Config;

import org.woen.Util.Pid.PidStatus;

@Config
public class ControlSystemConstant {

    public static class FeedbackConfig {
        public double PPLocalR   = 50;
        public double PPTransVel = 30;
        public PidStatus xPid = new PidStatus(0.,0,0.00,0,0,0,0);
        public PidStatus hPid = new PidStatus(2,10,0.05,0,0,0.02,0);

        public PidStatus xPidVel = new PidStatus(0.635, 0, 0., 0, 0, 0, 0.1,0);
        public PidStatus hPidVel = new PidStatus(0.0141, 0, 0., 0, 0, 0, 0);;
    }
    public static FeedbackConfig feedbackConfig = new FeedbackConfig();

    public static class RobotSizeConfig{ //sm
        public double lx = 6.3;
        public double ly = 16.4375;
        public double B = 44;
        public double wheelR = 55;
    }
    public static RobotSizeConfig robotSizeConfig = new RobotSizeConfig();

    public static class FeedforwardConfig{
        public Double xFeedforwardKA = 1.2d;
        public Double xFeedforwardKAReverse = 0d;
        public Double xFeedforwardKV = 5.1d;
        public Double hSlip = 0.6;
        public Double staticVoltageOffsetX = 0.909d;
        public Double staticVoltageOffsetH = 2.3;
    }
    public static FeedforwardConfig feedforwardConfig = new FeedforwardConfig();
    
    public static class OdometerConstantConfig {
        public double meterPerAngle = 0.29;
        public double y_odometer_radius = 0;
    }
    public static OdometerConstantConfig odometerConstantConfig = new OdometerConstantConfig();

    public static class GunConfig {
        public double velTol = 60;
        public double patternFireDelay = 1;


        public double shootVelSidePattern = 1860;
        public double shootVelSideFar = 1860;
        public double shootVelSideNear = 1630;

        public double shootVelCPattern = 1860;
        public double shootVelCFar = 1860;
        public double shootVelCNear = 1600;

        public double shootVelCGoalNear = 1470;

        public double shootVelSideGoalNear = 1470;

        public double adaptiveDeltaPos = 0;
        public double distLow = 186;
        public double distHi = 268;

        public double lightPower = 0.7;

        public PidStatus rightPidStatus = new PidStatus(0.001, 0, 0, 0.000456, 0, 0, 0,40);
        public PidStatus leftPidStatus = new PidStatus(0.001, 0, 0, 0.00039, 0, 0, 0,40);
        public PidStatus centerPidStatus = new PidStatus(0.001, 0, 0, 0.00043, 0, 0, 0,40);
    }
    public static GunConfig gunConfig = new GunConfig();

}
