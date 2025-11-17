package org.woen.OpModes.Main;

import static java.lang.Math.PI;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.woen.Architecture.EventBus.EventBus;
import org.woen.Autonom.AutonomTask;
import org.woen.Autonom.SetNewTrajectoryEvent;
import org.woen.Autonom.WayPoint;
import org.woen.Config.MatchData;
import org.woen.Config.Team;
import org.woen.Hardware.ActivationConfig.DeviceActivationConfig;
import org.woen.RobotModule.Factory.ModulesActivateConfig;
import org.woen.RobotModule.Modules.Camera.MOTIF;
import org.woen.RobotModule.Modules.Camera.NewMotifEvent;
import org.woen.RobotModule.Modules.DriveTrain.DriveTrain.FeedbackController.ReplaceFeedbackControllerEvent;
import org.woen.RobotModule.Modules.DriveTrain.DriveTrain.FeedbackController.TankFeedbackController;
import org.woen.RobotModule.Modules.Gun.Arcitecture.NewAimCommandAvaliable;
import org.woen.RobotModule.Modules.Gun.Arcitecture.NewGunCommandAvailable;
import org.woen.RobotModule.Modules.Gun.Config.GUN_COMMAND;
import org.woen.RobotModule.Modules.Localizer.Position.Architecture.RegisterNewLocalPositionListener;
import org.woen.RobotModule.Modules.TrajectoryFollower.Arcitecture.Feedback.FeedbackReference;
import org.woen.RobotModule.Modules.TrajectoryFollower.Arcitecture.Feedback.FeedbackReferenceObserver;
import org.woen.RobotModule.Modules.TrajectoryFollower.Arcitecture.Feedforward.FeedforwardReference;
import org.woen.RobotModule.Modules.TrajectoryFollower.Arcitecture.Feedforward.FeedforwardReferenceObserver;
import org.woen.RobotModule.Modules.TrajectoryFollower.Interface.TrajectoryFollower;
import org.woen.Telemetry.Telemetry;
import org.woen.Util.MotionProfile.TrapezoidMotionProfile;
import org.woen.Util.Pid.PidStatus;
import org.woen.Util.Vectors.Pose;
import org.woen.Util.Vectors.Vector2d;

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
    double firstAngle = 0.715*PI;

    Pose shoot = new Pose(0,-60,0);
    Pose startEat = new Pose(firstAngle,-10,0);
    Pose endEat   =  new Pose(firstAngle,
            new Vector2d(-10,0).plus(new Vector2d(40,0).rotate(PI*0.25)));

    @Override
    protected void firstRun(){
        EventBus.getInstance().invoke(new NewGunCommandAvailable(GUN_COMMAND.TARGET));
        EventBus.getListenersRegistration().invoke(new RegisterNewLocalPositionListener(this::setPose));
        EventBus.getInstance().invoke(new NewAimCommandAvaliable(true));

        EventBus.getInstance().invoke(new SetNewTrajectoryEvent(
                new WayPoint(AutonomTask.Stub,new Pose(0,0,0)),
                new WayPoint(
                        new AutonomTask(
                                ()->gunStatus==GUN_COMMAND.EAT,()->setGunCommand(GUN_COMMAND.PATTERN_FIRE)
                        ),
                        shoot,true
                ),
                new WayPoint(
                        new AutonomTask(()->Math.abs(pose.h-firstAngle)<0.015),
                        new Pose(0,0,40)
                )

        ));

        if(MatchData.team == Team.RED){
        EventBus.getInstance().invoke(new SetNewTrajectoryEvent(
                new WayPoint(AutonomTask.Stub,new Pose(0,0,0)),
                new WayPoint(
                        new AutonomTask(
                                ()->gunStatus==GUN_COMMAND.EAT,()->setGunCommand(GUN_COMMAND.PATTERN_FIRE)
                        ),
                        shoot.teamReverse(),true
                ),
                new WayPoint(
                        new AutonomTask(()->Math.abs(pose.h-firstAngle)<0.015),
                        startEat.teamReverse()
                ),
                new WayPoint(
                        new AutonomTask(()->true,()->setGunCommand(GUN_COMMAND.TARGET)),
                        endEat.teamReverse()
                ).setVel(40) ,
                new WayPoint(
                        new AutonomTask(()->Math.abs(pose.h)<0.015),
                        startEat.teamReverse(),
                        true
                ),
                new WayPoint(
                        new AutonomTask(
                                ()->gunStatus==GUN_COMMAND.EAT,()->setGunCommand(GUN_COMMAND.PATTERN_FIRE)
                        ),
                        shoot.teamReverse(),true
                )
        ));

        }
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
