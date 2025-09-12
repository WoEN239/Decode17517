package org.woen.Hardware.Gyro;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;

import org.woen.Telemetry.Configs.Provider;


public class GyroConf {


    public Provider<Boolean> vel = new Provider<>(true);

    public Provider<Boolean> pos = new Provider<>(true);

    public void init(){
        FtcDashboard.getInstance().addConfigVariable("GyroConf", "vel", vel);
        FtcDashboard.getInstance().addConfigVariable("GyroConf", "pos", pos);
    }

    public void off(){
        vel.set(false);
        pos.set(false);
    }

}
