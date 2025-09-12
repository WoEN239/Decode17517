package org.woen.Hardware.Gyro;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;

import org.woen.Telemetry.Configs.Provider;


public class GyroConf {


    public Provider<Double> vel = new Provider<>(0d);

    public Provider<Double> pos = new Provider<>(0d);

    public void init(){
        FtcDashboard.getInstance().addConfigVariable("GyroConf", "vel", vel);
        FtcDashboard.getInstance().addConfigVariable("GyroConf", "pos", pos);
    }

    public void off(){
        vel.set(null);
        pos.set(null);
    }

}
