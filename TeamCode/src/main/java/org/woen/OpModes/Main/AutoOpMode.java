package org.woen.OpModes.Main;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpModeManagerImpl;

import org.firstinspires.ftc.robotcore.internal.system.AppUtil;
import org.woen.Architecture.EventBus.EventBus;
import org.woen.Autonom.SetNewWaypointsSequenceEvent;
import org.woen.Autonom.WaypointPool;
import org.woen.Hardware.ActivationConfig.DeviceActivationConfig;
import org.woen.RobotModule.Factory.ModulesActivateConfig;
import org.woen.RobotModule.Modules.Gun.Arcitecture.NewAimEvent;
import org.woen.Util.Vectors.Vector2d;

@Autonomous
public class AutoOpMode extends BaseOpMode{

    @Override
    protected void initConfig(){
        DeviceActivationConfig devConfig =  DeviceActivationConfig.getAllOn();
        devConfig.colorSensor.set(false);
        deviceActivationConfig = devConfig;

        ModulesActivateConfig modConfig = ModulesActivateConfig.getAllOn();
        modConfig.camera.set(false);
        modulesActivationConfig = modConfig;
    }

    @Override
    protected void initRun(){
        EventBus.getInstance().invoke(new NewAimEvent(true).setGoal( new Vector2d(-180,-170)));

        WaypointPool pool = new WaypointPool();

        EventBus.getInstance().invoke(new SetNewWaypointsSequenceEvent(
                pool.firstAim.copy(),
                pool.fire1.copy(),
                pool.rotate.copy(),
                pool.firstEat.copy(),
                pool.secondAim.copy(),
                pool.fire2.copy(),
                pool.rotate.copy(),
                pool.secondEat.copy(),
                pool.secondAim.copy(),
                pool.fire3.copy()
        ));
    }

    protected void loopRun() {}

    @Override
    protected void lastRun() {
        OpModeManagerImpl.getOpModeManagerOfActivity(AppUtil.getInstance().getActivity()).initOpMode("teleop");
    }
}
