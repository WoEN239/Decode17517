package org.woen.Hardware.ActivationConfig;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;

import org.woen.Hardware.Devices.ColorSensor.ColorSensorConfig;
import org.woen.Hardware.Devices.Motor.MotorConfig;
import org.woen.Hardware.Devices.Odometers.OdometerConf;
import org.woen.Telemetry.ConfigurableVariables.Provider;
import org.woen.Telemetry.Telemetry;

public class DeviceActivationConfig {

    public final Provider<Boolean> odometers = new Provider<>(true);
    public final Provider<Boolean> motors    = new Provider<>(true);
    public final Provider<Boolean> servos    = new Provider<>(true);
    public final Provider<Boolean> rev       = new Provider<>(true);
    public final MotorConfig motorConfig     = new MotorConfig();
    public final OdometerConf odometerConfig = new OdometerConf();

    public final Provider<Boolean> colorSensor    = new Provider<>(true);

    public final ColorSensorConfig colorSensorConfig = new ColorSensorConfig();


    private void initConfigs(){
        FtcDashboard.getInstance().addConfigVariable("DeviceActivation", "odometers", odometers);
        FtcDashboard.getInstance().addConfigVariable("DeviceActivation", "motors",    motors);
        FtcDashboard.getInstance().addConfigVariable("DeviceActivation", "servos",    servos);
        FtcDashboard.getInstance().addConfigVariable("DeviceActivation", "rev",       rev);

        motorConfig.init();
        odometerConfig.init();
        colorSensorConfig.init();

    }

    public static DeviceActivationConfig getAllOn(){
        return new DeviceActivationConfig();
    }

    public static DeviceActivationConfig getAllOff(){
        DeviceActivationConfig config = new DeviceActivationConfig();
        config.motors     .set(false);
        config.servos     .set(false);
        config.odometers  .set(false);
        config.rev        .set(false);
        config.colorSensor.set(false);
        return config;
    }

    public static DeviceActivationConfig getManual(){
        DeviceActivationConfig config = new DeviceActivationConfig();
        config.initConfigs();
        config.motorConfig.init();
        config.odometerConfig.init();
        config.colorSensorConfig.init();
        return config;
    }

    public DeviceActivationConfig() {
    }
}
