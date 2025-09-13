package org.woen.RobotModule.Factory;

import org.woen.RobotModule.Modules.DriveTrain.ActivationConfig.DriveTrainActivationConfig;
import org.woen.RobotModule.Modules.Localizer.ActivationConfig.LocalizerActivationConfig;

public class ModulesActivateConfig {
    public LocalizerActivationConfig localizer = LocalizerActivationConfig.getAllOn();
    public DriveTrainActivationConfig driveTrain = DriveTrainActivationConfig.getAllOn();
    public static ModulesActivateConfig getAllOn(){
        ModulesActivateConfig config = new ModulesActivateConfig();
        config.localizer = LocalizerActivationConfig.getAllOn();
        config.driveTrain = DriveTrainActivationConfig.getAllOn();
        return config;
    }
}
