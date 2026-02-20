package org.woen.Config;

import com.acmerobotics.dashboard.config.Config;

import org.woen.Util.Pid.PidStatus;

@Config
public class ControlSystemConstant {

    public static class FeedbackConfig {
        public double PPLocalR   = 50;
        public double PPTransVel = 30;
        public PidStatus xPid = new PidStatus(0.,0,0.00,0,0,0,0);
        public PidStatus hPid = new PidStatus(2,10,0.05,0,0,0.1,0.05,0.005);

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
    
    public static class AdaptiveFireConfig {
        public double fullFireDelay = 0.025;
        public ValLerp farVel = new ValLerp(315,370,1700,1750);
        public ValLerp farAngle = new ValLerp(315,370,0.7,0.8);
        public ValLerp nearVel = new ValLerp(178,260,1400,1570);
        public ValLerp nearAngle = new ValLerp(178,260,1,0.9);
    }
    public static AdaptiveFireConfig adaptiveFireConfig = new AdaptiveFireConfig();

    public static class GunConfig {
        public double velTol = 60;
        public double patternFireDelay = 0.7;
        public double shootVelSidePattern = 1860;
        public double shootVelSideFar = 1840;
        public double shootVelSideGoalNear = 1450;

        public double lightPower = 1;

        public PidStatus rightPidStatus = new PidStatus(0.005, 0, 0, 0.00041, 0, 0, 0,10);
        public PidStatus leftPidStatus = new PidStatus(0.005, 0, 0, 0.00039, 0, 0, 0,10);
        public PidStatus centerPidStatus = new PidStatus(0.005, 0, 0, 0.00043, 0, 0, 0,10);
    }
    public static GunConfig gunConfig = new GunConfig();

}
