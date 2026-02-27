package org.woen.Hardware.Factory;

import com.acmerobotics.dashboard.FtcDashboard;

import org.woen.Hardware.DevicePool.Devices.Motor.MotorConfig;
import org.woen.Hardware.DevicePool.Devices.Odometers.OdometerConf;
import org.woen.Telemetry.ConfigurableVariables.SimpleProvider;

public class DeviceActivationConfig {

    public final SimpleProvider<Boolean> odometers = new SimpleProvider<>(true);
    public final SimpleProvider<Boolean> motors    = new SimpleProvider<>(true);
    public final SimpleProvider<Boolean> servos    = new SimpleProvider<>(true);
    public final SimpleProvider<Boolean> rev       = new SimpleProvider<>(true);
    public final MotorConfig motorConfig     = new MotorConfig();
    public final OdometerConf odometerConfig = new OdometerConf();


    private void initConfigs(){
        FtcDashboard.getInstance().addConfigVariable("DeviceActivation", "odometers", odometers);
        FtcDashboard.getInstance().addConfigVariable("DeviceActivation", "motors",    motors);
        FtcDashboard.getInstance().addConfigVariable("DeviceActivation", "servos",    servos);
        FtcDashboard.getInstance().addConfigVariable("DeviceActivation", "rev",       rev);

        motorConfig.init();
        odometerConfig.init();
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
        return config;
    }

    public static DeviceActivationConfig getManual(){
        DeviceActivationConfig config = new DeviceActivationConfig();
        config.initConfigs();
        config.motorConfig.init();
        config.odometerConfig.init();
        return config;
    }
}
