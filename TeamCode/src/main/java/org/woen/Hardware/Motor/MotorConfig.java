package org.woen.Hardware.Motor;

import com.acmerobotics.dashboard.FtcDashboard;

import org.woen.Telemetry.Configs.Provider;

public class MotorConfig{
    public Provider<Boolean> cfRightFrontMotorPos =  new Provider<>(true);
    public Provider<Boolean> cfRightBackMotorPos =  new Provider<>(true);
    public Provider<Boolean> cfLeftFrontMotorPos =  new Provider<>(true);
    public Provider<Boolean> cfLeftBackMotorPos =  new Provider<>(true);
    public Provider<Boolean> cfRightFrontMotorVol =  new Provider<>(true);
    public Provider<Boolean>  cfRightBackMotorVol =  new Provider<>(true);
    public Provider<Boolean>  cfLeftFrontMotorVol =  new Provider<>(true);
    public Provider<Boolean> cfLeftBackMotorVol =  new Provider<>(true);


    public  void initMotorConfig(){
        FtcDashboard.getInstance().addConfigVariable("MotorConfig", "rightFrontPos", cfRightFrontMotorPos);
        FtcDashboard.getInstance().addConfigVariable("MotorConfig", "rightBackPos", cfRightBackMotorPos);
        FtcDashboard.getInstance().addConfigVariable("MotorConfig", "leftFrontPos", cfLeftFrontMotorPos);
        FtcDashboard.getInstance().addConfigVariable("MotorConfig", "leftBackPos", cfLeftBackMotorPos);

        FtcDashboard.getInstance().addConfigVariable("MotorConfig", "rightFrontVol", cfRightFrontMotorVol);
        FtcDashboard.getInstance().addConfigVariable("MotorConfig", "rightBackVol", cfRightBackMotorVol);
        FtcDashboard.getInstance().addConfigVariable("MotorConfig", "leftFrontVol", cfLeftFrontMotorVol);
        FtcDashboard.getInstance().addConfigVariable("MotorConfig", "leftBackVol", cfLeftBackMotorVol);
    }

    public void off(){
        cfRightFrontMotorPos.set(false);
        cfRightBackMotorPos.set(false);
        cfLeftFrontMotorPos.set(false);
        cfLeftBackMotorPos.set(false);
        cfRightFrontMotorVol.set(false);
        cfRightBackMotorVol.set(false);
        cfLeftFrontMotorVol.set(false);
        cfLeftBackMotorVol.set(false);
    }

}
