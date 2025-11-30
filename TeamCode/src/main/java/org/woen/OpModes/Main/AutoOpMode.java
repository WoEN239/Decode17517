package org.woen.OpModes.Main;

import static java.lang.Math.abs;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.woen.Architecture.EventBus.EventBus;
import org.woen.Autonom.AutonomTask;
import org.woen.Autonom.PositionPool;
import org.woen.Autonom.SetNewTrajectoryEvent;
import org.woen.Autonom.WayPoint;
import org.woen.Config.MatchData;
import org.woen.Hardware.ActivationConfig.DeviceActivationConfig;
import org.woen.RobotModule.Factory.ModulesActivateConfig;
import org.woen.RobotModule.Modules.Camera.MOTIF;
import org.woen.RobotModule.Modules.Camera.NewMotifEvent;
import org.woen.RobotModule.Modules.Gun.Arcitecture.NewAimCommandAvaliable;
import org.woen.RobotModule.Modules.Gun.Arcitecture.NewGunCommandAvailable;
import org.woen.RobotModule.Modules.Gun.Config.GUN_COMMAND;
import org.woen.RobotModule.Modules.Localizer.Position.Architecture.RegisterNewLocalPositionListener;
import org.woen.RobotModule.Modules.TrajectoryFollower.Arcitecture.Feedback.FeedbackReferenceObserver;
import org.woen.RobotModule.Modules.TrajectoryFollower.Arcitecture.Feedforward.FeedforwardReferenceObserver;
import org.woen.Util.Vectors.Pose;

@Autonomous
public class AutoOpMode extends BaseOpMode{
    private final FeedforwardReferenceObserver velocityObserver = new FeedforwardReferenceObserver();
    private final FeedbackReferenceObserver positionObserver = new FeedbackReferenceObserver();

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
    @Override
    protected void firstRun(){
        EventBus.getInstance().invoke(new NewGunCommandAvailable(GUN_COMMAND.TARGET));
        EventBus.getListenersRegistration().invoke(new RegisterNewLocalPositionListener(this::setPose));
        EventBus.getInstance().invoke(new NewAimCommandAvaliable(true));

        EventBus.getInstance().invoke(new SetNewTrajectoryEvent(
                new WayPoint(AutonomTask.Stub,pool.goal),
                new WayPoint(
                        new AutonomTask(
                                ()->gunStatus==GUN_COMMAND.EAT,()->setGunCommand(GUN_COMMAND.PATTERN_FIRE)
                        ),
                        true,pool.shoot
                ),
                new WayPoint(
                        new AutonomTask(()->true,()->setGunCommand(GUN_COMMAND.TARGET)),
                        pool.firstEat
                ),
                new WayPoint(
                        new AutonomTask(
                                ()->gunStatus==GUN_COMMAND.EAT,()->setGunCommand(GUN_COMMAND.PATTERN_FIRE)
                        ),
                        true,pool.shoot
                )));
    }

    MOTIF motif = MOTIF.PGP;

    public void setMotif(NewMotifEvent e) {
        this.motif = e.getData();
    }

    protected void loopRun() {
        telemetry.addData("motif",motif.toString());
        telemetry.update();
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
