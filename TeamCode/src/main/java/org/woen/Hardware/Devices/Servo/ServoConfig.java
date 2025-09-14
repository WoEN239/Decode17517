package org.woen.Hardware.Devices.Servo;

import com.acmerobotics.dashboard.FtcDashboard;

import org.woen.Telemetry.ConfigurableVariables.Provider;

public class ServoConfig {
    public final Provider<Double> acceleration = new Provider<>(0d);
    public final Provider<Double> maxVel = new Provider<>(0d);
    public final Provider<Double> target = new Provider<>(0d);
    public final Provider<Double> t = new Provider<>(0d);

    public void init(){
        FtcDashboard.getInstance().addConfigVariable("ServorConfig", "acceleration", acceleration);
        FtcDashboard.getInstance().addConfigVariable("ServorConfig", "maxVel", maxVel);
        FtcDashboard.getInstance().addConfigVariable("ServorConfig", "target", target);
        FtcDashboard.getInstance().addConfigVariable("ServorConfig", "t", t);

    }
}
