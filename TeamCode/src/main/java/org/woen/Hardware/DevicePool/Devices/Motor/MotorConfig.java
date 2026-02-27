package org.woen.Hardware.DevicePool.Devices.Motor;

import com.acmerobotics.dashboard.FtcDashboard;

import org.woen.Telemetry.ConfigurableVariables.SimpleProvider;

public class MotorConfig{
    public final SimpleProvider<Double> rightFrontPos = new SimpleProvider<>(0d);
    public final SimpleProvider<Double> rightBackPos  = new SimpleProvider<>(0d);
    public final SimpleProvider<Double> leftFrontPos  = new SimpleProvider<>(0d);
    public final SimpleProvider<Double> leftBackPos   = new SimpleProvider<>(0d);

    public final SimpleProvider<Double> rightFrontVol = new SimpleProvider<>(0d);
    public final SimpleProvider<Double> rightBackVol  = new SimpleProvider<>(0d);
    public final SimpleProvider<Double> leftFrontVol  = new SimpleProvider<>(0d);
    public final SimpleProvider<Double> leftBackVol   = new SimpleProvider<>(0d);

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
