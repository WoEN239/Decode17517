package org.woen.OpModes.Main;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.woen.Architecture.EventBus.EventBus;
import org.woen.Hardware.ActivationConfig.DeviceActivationConfig;
import org.woen.RobotModule.Factory.ModulesActivateConfig;
import org.woen.RobotModule.Modules.Gun.Arcitecture.NewGunCommandAvailable;
import org.woen.RobotModule.Modules.Gun.GUN_COMMAND;
import org.woen.RobotModule.Modules.TrajectoryFollower.Arcitecture.Feedforward.FeedforwardReference;
import org.woen.RobotModule.Modules.TrajectoryFollower.Arcitecture.Feedforward.FeedforwardReferenceObserver;
import org.woen.RobotModule.Modules.TrajectoryFollower.Interface.TrajectoryFollower;
import org.woen.Telemetry.Telemetry;
import org.woen.Util.Vectors.Pose;

@TeleOp(name = "teleop")
public class TeleOpMode extends BaseOpMode{
    private final FeedforwardReferenceObserver velocityObserver = new FeedforwardReferenceObserver();


    @Override
    protected void initConfig(){
        DeviceActivationConfig devConfig =  DeviceActivationConfig.getAllOn();
        devConfig.servos.set(true);
        deviceActivationConfig = devConfig;

        ModulesActivateConfig modConfig = ModulesActivateConfig.getAllOn();
        modConfig.driveTrain.trajectoryFollower.set(false);
        modConfig.gun.set(true);

        modulesActivationConfig = modConfig;

    }

    @Override
    protected void modulesReplace(){
        robot.getFactory().replace(TrajectoryFollower.class, new TrajectoryFollower(){});
    }

    @Override
    protected void loopRun() {

        velocityObserver.notifyListeners(new FeedforwardReference(new Pose(
                gamepad1.left_stick_x*7,gamepad1.right_trigger*150-gamepad1.left_trigger*150, 100
        ),new Pose(0,0,0)));

        Telemetry.getInstance().add("triangle",gamepad1.triangle);

        Telemetry.getInstance().add("modules",robot.getFactory().getModules().toString());

        if(gamepad1.triangle){
            EventBus.getInstance().invoke(new NewGunCommandAvailable(GUN_COMMAND.TARGET));
        }

        if(gamepad1.dpad_up){
            EventBus.getInstance().invoke(new NewGunCommandAvailable(GUN_COMMAND.FULL_FIRE));
        }
        if(gamepad1.dpad_left){
            EventBus.getInstance().invoke(new NewGunCommandAvailable(GUN_COMMAND.SHOT_LEFT));
        }
        if(gamepad1.dpad_right){
            EventBus.getInstance().invoke(new NewGunCommandAvailable(GUN_COMMAND.SHOT_RIGHT));
        }
        if(gamepad1.dpad_down){
            EventBus.getInstance().invoke(new NewGunCommandAvailable(GUN_COMMAND.SHOT_CENTER));
        }

    }

}
