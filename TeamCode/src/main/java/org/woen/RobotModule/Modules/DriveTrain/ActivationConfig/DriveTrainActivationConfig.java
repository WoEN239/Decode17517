package org.woen.RobotModule.Modules.DriveTrain.ActivationConfig;

import com.acmerobotics.dashboard.FtcDashboard;

import org.woen.Telemetry.Configs.Provider;

public class DriveTrainActivationConfig {

    public final Provider<Boolean> driveTrain         = new Provider<>(true);
    public final Provider<Boolean> voltageController  = new Provider<>(true);
    public final Provider<Boolean> trajectoryFollower = new Provider<>(true);

    private void initConfigs(){
        FtcDashboard.getInstance().addConfigVariable("DriveTrainActivation", "driveTrain", driveTrain);
        FtcDashboard.getInstance().addConfigVariable("DriveTrainActivation", "voltageController",voltageController);
        FtcDashboard.getInstance().addConfigVariable("DriveTrainActivation", "trajectoryFollower",trajectoryFollower);
    }

    public static DriveTrainActivationConfig getAllOn(){
        return new DriveTrainActivationConfig();
    }

    public static DriveTrainActivationConfig getAllOff(){
        DriveTrainActivationConfig config = new DriveTrainActivationConfig();
        config.driveTrain        .set(false);
        config.trajectoryFollower.set(false);
        config.voltageController .set(false);
        return config;
    }

    public static DriveTrainActivationConfig manual(){
        DriveTrainActivationConfig config = new DriveTrainActivationConfig();
        config.initConfigs();
        return config;
    }

}
