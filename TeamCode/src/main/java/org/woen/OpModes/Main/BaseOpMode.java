package org.woen.OpModes.Main;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.ServoController;

import org.woen.Hardware.ActivationConfig.DeviceActivationConfig;
import org.woen.Hardware.DevicePool.DevicePool;
import org.woen.Robot.Robot;
import org.woen.RobotModule.Factory.ModulesActivateConfig;

public abstract class BaseOpMode extends LinearOpMode {
    protected Robot robot;
    protected static DeviceActivationConfig deviceActivationConfig = DeviceActivationConfig.getAllOn();
    protected static ModulesActivateConfig modulesActivationConfig = ModulesActivateConfig.getAllOn();

    private void initOpMode() {
        DevicePool.getInstance().init(hardwareMap,deviceActivationConfig);
    }

    protected void initConfig(){}
    protected void modulesReplace(){}
    protected void initRun(){}
    protected void lastRun(){}

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
        initRun();
        waitForStart();
        while (opModeIsActive()) {
            loopRun();
            robot.update();
        }
        lastRun();
    }
}
