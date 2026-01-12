package org.woen.OpModes.Main;

import static org.woen.RobotModule.Modules.Camera.Enums.MOTIF.PGP;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.woen.Architecture.EventBus.EventBus;
import org.woen.Autonom.Pools.Far9PatternPool;
import org.woen.Autonom.Structure.SetNewWaypointsSequenceEvent;
import org.woen.Config.MatchData;
import org.woen.Config.Start;
import org.woen.Hardware.Factory.DeviceActivationConfig;
import org.woen.OpModes.BaseOpMode;
import org.woen.RobotModule.Factory.ModulesActivateConfig;
import org.woen.RobotModule.Modules.Camera.Enums.MOTIF;
import org.woen.RobotModule.Modules.Camera.Events.NewTargetMotifEvent;
import org.woen.RobotModule.Modules.Gun.Arcitecture.NewAimEvent;

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
    protected void initRun() {}

    protected void loopRun() {
        telemetry.addData("target motif",motif);
        telemetry.update();
    }

    @Override
    protected void lastRun() {}
}
