package org.woen.Hardware.DeviceActivationConfig;

import com.acmerobotics.dashboard.FtcDashboard;
import org.woen.Telemetry.Configs.Provider;

public class DeviceActivationConfig {

    public final Provider<Boolean> odometers = new Provider<>(true);
    public final Provider<Boolean> motors    = new Provider<>(true);
    public final Provider<Boolean> gyro      = new Provider<>(true);

    private void initConfigs(){
        FtcDashboard.getInstance().addConfigVariable("Odometers", "TurnOff/On", odometers);
        FtcDashboard.getInstance().addConfigVariable("Motors", "TurnOff/On", motors);
        FtcDashboard.getInstance().addConfigVariable("Gyro", "TurnOff/On", gyro);
    }

    public static DeviceActivationConfig getAllOn(){
        return new DeviceActivationConfig();
    }

    public static DeviceActivationConfig getAllOff(){
        DeviceActivationConfig config = new DeviceActivationConfig();
        config.motors   .set(false);
        config.odometers.set(false);
        config.gyro     .set(false);
        return config;
    }
    public static DeviceActivationConfig manual(){
        DeviceActivationConfig config = new DeviceActivationConfig();
        config.initConfigs();
        return config;
    }

    public DeviceActivationConfig() {
    }
}
