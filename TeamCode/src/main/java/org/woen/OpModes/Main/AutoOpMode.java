package org.woen.OpModes.Main;

import static org.woen.RobotModule.Modules.Camera.Enums.MOTIF.PGP;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.woen.Architecture.EventBus.EventBus;
import org.woen.Autonom.Pools.Far15BallPool;
import org.woen.Autonom.Pools.Far9Pattern3Ball;
import org.woen.Autonom.Pools.HeaveyBallsAuto;
import org.woen.Autonom.Pools.Near9Pattern6Ball;
import org.woen.Autonom.Pools.Far9PatternPool;
import org.woen.Autonom.Pools.WayPointPool;
import org.woen.Autonom.Structure.SetNewWaypointsSequenceEvent;
import org.woen.Config.MatchData;
import org.woen.Hardware.Factory.DeviceActivationConfig;
import org.woen.OpModes.BaseOpMode;
import org.woen.RobotModule.Factory.ModulesActivateConfig;
import org.woen.RobotModule.Modules.Camera.Enums.MOTIF;
import org.woen.RobotModule.Modules.Camera.Events.NewTargetMotifEvent;

@Autonomous
public class AutoOpMode extends BaseOpMode {
    @Override
    protected void initConfig(){
        DeviceActivationConfig devConfig =  DeviceActivationConfig.getAllOn();
        deviceActivationConfig = devConfig;

        ModulesActivateConfig modConfig = ModulesActivateConfig.getAllOn();
        modConfig.camera.set(true);
        modulesActivationConfig = modConfig;

        MatchData.setStartPose(MatchData.start.pose);

        EventBus.getInstance().subscribe(NewTargetMotifEvent.class,this::setMotif);
    }
    private void setMotif(NewTargetMotifEvent e){
        motif = e.getData();
    }
    private MOTIF motif = PGP;
    @Override
    protected void initRun() {
        WayPointPool pool = null;
        if(MatchData.auto.equals("far9pattern")){
            pool = new Far9PatternPool();
        } else if (MatchData.auto.equals("far15ball")) {
            pool = new Far15BallPool();
        } else if (MatchData.auto.equals("far9pattern3ball")) {
            pool = new Far9Pattern3Ball();
        } else if (MatchData.auto.equals("near9pattern6ball")) {
            pool = new Near9Pattern6Ball();
        } else if (MatchData.auto.equals("heaveyballsauto")) {
            pool = new HeaveyBallsAuto();
        }
        EventBus.getInstance().invoke(new SetNewWaypointsSequenceEvent(
                pool.getPool()
        ));
    }

    protected void loopRun() {
        telemetry.addData("target motif",motif);
        telemetry.update();
    }

    @Override
    protected void lastRun() {}
}
