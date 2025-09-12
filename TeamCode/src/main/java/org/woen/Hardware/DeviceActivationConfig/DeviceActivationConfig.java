package org.woen.Hardware.DeviceActivationConfig;

import com.acmerobotics.dashboard.FtcDashboard;

import org.woen.Hardware.Gyro.GyroConf;
import org.woen.Hardware.Gyro.Impl.Gyro;
import org.woen.Hardware.Motor.MotorConfig;
import org.woen.Hardware.Odometers.OdometerConf;
import org.woen.Telemetry.Configs.Provider;

public class DeviceActivationConfig {

    public final Provider<Boolean> odometers = new Provider<>(true);
    public final Provider<Boolean> motors    = new Provider<>(true);
    public final Provider<Boolean> gyro      = new Provider<>(true);
    public final MotorConfig motorConfig = new MotorConfig();
    public final OdometerConf odometerConf = new OdometerConf();
    public final GyroConf gyroConf = new GyroConf();


    private void initConfigs(){
        FtcDashboard.getInstance().addConfigVariable("Odometers", "TurnOff/On", odometers);
        FtcDashboard.getInstance().addConfigVariable("Motors", "TurnOff/On", motors);
        FtcDashboard.getInstance().addConfigVariable("Gyro", "TurnOff/On", gyro);
        motorConfig.initMotorConfig();
        odometerConf.init();
        gyroConf.init();
    }

    public static DeviceActivationConfig getAllOn(){
        return new DeviceActivationConfig();
    }

    public static DeviceActivationConfig getAllOff(){
        DeviceActivationConfig config = new DeviceActivationConfig();
        config.motors   .set(false);
        config.odometers.set(false);
        config.gyro     .set(false);
        config.motorConfig.off();
        config.odometerConf.off();
        config.gyroConf.off();
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
