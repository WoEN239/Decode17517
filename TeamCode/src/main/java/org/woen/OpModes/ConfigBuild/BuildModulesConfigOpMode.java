package org.woen.OpModes.ConfigBuild;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.woen.OpModes.BaseOpMode;
import org.woen.RobotModule.Factory.ModulesActivateConfig;

@TeleOp(name = "build modules",group = "test")
public class BuildModulesConfigOpMode extends BaseOpMode {
    @Override
    protected void loopRun() {

    }
    ModulesActivateConfig config = ModulesActivateConfig.getManual();
    @Override
    public void runOpMode() {
        waitForStart();
        while (opModeIsActive()){
            modulesActivationConfig = config;
        }
    }
}
