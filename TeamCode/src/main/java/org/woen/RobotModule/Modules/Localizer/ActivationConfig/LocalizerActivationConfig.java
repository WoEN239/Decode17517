package org.woen.RobotModule.Modules.Localizer.ActivationConfig;


import com.acmerobotics.dashboard.FtcDashboard;

import org.woen.Telemetry.ConfigurableVariables.SimpleProvider;

public class LocalizerActivationConfig {
    public final SimpleProvider<Boolean> velocity = new SimpleProvider<>(true);
    public final SimpleProvider<Boolean> position = new SimpleProvider<>(true);

    private void initConfigs(){
        FtcDashboard.getInstance().addConfigVariable("LocalizerActivation", "position", position);
        FtcDashboard.getInstance().addConfigVariable("LocalizerActivation", "velocity",velocity );
    }

    public static LocalizerActivationConfig getAllOn(){
        return new LocalizerActivationConfig();
    }

    public static LocalizerActivationConfig getAllOff(){
        LocalizerActivationConfig config = new LocalizerActivationConfig();
        config.velocity.set(false);
        config.position.set(false);
        return config;
    }

    public static LocalizerActivationConfig getManual(){
        LocalizerActivationConfig config = LocalizerActivationConfig.getAllOff();
        config.initConfigs();
        return config;
    }
}
