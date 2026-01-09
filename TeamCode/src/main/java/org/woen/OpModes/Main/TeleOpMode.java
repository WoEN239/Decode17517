package org.woen.OpModes.Main;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.woen.Architecture.EventBus.EventBus;
import org.woen.Config.MatchData;
import org.woen.Hardware.Factory.DeviceActivationConfig;
import org.woen.Hardware.DevicePool.DevicePool;
import org.woen.OpModes.BaseOpMode;
import org.woen.RobotModule.Factory.ModulesActivateConfig;
import org.woen.RobotModule.Modules.DriveTrain.DriveTrain.FeedbackController.ReplaceFeedbackControllerEvent;
import org.woen.RobotModule.Modules.DriveTrain.DriveTrain.FeedbackController.TankFeedbackController;
import org.woen.RobotModule.Modules.Gun.Arcitecture.NewAimEvent;
import org.woen.RobotModule.Modules.Gun.Arcitecture.NewBrushReversEvent;
import org.woen.RobotModule.Modules.Gun.Arcitecture.NewGunCommandAvailable;
import org.woen.RobotModule.Modules.Gun.Config.GUN_COMMAND;
import org.woen.RobotModule.Modules.Gun.Config.GunServoPositions;
import org.woen.RobotModule.Modules.TrajectoryFollower.Arcitecture.Feedforward.FeedforwardReference;
import org.woen.RobotModule.Modules.TrajectoryFollower.Arcitecture.Feedforward.FeedforwardReferenceObserver;
import org.woen.RobotModule.Modules.TrajectoryFollower.Interface.TrajectoryFollower;
import org.woen.Util.Pid.Pid;
import org.woen.Util.Pid.PidStatus;
import org.woen.Util.Vectors.Pose;

@TeleOp(name = "teleOp")
public class TeleOpMode extends BaseOpMode {
    private final FeedforwardReferenceObserver feedforwardReferenceObserver = new FeedforwardReferenceObserver();

    @Override
    protected void initConfig() {
        DeviceActivationConfig devConfig = DeviceActivationConfig.getAllOn();
        devConfig.servos.set(true);
        devConfig.odometers.set(true);
        devConfig.motors.set(true);
        deviceActivationConfig = devConfig;

        ModulesActivateConfig modConfig = ModulesActivateConfig.getAllOn();
        modConfig.driveTrain.trajectoryFollower.set(false);
        modConfig.driveTrain.voltageController.set(true);
        modConfig.gun.set(true);
        modConfig.camera.set(true);
        modConfig.autonomTaskManager.set(false);
        modulesActivationConfig = modConfig;
    }

    @Override
    protected void modulesReplace() {
        robot.getFactory().replace(TrajectoryFollower.class, new TrajectoryFollower() {});
        EventBus.getInstance().invoke(new ReplaceFeedbackControllerEvent(new TeleOpFeedback()));
    }

    private final BorderButton hiAimButt = new BorderButton();
    private final BorderButton dirReverseButt = new BorderButton();
    private final BorderButton brushReverseButt = new BorderButton();
    private final BorderButton brushReverseButt1 = new BorderButton();
    private final BorderButton ptoButt = new BorderButton();
    private final BorderButton fireButt = new BorderButton();
    private final BorderButton patternFireButt = new BorderButton();
    private final BorderButton purpleFireButt = new BorderButton();
    private final BorderButton greenFireButt = new BorderButton();
    private final BorderButton cancelFireButt = new BorderButton();

    private boolean isFarAim = false;
    private boolean isPtoActive = false;
    private int dir = 1;

