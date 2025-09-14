package org.woen.OpModes.ConfigBuild;

import org.woen.OpModes.Main.BaseOpMode;
import org.woen.RobotModule.Factory.ModulesActivateConfig;

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
