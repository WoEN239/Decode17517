package org.woen.Config;

import com.acmerobotics.dashboard.config.Config;

import org.woen.Util.Pid.PidStatus;

@Config
public class ControlSystemConstant {

    public static class FeedbackConfig {
        public double PPLocalR   = 50;
        public double PPTransVel = 30;
        public PidStatus xPid = new PidStatus(0.,0,0.00,0,0,0,0);
        public PidStatus hPid = new PidStatus(1.5,10,0.05,0,0,0.02,0.05,0.005);

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
        public double kFarAngle = 0.001969;
        public double kNearAngle = 0.003150;
        public double kNearVel   = 2.0833;
        public double lowAngleFar  = 0.58;
        public double lowAngleNear = 0;
        public double lowVelNear   = 1420;
        public double lowVelFar    = 1840;
        public double hiDistNear   =  264;
        public double hiDistFar = 406.5;
        public double lowDistNear  = 178;
        public double lowDistFar = 330;
    }
    public static AdaptiveFireConfig adaptiveFireConfig = new AdaptiveFireConfig();

    public static class GunConfig {
        public double velTol = 60;
        public double patternFireDelay = 1;


        public double shootVelSidePattern = 1860;
        public double shootVelSideFar = 1840;
        public double shootVelSideNear = 1600;

        public double shootVelCPattern = 1860;
        public double shootVelCFar = 1840;
        public double shootVelCNear = 1600;

        public double shootVelCGoalNear = 1450;

        public double shootVelSideGoalNear = 1450;

        public double adaptiveDeltaPos = 0;
        public double distLow = 186;
        public double distHi = 268;

        public double lightPower = 0.7;

        public PidStatus rightPidStatus = new PidStatus(0.001, 0, 0, 0.00041, 0, 0, 0,10);
        public PidStatus leftPidStatus = new PidStatus(0.001, 0, 0, 0.00039, 0, 0, 0,10);
        public PidStatus centerPidStatus = new PidStatus(0.001, 0, 0, 0.00043, 0, 0, 0,10);
    }
    public static GunConfig gunConfig = new GunConfig();

}
