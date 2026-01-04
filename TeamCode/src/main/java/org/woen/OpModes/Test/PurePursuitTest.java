package org.woen.OpModes.Test;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.woen.Architecture.EventBus.EventBus;
import org.woen.Autonom.Structure.AutonomTask;
import org.woen.Autonom.Structure.SetNewWaypointsSequenceEvent;
import org.woen.Autonom.Structure.WayPoint;
import org.woen.Hardware.Factory.DeviceActivationConfig;
import org.woen.OpModes.BaseOpMode;
import org.woen.RobotModule.Factory.ModulesActivateConfig;
import org.woen.Util.Vectors.Pose;
@Disabled
@TeleOp(name = "pure_pursuit_test")
public class PurePursuitTest extends BaseOpMode {

    @Override
    protected void initConfig(){
        DeviceActivationConfig devConfig =  DeviceActivationConfig.getAllOn();
        devConfig.servos.set(true);
        deviceActivationConfig = devConfig;

        ModulesActivateConfig modConfig = ModulesActivateConfig.getAllOn();
        modConfig.driveTrain.trajectoryFollower.set(true);
        modConfig.gun.set(true);
        modConfig.autonomTaskManager.set(true);

        modulesActivationConfig = modConfig;

    }

    @Override
    public void initRun(){
        EventBus.getInstance().invoke(new SetNewWaypointsSequenceEvent(
                new WayPoint(AutonomTask.Stub,new Pose(0,0,0)),
                new WayPoint(AutonomTask.Stub,new Pose(0,100,0),new Pose(0,100,100)).setVel(20)
        ));
    }

    @Override
    protected void loopRun() {
    }

}
