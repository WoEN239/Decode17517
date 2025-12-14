package org.woen.Config;

import com.acmerobotics.dashboard.config.Config;

import org.woen.Util.Pid.PidStatus;

@Config
public class ControlSystemConstant {

    public static class FeedbackConfig {
        public double PPLocalR   = 15;
        public double PPTransVel = 30;
        public PidStatus xPid = new PidStatus(0.,0,0.00,0,0,0,0);
        public PidStatus hPid = new PidStatus(1,0,0.,0,0,0.0,0);
    }
    public static FeedbackConfig feedbackConfig = new FeedbackConfig();

    public static class RobotSizeConfig{ //sm
        public double lx = 6.3;
        public double ly = 16.4375;
        public double B = 35.925;
        public double wheelR = 55;
    }
    public static RobotSizeConfig robotSizeConfig = new RobotSizeConfig();

    public static class FeedforwardConfig{
        public Double xFeedforwardKV = 2.2d;
        public Double xFeedforwardKA = 0.2d;
        public Double hSlip = 1.5d;
        public Double staticVoltageOffsetX = 1.5d;
        public Double staticVoltageOffsetH = 2.1d;
    }
    public static FeedforwardConfig feedforwardConfig = new FeedforwardConfig();

    public static class KinematicConstrainConfig {
        public Double transAccel = 15d;
        public Double angleAccel = 2d;
        public Double transVel = 15d;
        public Double angleVel = 2d;

    }
    public static KinematicConstrainConfig kinematicConstrainConfig = new KinematicConstrainConfig();

    public static class OdometerConstantConfig {
        public double meterPerAngle = 0.29;
        public double y_odometer_radius = 0;
    }
    public static OdometerConstantConfig odometerConstantConfig = new OdometerConstantConfig();

    public static class GunConfig {
        public double delay = 1.5;
        public double velTol = 30;
        public double shootVel = 1450;
        public double eatVel = 1000;
        public double patternShootVel = 1300;
        public PidStatus rightPidStatus = new PidStatus(1, 0, 0, 0.0004, 0, 0, 0,50);
        public PidStatus leftPidStatus = new PidStatus(1, 0, 0, 0.0004, 0, 0, 0,50);
        public PidStatus centerPidStatus = new PidStatus(1, 0, 0, 0.0004, 0, 0, 0,50);
    }
    public static GunConfig gunConfig = new GunConfig();

}
