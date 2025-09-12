package org.woen.RobotModule.Factory;

import org.woen.RobotModule.Modules.DriveTrain.ActivationConfig.DriveTrainActivationConfig;
import org.woen.RobotModule.Modules.Localizer.ActivationConfig.LocalizerActivationConfig;

public class ModulesActivateConfig {
    public LocalizerActivationConfig locolizer    = LocalizerActivationConfig.getAllOn();
    public DriveTrainActivationConfig driveTraint = DriveTrainActivationConfig.getAllOn();
}
