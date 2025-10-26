package org.woen.RobotModule.Factory;

import com.acmerobotics.dashboard.FtcDashboard;

import org.woen.RobotModule.Modules.DriveTrain.ActivationConfig.DriveTrainActivationConfig;
import org.woen.RobotModule.Modules.Localizer.ActivationConfig.LocalizerActivationConfig;
import org.woen.Telemetry.ConfigurableVariables.Provider;

public class ModulesActivateConfig {
    public LocalizerActivationConfig localizer = LocalizerActivationConfig.getAllOn();
    public DriveTrainActivationConfig driveTrain = DriveTrainActivationConfig.getAllOn();
    public Provider<Boolean> gun = new Provider<>(true);

    public static ModulesActivateConfig getAllOn(){
        return new ModulesActivateConfig();
    }

    public static ModulesActivateConfig getAllOff(){
        ModulesActivateConfig config = new ModulesActivateConfig();
        config.localizer = LocalizerActivationConfig.getAllOff();
        config.driveTrain = DriveTrainActivationConfig.getAllOff();
        config.gun.set(false);
        return config;
    }

    public static ModulesActivateConfig getManual(){
        ModulesActivateConfig config = new ModulesActivateConfig();
        config.localizer = LocalizerActivationConfig.getManual();
        config.driveTrain = DriveTrainActivationConfig.getManual();
        FtcDashboard.getInstance().addConfigVariable("Gun activation","gun",config.gun);
        return config;
    }


}
