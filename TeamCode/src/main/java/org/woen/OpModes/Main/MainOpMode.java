package org.woen.OpModes.Main;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.woen.Hardware.DeviceActivationConfig.DeviceActivationConfig;
import org.woen.Hardware.DevicePool.DevicePool;
import org.woen.Hardware.Factories.HardwareFactory;
import org.woen.Robot.Robot;
import org.woen.RobotModule.Factory.ModulesActivateConfig;

public abstract class MainOpMode extends LinearOpMode {
    protected Robot robot;
    protected static DeviceActivationConfig deviceActivationConfig = new DeviceActivationConfig();
    protected static ModulesActivateConfig modulesActivateConfig   = new ModulesActivateConfig();

    private void initOpMode(){
        DevicePool.getInstance().init(new HardwareFactory(hardwareMap,deviceActivationConfig));
    }

    private void robotInit(){
        robot = new Robot();
        robot.factoryInit(modulesActivateConfig);
        robot.modulesInit();
    }

    protected abstract void loopRun();

    @Override
    public void runOpMode() {
        initOpMode();
        robotInit();
        waitForStart();
        while (opModeIsActive()) {
            loopRun();
            robot.update();
        }
    }
}
