package org.woen.OpModes.Test;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.woen.Architecture.EventBus.EventBus;
import org.woen.Autonom.AutonomTask;
import org.woen.Autonom.SetNewTrajectoryEvent;
import org.woen.Autonom.WayPoint;
import org.woen.Hardware.ActivationConfig.DeviceActivationConfig;
import org.woen.OpModes.Main.BaseOpMode;
import org.woen.RobotModule.Factory.ModulesActivateConfig;
import org.woen.Util.Vectors.Pose;

@TeleOp(name = "pure_pursuit_test")
public class PurePursuitTest extends BaseOpMode {

    @Override
    protected void initConfig(){
        DeviceActivationConfig devConfig =  DeviceActivationConfig.getAllOn();
        devConfig.servos.set(true);
        devConfig.colorSensor.set(false);
        deviceActivationConfig = devConfig;

        ModulesActivateConfig modConfig = ModulesActivateConfig.getAllOn();
        modConfig.driveTrain.trajectoryFollower.set(true);
        modConfig.gun.set(true);
        modConfig.autonomTaskManager.set(true);

        modulesActivationConfig = modConfig;

    }

    @Override
    public void firstRun(){
        EventBus.getInstance().invoke(new SetNewTrajectoryEvent(
                new WayPoint(AutonomTask.Stub,new Pose(0,0,0)),
                new WayPoint(AutonomTask.Stub,new Pose(0,50,0)),
                new WayPoint(new AutonomTask(()->false),new Pose(0,50,50))
        ));
    }

    @Override
    protected void loopRun() {
    }

}
