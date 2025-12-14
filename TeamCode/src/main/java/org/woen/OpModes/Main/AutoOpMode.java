package org.woen.OpModes.Main;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.util.ElapsedTime;

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
import org.woen.RobotModule.Modules.Gun.Arcitecture.NewAimCommandAvaliable;
import org.woen.RobotModule.Modules.Gun.Arcitecture.NewGunCommandAvailable;
import org.woen.RobotModule.Modules.Gun.Arcitecture.ServoActionUnit;
import org.woen.RobotModule.Modules.Gun.Config.GUN_COMMAND;
import org.woen.RobotModule.Modules.Localizer.Position.Architecture.RegisterNewLocalPositionListener;
import org.woen.RobotModule.Modules.TrajectoryFollower.Arcitecture.Feedback.FeedbackReferenceObserver;
import org.woen.RobotModule.Modules.TrajectoryFollower.Arcitecture.Feedforward.FeedforwardReferenceObserver;
import org.woen.Telemetry.Telemetry;
import org.woen.Util.Angel.AngleUtil;
import org.woen.Util.Vectors.Pose;

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
        modConfig.camera.set(true);
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
        EventBus.getListenersRegistration().invoke(new RegisterNewLocalPositionListener(this::setPose));
        EventBus.getInstance().invoke(new NewAimCommandAvaliable(true));


        EventBus.getInstance().invoke(new SetNewWaypointsSequenceEvent(
                new WayPoint(AutonomTask.Stub,pool.goal),
                new WayPoint(new AutonomTask(
                        //()->abs(AngleUtil.normalize(pose.h - pool.shoot.h)) < 0.1 )
                        ()->true)
                        ,pool.shoot),
                new WayPoint(
                        new AutonomTask(
                                ()->gunStatus==GUN_COMMAND.EAT,
                                ()->setGunCommand(GUN_COMMAND.PATTERN_FIRE)
                        ),
                        pool.shoot),
                new WayPoint(new AutonomTask(()->true)
                        ,pool.goal).setVel(40),
                new WayPoint(new AutonomTask(()->false)
                        ,pool.goal)));


    }

    MOTIF motif = MOTIF.PGP;

    public void setMotif(NewMotifEvent e) {
        this.motif = e.getData();
    }

    boolean firstRun = true;
    protected void loopRun() {
        if(firstRun) {timer.reset();}
        firstRun = false;
        telemetry.addData("motif",motif.toString());
        telemetry.update();
        Telemetry.getInstance().add("angle err", AngleUtil.normalize(pose.h - pool.shoot.h));
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
