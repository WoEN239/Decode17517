package org.woen.Hardware.DevicePool.Devices.ColorSensor;

import com.acmerobotics.dashboard.FtcDashboard;

import org.woen.Telemetry.ConfigurableVariables.SimpleProvider;

public class ColorSensorConfig {

    public final SimpleProvider<Integer> green = new SimpleProvider<>(0);
    public final SimpleProvider<Integer> blue = new SimpleProvider<>(0);
    public final SimpleProvider<Integer> red = new SimpleProvider<>(0);

    public void init(){
        FtcDashboard.getInstance().addConfigVariable("ColorSesnorConfig", "green", green);
        FtcDashboard.getInstance().addConfigVariable("ColorSesnorConfig", "blue", blue);
        FtcDashboard.getInstance().addConfigVariable("ColorSesnorConfig", "red", red);
    }
}
