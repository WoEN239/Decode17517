package org.woen.OpModes.Main;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.woen.Architecture.EventBus.EventBus;
import org.woen.Hardware.ActivationConfig.DeviceActivationConfig;
import org.woen.RobotModule.Factory.ModulesActivateConfig;
import org.woen.RobotModule.Modules.DriveTrain.DriveTrain.FeedbackController.ReplaceFeedbackControllerEvent;
import org.woen.RobotModule.Modules.DriveTrain.DriveTrain.FeedbackController.TankFeedbackController;
import org.woen.RobotModule.Modules.Gun.Arcitecture.NewGunCommandAvailable;
import org.woen.RobotModule.Modules.Gun.Config.GUN_COMMAND;
import org.woen.RobotModule.Modules.TrajectoryFollower.Arcitecture.Feedback.FeedbackReference;
import org.woen.RobotModule.Modules.TrajectoryFollower.Arcitecture.Feedback.FeedbackReferenceObserver;
import org.woen.RobotModule.Modules.TrajectoryFollower.Arcitecture.Feedforward.FeedforwardReference;
import org.woen.RobotModule.Modules.TrajectoryFollower.Arcitecture.Feedforward.FeedforwardReferenceObserver;
import org.woen.RobotModule.Modules.TrajectoryFollower.Interface.TrajectoryFollower;
import org.woen.Telemetry.Telemetry;
import org.woen.Util.MotionProfile.TrapezoidMotionProfile;
import org.woen.Util.Pid.PidStatus;
import org.woen.Util.Vectors.Pose;

@Autonomous
public class AutoOpMode extends BaseOpMode{
    private final FeedforwardReferenceObserver velocityObserver = new FeedforwardReferenceObserver();
    private final FeedbackReferenceObserver positionObserver = new FeedbackReferenceObserver();

    @Override
    protected void initConfig(){
        DeviceActivationConfig devConfig =  DeviceActivationConfig.getAllOn();
        devConfig.servos.set(true);
        devConfig.colorSensor.set(true);
        deviceActivationConfig = devConfig;

        ModulesActivateConfig modConfig = ModulesActivateConfig.getAllOn();
        modConfig.driveTrain.trajectoryFollower.set(false);
        modConfig.gun.set(true);
        modConfig.autonomTaskManager.set(false);

        modulesActivationConfig = modConfig;

    }

    @Override
    protected void modulesReplace(){
        robot.getFactory().replace(TrajectoryFollower.class, new TrajectoryFollower(){});
        //EventBus.getInstance().invoke(new ReplaceFeedbackControllerEvent(new AutoOpMode.StubTankFeedback()));
    }

    ElapsedTime timer = new ElapsedTime();
    boolean isRunOnce = false;
    boolean isShotOnce = false;
    TrapezoidMotionProfile x = new TrapezoidMotionProfile(25,35,-75,0,0);
    TrapezoidMotionProfile h = new TrapezoidMotionProfile(5,3,-2.5,0,0);
    @Override
    protected void loopRun() {
        if(!isRunOnce){
            timer.reset();
            isRunOnce = true;
            EventBus.getInstance().invoke(new NewGunCommandAvailable(GUN_COMMAND.TARGET));
        }

        if(timer.seconds()<x.duration+3){
            positionObserver.notifyListeners(new FeedbackReference(
                    new Pose(0, x.getPos(timer.seconds()), 0),
                    new Pose(0, x.getVel(timer.seconds()), 0)
            ));

            velocityObserver.notifyListeners(new FeedforwardReference(
                    new Pose(0, x.getVel(timer.seconds()), 0),
                    new Pose(0, x.getAccel(timer.seconds()), 0)
            ));
        }else{
            if(!isShotOnce) {
                EventBus.getInstance().invoke(new NewGunCommandAvailable(GUN_COMMAND.FULL_FIRE));
                isShotOnce = true;
            }
        }

        Telemetry.getInstance().add("x target",x.getPos(timer.seconds()));
        Telemetry.getInstance().add("h target",h.getPos(timer.seconds()-x.duration-3));

    }

    private static class StubTankFeedback extends TankFeedbackController {
        public StubTankFeedback() {
            super(new PidStatus(0,0,0,0,0,0,0),
                    new PidStatus(0,0,0,0,0,0,0)
            );
        }
        @Override
        public Pose computeU(Pose target, Pose localPosition, Pose targetVel, Pose localVel)
        {
            return new Pose(0,0,0);
        }
    }

}
