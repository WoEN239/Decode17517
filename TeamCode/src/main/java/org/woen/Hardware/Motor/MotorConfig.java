package org.woen.Hardware.Motor;

import com.acmerobotics.dashboard.FtcDashboard;

import org.woen.Telemetry.Configs.Provider;

public class MotorConfig{
    public final Provider<Double> cfRightFrontMotorPos =  new Provider<>(0d);
    public final Provider<Double> cfRightBackMotorPos  =  new Provider<>(0d);
    public final Provider<Double> cfLeftFrontMotorPos  =  new Provider<>(0d);
    public final Provider<Double> cfLeftBackMotorPos   =  new Provider<>(0d);
    public final Provider<Double> cfRightFrontMotorVol =  new Provider<>(0d);
    public final Provider<Double> cfRightBackMotorVol  =  new Provider<>(0d);
    public final Provider<Double> cfLeftFrontMotorVol  =  new Provider<>(0d);
    public final Provider<Double> cfLeftBackMotorVol   =  new Provider<>(0d);

    public void initMotorConfig(){
        FtcDashboard.getInstance().addConfigVariable("MotorConfig", "rightFrontPos", cfRightFrontMotorPos);
        FtcDashboard.getInstance().addConfigVariable("MotorConfig", "rightBackPos", cfRightBackMotorPos);
        FtcDashboard.getInstance().addConfigVariable("MotorConfig", "leftFrontPos", cfLeftFrontMotorPos);
        FtcDashboard.getInstance().addConfigVariable("MotorConfig", "leftBackPos", cfLeftBackMotorPos);

        FtcDashboard.getInstance().addConfigVariable("MotorConfig", "rightFrontVol", cfRightFrontMotorVol);
        FtcDashboard.getInstance().addConfigVariable("MotorConfig", "rightBackVol", cfRightBackMotorVol);
        FtcDashboard.getInstance().addConfigVariable("MotorConfig", "leftFrontVol", cfLeftFrontMotorVol);
        FtcDashboard.getInstance().addConfigVariable("MotorConfig", "leftBackVol", cfLeftBackMotorVol);
    }

}
