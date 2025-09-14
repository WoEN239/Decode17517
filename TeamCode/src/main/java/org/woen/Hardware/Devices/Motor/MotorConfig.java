package org.woen.Hardware.Devices.Motor;

import com.acmerobotics.dashboard.FtcDashboard;

import org.woen.Telemetry.Configs.Provider;

public class MotorConfig{
    public final Provider<Double>  rightFrontPos = new Provider<>(0d);
    public final Provider<Double>  rightBackPos  = new Provider<>(0d);
    public final Provider<Double>  leftFrontPos  = new Provider<>(0d);
    public final Provider<Double>  leftBackPos   = new Provider<>(0d);

    public final Provider<Double>  rightFrontVol = new Provider<>(0d);
    public final Provider<Double>  rightBackVol  = new Provider<>(0d);
    public final Provider<Double>  leftFrontVol  = new Provider<>(0d);
    public final Provider<Double>  leftBackVol   = new Provider<>(0d);

    public void init(){
        FtcDashboard.getInstance().addConfigVariable("MotorConfig", "rightFrontPos",rightFrontPos);
        FtcDashboard.getInstance().addConfigVariable("MotorConfig", "rightBackPos",  rightBackPos);
        FtcDashboard.getInstance().addConfigVariable("MotorConfig", "leftFrontPos",  leftFrontPos);
        FtcDashboard.getInstance().addConfigVariable("MotorConfig", "leftBackPos",    leftBackPos);

        FtcDashboard.getInstance().addConfigVariable("MotorConfig", "rightFrontVol", rightFrontVol);
        FtcDashboard.getInstance().addConfigVariable("MotorConfig", "rightBackVol",   rightBackVol);
        FtcDashboard.getInstance().addConfigVariable("MotorConfig", "leftFrontVol",   leftFrontVol);
        FtcDashboard.getInstance().addConfigVariable("MotorConfig", "leftBackVol",     leftBackVol);
    }

}
