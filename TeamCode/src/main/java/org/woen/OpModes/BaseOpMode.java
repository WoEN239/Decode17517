package org.woen.OpModes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.woen.Architecture.EventBus.EventBus;
import org.woen.Config.MatchData;
import org.woen.Hardware.Factory.DeviceActivationConfig;
import org.woen.Hardware.DevicePool.DevicePool;
import org.woen.RobotModule.Modules.Localizer.Architecture.RegisterNewPositionListener;
import org.woen.RobotModule.Modules.Localizer.Architecture.RegisterNewVelocityListener;
import org.woen.RobotModule.Robot;
import org.woen.RobotModule.Factory.ModulesActivateConfig;
import org.woen.Util.Vectors.Pose;

public abstract class BaseOpMode extends LinearOpMode {
    protected Robot robot;
    protected static DeviceActivationConfig deviceActivationConfig = DeviceActivationConfig.getAllOn();
    protected static ModulesActivateConfig modulesActivationConfig = ModulesActivateConfig.getAllOn();

    protected Pose pose = new Pose(0,0,0);
    private void setPose(Pose p){pose = p;}

    protected Pose velocity = new Pose(0,0,0);
    private void setVelocity(Pose p){velocity = p;}

    private void initOpMode() {
        DevicePool.getInstance().init(hardwareMap,deviceActivationConfig);
    }
    protected void initConfig(){}
    protected void modulesReplace(){}
    protected void initRun(){}
    protected void lastRun(){}
    protected abstract void loopRun();

    private void robotInit(){
        robot = new Robot();
        robot.factoryInit(modulesActivationConfig);
        robot.modulesInit();
        EventBus.getListenersRegistration().invoke(new RegisterNewPositionListener(this::setPose));
        EventBus.getListenersRegistration().invoke(new RegisterNewVelocityListener(this::setVelocity));
    }


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
        EventBus.getInstance().invoke(new EndOfOpModeEvent());
        MatchData.setStartPose(pose);
    }
}
