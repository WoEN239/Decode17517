package org.woen.RobotModule.Factory;

import com.acmerobotics.dashboard.FtcDashboard;

import org.woen.RobotModule.Modules.DriveTrain.ActivationConfig.DriveTrainActivationConfig;
import org.woen.RobotModule.Modules.Localizer.ActivationConfig.LocalizerActivationConfig;
import org.woen.Telemetry.ConfigurableVariables.Provider;

public class ModulesActivateConfig {
    public LocalizerActivationConfig localizer = LocalizerActivationConfig.getAllOn();
    public DriveTrainActivationConfig driveTrain = DriveTrainActivationConfig.getAllOn();
    public Provider<Boolean> gun                = new Provider<>(true);
    public Provider<Boolean> camera             = new Provider<>(true);
    public Provider<Boolean> autonomTaskManager = new Provider<>(true);

    public static ModulesActivateConfig getAllOn(){
        return new ModulesActivateConfig();
    }

    public static ModulesActivateConfig getAllOff(){
        ModulesActivateConfig config = new ModulesActivateConfig();
        config.localizer = LocalizerActivationConfig.getAllOff();
        config.driveTrain = DriveTrainActivationConfig.getAllOff();
        config.gun.set(false);
        config.camera.set(false);
        config.autonomTaskManager.set(false);
        return config;
    }

    public static ModulesActivateConfig getManual(){
        ModulesActivateConfig config = new ModulesActivateConfig();
        config.localizer = LocalizerActivationConfig.getManual();
        config.driveTrain = DriveTrainActivationConfig.getManual();
        FtcDashboard.getInstance().addConfigVariable("gun activation","gun",config.gun);
        FtcDashboard.getInstance().addConfigVariable("camera activation","camera",config.camera);
        FtcDashboard.getInstance().addConfigVariable("task manager activation","task manager",config.autonomTaskManager);
        return config;
    }


}
