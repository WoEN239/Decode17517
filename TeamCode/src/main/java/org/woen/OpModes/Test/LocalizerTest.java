package org.woen.OpModes.Test;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.woen.Hardware.ActivationConfig.DeviceActivationConfig;
import org.woen.OpModes.Main.BaseOpMode;
import org.woen.RobotModule.Factory.ModulesActivateConfig;
import org.woen.RobotModule.Modules.DriveTrain.ActivationConfig.DriveTrainActivationConfig;
import org.woen.RobotModule.Modules.Localizer.ActivationConfig.LocalizerActivationConfig;
@TeleOp(name = "localizer_test")
public class LocalizerTest extends BaseOpMode {

    @Override
    protected void initConfig(){
        DeviceActivationConfig deviceConfig = new DeviceActivationConfig();
        deviceConfig.motors.set(false);
        deviceConfig.odometers.set(true);
        deviceActivationConfig = deviceConfig;

        ModulesActivateConfig moduleConfig = new ModulesActivateConfig();
        moduleConfig.localizer = LocalizerActivationConfig.getAllOn();
        moduleConfig.driveTrain = DriveTrainActivationConfig.getAllOff();
        modulesActivationConfig = moduleConfig;
    }

    @Override
    protected void loopRun() {

    }
}
