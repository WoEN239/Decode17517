package org.woen.OpModes.Main;

import static java.lang.Math.PI;
import static java.lang.Math.abs;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.RobotLog;

import org.woen.Architecture.EventBus.EventBus;
import org.woen.Autonom.AutonomTask;
import org.woen.Autonom.PositionPool;
import org.woen.Autonom.SetNewWaypointsSequenceEvent;
import org.woen.Autonom.WayPoint;
import org.woen.Config.MatchData;
import org.woen.Hardware.ActivationConfig.DeviceActivationConfig;
import org.woen.RobotModule.Factory.ModulesActivateConfig;
import org.woen.RobotModule.Modules.Camera.MOTIF;
import org.woen.RobotModule.Modules.Camera.NewMotifEvent;
import org.woen.RobotModule.Modules.Gun.Arcitecture.NewAimEvent;
import org.woen.RobotModule.Modules.Gun.Arcitecture.NewGunCommandAvailable;
import org.woen.RobotModule.Modules.Gun.Config.GUN_COMMAND;
import org.woen.RobotModule.Modules.Localizer.Position.Architecture.RegisterNewLocalPositionListener;
import org.woen.RobotModule.Modules.Localizer.Position.Architecture.RegisterNewPositionListener;
import org.woen.Telemetry.Telemetry;
import org.woen.Util.Angel.AngleUtil;
import org.woen.Util.Vectors.Pose;
import org.woen.Util.Vectors.Vector2d;

@Autonomous
public class AutoOpMode extends BaseOpMode{

    @Override
    protected void initConfig(){
        DeviceActivationConfig devConfig =  DeviceActivationConfig.getAllOn();
        devConfig.servos.set(true);
        devConfig.colorSensor.set(false);
        deviceActivationConfig = devConfig;

        ModulesActivateConfig modConfig = ModulesActivateConfig.getAllOn();
        modConfig.driveTrain.trajectoryFollower.set(true);
        modConfig.gun.set(true);
        modConfig.camera.set(false);
        modConfig.autonomTaskManager.set(true);

        modulesActivationConfig = modConfig;

        EventBus.getInstance().subscribe(NewGunCommandAvailable.class,this::setGunStatus);
        EventBus.getInstance().subscribe(NewMotifEvent.class,this::setMotif);
    }

    PositionPool pool = new PositionPool();
    ElapsedTime timer = new ElapsedTime();
    @Override
    protected void initRun(){
        timer.reset();
        EventBus.getInstance().invoke(new NewGunCommandAvailable(GUN_COMMAND.TARGET));
        EventBus.getListenersRegistration().invoke(new RegisterNewPositionListener(this::setPose));
        EventBus.getInstance().invoke(new NewAimEvent(true).setGoal( new Vector2d(-180,-170)));


        EventBus.getInstance().invoke(new SetNewWaypointsSequenceEvent(
        new WayPoint(
                         new AutonomTask(()->abs(pose.h-angleToGoal())<0.015),
                        true,
                        MatchData.startPosition,new Pose(angleToGoal(),100,-57))
                .setName("start"),
                new WayPoint(
                        new AutonomTask(()->true,
                                ()->RobotLog.dd("auto","fire")),
                        new Pose(angleToGoal(),150,-57),new Pose(angleToGoal(),150,-57)
                )
                .setName("fire"),
                new WayPoint(
                        new AutonomTask(()->true,
                                ()->RobotLog.dd("auto","eat")),
                        new Pose(angleToGoal(),150,-57),
                        new Pose(angleToGoal(),140,-57),
                        new Pose(angleToGoal(),140,-80)
                )
        ));

    }

    MOTIF motif = MOTIF.PGP;

    public void setMotif(NewMotifEvent e) {
        this.motif = e.getData();
    }
    private double angleToGoal(){
        return  PI + new Vector2d(-180,-170).minus(pose.vector).getAngle();
    }
    boolean firstRun = true;
    protected void loopRun() {
        if(firstRun) {timer.reset();}
        firstRun = false;

        telemetry.addData("motif",motif.toString());
        telemetry.update();
        Telemetry.getInstance().add("angle err", AngleUtil.normalize(pose.h - 15));
        Telemetry.getInstance().add("auto time", timer.seconds());

    }

    private Pose pose = MatchData.startPosition;
    private void setPose(Pose pose) {this.pose = pose;}

    private GUN_COMMAND gunStatus = GUN_COMMAND.TARGET;

    private void setGunStatus(NewGunCommandAvailable e) {
        this.gunStatus = e.getData();
    }

    private void setGunCommand(GUN_COMMAND command){
        EventBus.getInstance().invoke(new NewGunCommandAvailable(command));
    }

}
