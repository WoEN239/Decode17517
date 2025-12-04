package org.woen.Config;

import com.acmerobotics.dashboard.config.Config;

import org.woen.Util.Pid.PidStatus;

@Config
public class ControlSystemConstant {

    public static class FeedbackConfig {
        public double PPLocalR   = 15;
        public double PPTransVel = 30;
        public PidStatus xPid = new PidStatus(0.15,0,0.001,0,0,0,0);
        public PidStatus hPid = new PidStatus(1.5,0,0.5,0,0,0.0,0);
    }
    public static FeedbackConfig feedbackConfig = new FeedbackConfig();

    public static class RobotSizeConfig{ //sm
        public double lx = 6.3;
        public double ly = 16.4375;
        public double B = 35.925;
        public double wheelR = 75;
    }
    public static RobotSizeConfig robotSizeConfig = new RobotSizeConfig();

    public static class FeedforwardConfig{
        public Double xFeedforwardKV = 10.8d;
        public Double xFeedforwardKA = 1.7d;
        public Double hSlip = 1.065d;
        public Double staticVoltageOffsetX = 1.1d;
        public Double staticVoltageOffsetH = 3d;
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
        public double shootVel = 1300;
        public double eatVel = 1000;
        public double patternShootVel = 1100;
    }
    public static GunConfig gunConfig = new GunConfig();

}
