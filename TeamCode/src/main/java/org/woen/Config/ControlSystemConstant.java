package org.woen.Config;

import com.acmerobotics.dashboard.config.Config;

import org.woen.Util.Pid.PidStatus;

@Config
public class ControlSystemConstant {

    public static class FeedbackConfig {
        public double PPLocalR   = 15;
        public double PPTransVel = 30;
        public PidStatus xPid = new PidStatus(0.,0,0.00,0,0,0,0);
        public PidStatus hPid = new PidStatus(2.5,5,0.2,0,0,0.1,0);
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
        public Double xFeedforwardKA = 0.4d;
        public Double xFeedforwardKAReverse = 0.4d;
        public Double xFeedforwardKV = 2.8d;
        public Double hSlip = 1.15d;
        public Double staticVoltageOffsetX = 1.d;
        public Double staticVoltageOffsetH = 2.1d;
    }
    public static FeedforwardConfig feedforwardConfig = new FeedforwardConfig();
    
    public static class OdometerConstantConfig {
        public double meterPerAngle = 0.29;
        public double y_odometer_radius = 0;
    }
    public static OdometerConstantConfig odometerConstantConfig = new OdometerConstantConfig();

    public static class GunConfig {
        public double velTol = 30;

        public double shootVelSide = 2000;
        public double shootVelSideNear = 2000;

        public double shootVelC = 1800;
        public double shootVelCNear = 1600;

        public double deltaPosC = 0.2;
        public double deltaPosS = 0.2;

        public double distLow = 50;
        public double distHi = 60;

        public PidStatus rightPidStatus = new PidStatus(0.001, 0, 0, 0.00037, 0, 0, 0,40);
        public PidStatus leftPidStatus = new PidStatus(0.001, 0, 0, 0.00042, 0, 0, 0,40);
        public PidStatus centerPidStatus = new PidStatus(0.001, 0, 0, 0.00046, 0, 0, 0,40);
    }
    public static GunConfig gunConfig = new GunConfig();

}
