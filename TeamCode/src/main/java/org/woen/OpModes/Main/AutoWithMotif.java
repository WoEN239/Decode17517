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

@Autonomous
public class AutoWithMotif extends BaseOpMode{

    @Override
    protected void initConfig(){
        DeviceActivationConfig devConfig =  DeviceActivationConfig.getAllOn();
        devConfig.colorSensor.set(false);
        deviceActivationConfig = devConfig;

        ModulesActivateConfig modConfig = ModulesActivateConfig.getAllOn();
        modConfig.camera.set(true);
        modulesActivationConfig = modConfig;
    }

    @Override
    protected void initRun() {


        if(MatchData.start == Start.FAR_BLUE || MatchData.start == Start.FAR_RED){
            EventBus.getInstance().invoke(new NewAimEvent(true).setGoal(MatchData.team.goalPose));
            WaypointPoolFar poolFar = new WaypointPoolFar();
            EventBus.getInstance().invoke(new SetNewWaypointsSequenceEvent(
                    poolFar.firstAim.copy().setVel(70),
                    poolFar.fire1Pat.copy(),
                    poolFar.rotate1.copy(),
                    poolFar.firstEat.copy().setVel(80),
                    poolFar.secondAim.copy().setVel(80),
                    poolFar.fire2Pat.copy(),
                    poolFar.rotate2.copy(),
                    poolFar.secondEat.copy().setVel(80).setEndDetect(20),
                    poolFar.thirdAim.copy().setVel(80),
                    poolFar.fire3Pat.copy(),
                    poolFar.park.copy()
            ));
        }

        if(MatchData.start == Start.NEAR_BLUE || MatchData.start == Start.NEAR_RED){
            EventBus.getInstance().invoke(new NewAimEvent(false).setGoal(MatchData.team.goalPose));
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
        OpModeManagerImpl.getOpModeManagerOfActivity(AppUtil.getInstance().getActivity()).startActiveOpMode();
    }
}
