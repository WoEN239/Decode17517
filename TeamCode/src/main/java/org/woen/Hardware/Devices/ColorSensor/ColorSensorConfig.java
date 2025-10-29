package org.woen.Hardware.Devices.ColorSensor;

import com.acmerobotics.dashboard.FtcDashboard;

import org.woen.Telemetry.ConfigurableVariables.Provider;

public class ColorSensorConfig {

    public final Provider<Integer> green = new Provider<>(0);
    public final Provider<Integer> blue = new Provider<>(0);
    public final Provider<Integer> red = new Provider<>(0);

    public void init(){
        FtcDashboard.getInstance().addConfigVariable("ColorSesnorConfig", "green", green);
        FtcDashboard.getInstance().addConfigVariable("ColorSesnorConfig", "blue", blue);
        FtcDashboard.getInstance().addConfigVariable("ColorSesnorConfig", "red", red);
    }
}
