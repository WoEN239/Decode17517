package org.woen.OpModes.Main;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.woen.Hardware.ActivationConfig.DeviceActivationConfig;
import org.woen.Hardware.DevicePool.DevicePool;
import org.woen.Robot.Robot;
import org.woen.RobotModule.Factory.ModulesActivateConfig;
import org.woen.Telemetry.Telemetry;

public abstract class BaseOpMode extends LinearOpMode {
    protected Robot robot;
    protected static DeviceActivationConfig deviceActivationConfig = DeviceActivationConfig.getAllOn();
    protected static ModulesActivateConfig modulesActivationConfig = ModulesActivateConfig.getAllOn();

    private void initOpMode(){
        DevicePool.getInstance().init(hardwareMap,deviceActivationConfig);
    }

    protected void initConfig(){}
    protected void modulesReplace(){}

    private void robotInit(){
        robot = new Robot();
        robot.factoryInit(modulesActivationConfig);
        robot.modulesInit();
    }

    protected abstract void loopRun();

    @Override
    public void runOpMode() {
        initConfig();
        initOpMode();
        robotInit();
        modulesReplace();
        waitForStart();
        while (opModeIsActive()) {
            loopRun();
            robot.update();
        }
    }
}
