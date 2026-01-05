package org.woen.OpModes.Main;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpModeManagerImpl;

import org.firstinspires.ftc.robotcore.internal.system.AppUtil;
import org.woen.Architecture.EventBus.EventBus;
import org.woen.Autonom.Pools.WaypointPoolNear;
import org.woen.Autonom.Structure.SetNewWaypointsSequenceEvent;
import org.woen.Autonom.Pools.WaypointPoolFar;
import org.woen.Config.MatchData;
import org.woen.Config.Start;
import org.woen.Hardware.Factory.DeviceActivationConfig;
import org.woen.OpModes.BaseOpMode;
import org.woen.RobotModule.Factory.ModulesActivateConfig;
import org.woen.RobotModule.Modules.Gun.Arcitecture.NewAimEvent;

@Autonomous
public class AutoOpMode extends BaseOpMode {
    @Override
    protected void initConfig(){
        DeviceActivationConfig devConfig =  DeviceActivationConfig.getAllOn();
        deviceActivationConfig = devConfig;

        ModulesActivateConfig modConfig = ModulesActivateConfig.getAllOn();
        modConfig.camera.set(false);
        modulesActivationConfig = modConfig;

        MatchData.setStartPose(MatchData.start.pose);
    }

    @Override
    protected void initRun() {

        if(MatchData.start == Start.FAR_BLUE || MatchData.start == Start.FAR_RED){
            EventBus.getInstance().invoke(new NewAimEvent(true));
            WaypointPoolFar poolFar = new WaypointPoolFar();
            EventBus.getInstance().invoke(new SetNewWaypointsSequenceEvent(
                poolFar.firstAim.copy().setVel(70),
                poolFar.fire1.copy(),
                poolFar.rotate1.copy(),
                poolFar.firstEat.copy().setVel(120),
                poolFar.secondAim.copy().setVel(80),
                poolFar.fire2.copy(),
                poolFar.rotate2.copy(),
                poolFar.secondEat.copy().setVel(120),
                poolFar.thirdAim.copy().setVel(80),
                poolFar.fire3.copy(),
                poolFar.rotate3.copy(),
                poolFar.thirdEat.copy().setVel(150),
                poolFar.thirdEatRotate.copy(),
                poolFar.forthAim.copy().setVel(110),
                poolFar.fire4.copy(),
                poolFar.rotate4.copy(),
                poolFar.forthEat.copy().setVel(150),
                poolFar.fiveAim.copy().setVel(110),
                poolFar.fire5.copy(),
                poolFar.park.copy()
        ));
        }

        if(MatchData.start == Start.NEAR_BLUE || MatchData.start == Start.NEAR_RED){
            EventBus.getInstance().invoke(new NewAimEvent(false));
            WaypointPoolNear poolNear = new WaypointPoolNear();
            EventBus.getInstance().invoke(new SetNewWaypointsSequenceEvent(
                    poolNear.aim1.copy(),
                    poolNear.fire1.copy(),
                    poolNear.rotate1.copy(),
                    poolNear.eat1.copy().setVel(150),
                    poolNear.aim2.copy().setVel(100),
                    poolNear.fire2.copy(),
                    poolNear.rotate2.copy(),
                    poolNear.eat2.copy().setVel(150),
                    poolNear.aim3.copy().setVel(150),
                    poolNear.fire3.copy(),
                    poolNear.rotate3.copy(),
                    poolNear.eat3.copy().setVel(150),
                    poolNear.aim4.copy().setVel(150),
                    poolNear.fire4.copy(),
                    poolNear.rotate4.copy(),
                    poolNear.eat4.copy().setVel(150),
                    poolNear.aim5.copy().setVel(150),
                    poolNear.fire5.copy(),
                    poolNear.park.copy().setVel(220)

            ));
        }
    }

    protected void loopRun() {}

    @Override
    protected void lastRun() {
        OpModeManagerImpl.getOpModeManagerOfActivity(AppUtil.getInstance().getActivity()).initOpMode("teleOp");
    }
}
