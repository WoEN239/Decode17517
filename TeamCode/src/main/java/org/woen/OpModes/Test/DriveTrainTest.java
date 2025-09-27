package org.woen.OpModes.Test;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.woen.Hardware.ActivationConfig.DeviceActivationConfig;
import org.woen.OpModes.Main.BaseOpMode;
import org.woen.RobotModule.Factory.ModulesActivateConfig;

@TeleOp(name = "drive_train_test")
public class DriveTrainTest extends BaseOpMode {
    @Override
    protected void initConfig(){
        deviceActivationConfig = DeviceActivationConfig.getAllOn();
        ModulesActivateConfig modules = ModulesActivateConfig.getAllOn();
        modules.driveTrain.trajectoryFollower.set(false);
        modulesActivationConfig = modules;
    }

    @Override
    protected void loopRun() {

    }
}
