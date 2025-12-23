package org.woen.OpModes.Main;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpModeManagerImpl;

import org.firstinspires.ftc.robotcore.internal.system.AppUtil;
import org.woen.Architecture.EventBus.EventBus;
import org.woen.Autonom.Pools.WaypointPoolNear;
import org.woen.Autonom.SetNewWaypointsSequenceEvent;
import org.woen.Autonom.Pools.WaypointPoolFar;
import org.woen.Config.MatchData;
import org.woen.Config.Start;
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
    protected void initRun() {
        EventBus.getInstance().invoke(new NewAimEvent(true).setGoal(new Vector2d(-180, -170)));


        if(MatchData.start == Start.FAR_BLUE || MatchData.start == Start.FAR_RED){
            WaypointPoolFar poolFar = new WaypointPoolFar();
                EventBus.getInstance().invoke(new SetNewWaypointsSequenceEvent(
                    poolFar.firstAim.copy(),
                    poolFar.fire1.copy(),
                    poolFar.rotate1.copy(),
                    poolFar.firstEat.copy(),
                    poolFar.secondAim.copy(),
                    poolFar.fire2.copy(),
                    poolFar.rotate2.copy(),
                    poolFar.secondEat.copy(),
                    poolFar.thirdAim.copy(),
                    poolFar.fire3.copy()
            ));
        }

        if(MatchData.start == Start.NEAR_BLUE || MatchData.start == Start.NEAR_RED){
            WaypointPoolNear poolNear = new WaypointPoolNear();
                EventBus.getInstance().invoke(new SetNewWaypointsSequenceEvent(
                        poolNear.look.copy(),
                        poolNear.firstAim.copy(),
                        poolNear.fire1.copy(),
                        poolNear.firstEat.copy(),
                        poolNear.secondAim.copy(),
                        poolNear.fire2.copy(),
                        poolNear.secondEat.copy(),
                        poolNear.thirdAim.copy(),
                        poolNear.fire3.copy()
                ));
        }
    }

    protected void loopRun() {}

    @Override
    protected void lastRun() {
        OpModeManagerImpl.getOpModeManagerOfActivity(AppUtil.getInstance().getActivity()).initOpMode("teleop");
    }
}