    private Pose targetVelocity = new Pose(0,0,0);
    @Override
    protected void loopRun() {
        targetVelocity = new Pose(
                -      (gamepad1.right_stick_x * Math.abs(gamepad1.right_stick_x)) * 8,
                -dir * (gamepad1.left_stick_y * Math.abs(gamepad1.left_stick_y)    * 180),
                0
        );

        feedforwardReferenceObserver.notifyListeners(new FeedforwardReference(targetVelocity, new Pose(0, 0, 0)));

        angleToControl = Math.PI + MatchData.team.goalPose.minus(pose.vector).getAngle();


        if(brushReverseButt.get(gamepad1.left_trigger>0.1)){
            EventBus.getInstance().invoke(new NewBrushReversEvent(true));
        }
        if(brushReverseButt1.get( !(gamepad1.left_trigger>0.1) )){
            EventBus.getInstance().invoke(new NewBrushReversEvent(false));
        }

        if (dirReverseButt.get(gamepad1.left_bumper)) {
            dir = -dir;
        }

        if (hiAimButt.get(gamepad1.dpad_up)) {
            isFarAim = !isFarAim;
            EventBus.getInstance().invoke(new NewAimEvent(isFarAim));
        }

        isAngleControl = gamepad1.right_bumper;

        if (fireButt.get(gamepad1.cross)) {
            EventBus.getInstance().invoke(new NewGunCommandAvailable(GUN_COMMAND.FULL_FIRE));
            isAngleControl = false;
        }

        if (patternFireButt.get(gamepad1.dpad_right)) {
            EventBus.getInstance().invoke(new NewGunCommandAvailable(GUN_COMMAND.PATTERN_FIRE));
        }

        if(greenFireButt.get(gamepad1.triangle)){
            EventBus.getInstance().invoke(new NewGunCommandAvailable(GUN_COMMAND.G_FIRE));
        }

        if(purpleFireButt.get(gamepad1.square)){
            EventBus.getInstance().invoke(new NewGunCommandAvailable(GUN_COMMAND.P_FIRE));
        }

        if(cancelFireButt.get(gamepad1.right_trigger>0.1)){
            EventBus.getInstance().invoke(new NewGunCommandAvailable(GUN_COMMAND.EAT));
        }

        if(gamepad1.circleWasPressed()){
            EventBus.getInstance().invoke(new NewGunCommandAvailable(GUN_COMMAND.PATTERN_FIRE));
        }


        if (ptoButt.get(gamepad1.ps)) {
            isPtoActive = !isPtoActive;
            if(isPtoActive) {
                DevicePool.getInstance().ptoL.setPos(GunServoPositions.ptoLClose);
                DevicePool.getInstance().ptoR.setPos(GunServoPositions.ptoRClose);
                EventBus.getInstance().invoke(new NewGunCommandAvailable(GUN_COMMAND.OFF));
            }else{
                DevicePool.getInstance().ptoL.setPos(GunServoPositions.ptoLOpen);
                DevicePool.getInstance().ptoR.setPos(GunServoPositions.ptoROpen);
                EventBus.getInstance().invoke(new NewGunCommandAvailable(GUN_COMMAND.EAT));
            }
        }


    }

    @Override
    protected void initRun() {
        DevicePool.getInstance().ptoL.setPos(GunServoPositions.ptoLOpen);
        DevicePool.getInstance().ptoR.setPos(GunServoPositions.ptoROpen);
    }

    private double angleToControl = 0;
    private boolean isAngleControl = false;
    private class TeleOpFeedback extends TankFeedbackController {
        public TeleOpFeedback() {
            super(new PidStatus(0, 0, 0, 0, 0, 0, 0),
                  new PidStatus(0, 0, 0, 0, 0, 0, 0)
            );
        }
        PidStatus anglePidStatus = new PidStatus(1.5, 10, 0.05, 0, 0, 0.02, 0);
        Pid anglePid = new Pid(anglePidStatus);
        {
            anglePid.isNormolized = true;
            anglePid.isDAccessible = false;
        }
        PidStatus velPidStatus = new PidStatus(0., 0, 0., 0, 0, 0, 0);
        Pid velPid = new Pid(velPidStatus);
        {
            velPid.isNormolized = false;
            velPid.isDAccessible = false;
        }

        @Override
        public Pose computeU(Pose p1, Pose p2, Pose p3, Pose p4) {
            if(isAngleControl) {
                anglePid.setTarget(angleToControl);
                anglePid.setPos(pose.h);
                anglePid.update();
                return new Pose(anglePid.getU(), 0, 0);
            } else {
                velPid.setTarget(targetVelocity.h);
                velPid.setPos(velocity.h);
                velPid.update();
                return new Pose(velPid.getU(), 0, 0);
            }
        }
    }
}
class BorderButton{
    private boolean old = false;

    public boolean get(boolean button) {
        boolean indicator = (button != old) && button;
        old = button;
        return indicator;
    }

}

