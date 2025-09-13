package org.woen.RobotModule.Modules.Localizer.ActivationConfig;


import com.acmerobotics.dashboard.FtcDashboard;

import org.woen.Telemetry.Configs.Provider;

public class LocalizerActivationConfig {
    public final Provider<Boolean> device   = new Provider<>(true);
    public final Provider<Boolean> velocity = new Provider<>(true);
    public final Provider<Boolean> position = new Provider<>(true);

    private void initConfigs(){
        FtcDashboard.getInstance().addConfigVariable("LocalizerActivation", "device", device);
        FtcDashboard.getInstance().addConfigVariable("LocalizerActivation", "position", position);
        FtcDashboard.getInstance().addConfigVariable("LocalizerActivation", "velocity",velocity );
    }

    public static LocalizerActivationConfig getAllOn(){
        return new LocalizerActivationConfig();
    }

    public static LocalizerActivationConfig getAllOff(){
        LocalizerActivationConfig config = new LocalizerActivationConfig();
        config.device  .set(false);
        config.velocity.set(false);
        config.position.set(false);
        return config;
    }
    public static LocalizerActivationConfig geManual(){
        LocalizerActivationConfig config = new LocalizerActivationConfig();
        config.initConfigs();
        return config;
    }
}
