package org.woen.OpModes.Main;

import static java.lang.Math.PI;
import static java.lang.Math.abs;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.woen.Architecture.EventBus.EventBus;
import org.woen.Autonom.Structure.AutonomTask;
import org.woen.Autonom.Structure.WayPoint;
import org.woen.Config.ControlSystemConstant;
import org.woen.Config.MatchData;
import org.woen.Config.Team;
import org.woen.Hardware.Factory.DeviceActivationConfig;
import org.woen.Hardware.DevicePool.DevicePool;
import org.woen.OpModes.BaseOpMode;
import org.woen.RobotModule.Factory.ModulesActivateConfig;
import org.woen.RobotModule.Modules.DriveTrain.DriveTrain.FeedbackController.ReplaceFeedbackControllerEvent;
import org.woen.RobotModule.Modules.DriveTrain.DriveTrain.FeedbackController.TankFeedbackController;
import org.woen.RobotModule.Modules.Gun.Arcitecture.NewAimEvent;
import org.woen.RobotModule.Modules.Gun.Arcitecture.NewBrushReversEvent;
import org.woen.RobotModule.Modules.Gun.Arcitecture.NewGunCommandAvailable;
import org.woen.RobotModule.Modules.Gun.Config.AIM_COMMAND;
import org.woen.RobotModule.Modules.Gun.Config.GUN_COMMAND;
import org.woen.RobotModule.Modules.Gun.Config.GunServoPositions;
import org.woen.RobotModule.Modules.TrajectoryFollower.Arcitecture.Feedforward.FeedforwardReference;
import org.woen.RobotModule.Modules.TrajectoryFollower.Arcitecture.Feedforward.FeedforwardReferenceObserver;
import org.woen.RobotModule.Modules.TrajectoryFollower.Arcitecture.TargetSegment.SetNewTargetTrajectorySegmentEvent;
import org.woen.Util.Angel.AngleUtil;
import org.woen.Util.Pid.Pid;
import org.woen.Util.Pid.PidStatus;
import org.woen.Util.Vectors.Pose;

@Config
@TeleOp(name = "teleOp")
public class TeleOpMode extends BaseOpMode {
    private final FeedforwardReferenceObserver feedforwardReferenceObserver = new FeedforwardReferenceObserver();

    public static PidStatus velPidStatusForward = new PidStatus(0.0141, 0, 0., 0, 0, 0, 0);
    Pid velForwardPid = new Pid(velPidStatusForward);
    {
        velForwardPid.isNormolized = false;
        velForwardPid.isDAccessible = false;
    }

    public static PidStatus velAnglePidStatus = new PidStatus(0.635, 0, 0., 0, 0, 0, 0.1,0);
    Pid velAnglePid = new Pid(velAnglePidStatus);
    {
        velAnglePid.isNormolized = false;
        velAnglePid.isDAccessible = false;
    }

    public static PidStatus anglePidStatus = new PidStatus(10, 10, 1, 0, 0, 0.2, 0.3,0.015);
    Pid anglePid = new Pid(anglePidStatus);
    {
        anglePid.isNormolized = true;
        anglePid.isDAccessible = false;
    }

    public static Pose park = new Pose(0,102,85);

    private TankFeedbackController tankFeedbackController = new TankFeedbackController(
            ControlSystemConstant.feedbackConfig.xPid,
            ControlSystemConstant.feedbackConfig.hPid,
            new PidStatus(0,0,0,0,0,0,0,0),
            new PidStatus(0,0,0,0,0,0,0,0));

    @Override
    protected void initConfig() {
        DeviceActivationConfig devConfig = DeviceActivationConfig.getAllOn();
        devConfig.servos.set(true);
        devConfig.odometers.set(true);
        devConfig.motors.set(true);
        deviceActivationConfig = devConfig;

        ModulesActivateConfig modConfig = ModulesActivateConfig.getAllOn();
        modConfig.driveTrain.trajectoryFollower.set(true);
        modConfig.driveTrain.voltageController.set(true);
        modConfig.gun.set(true);
        modConfig.camera.set(true);
        modConfig.autonomTaskManager.set(false);
        modulesActivationConfig = modConfig;

        if(MatchData.team == Team.RED){
            park = park.teamReverse();
        }
    }

    @Override
    protected void modulesReplace() {
        //robot.getFactory().replace(TrajectoryFollower.class, new TrajectoryFollower() {});
        EventBus.getInstance().invoke(new ReplaceFeedbackControllerEvent(new TeleOpFeedback()));
    }

    private final BorderButton hiAimButt = new BorderButton();
    private final BorderButton lowAimButt = new BorderButton();
    private final BorderButton dirReverseButt = new BorderButton();
    private final BorderButton brushReverseButt = new BorderButton();
    private final BorderButton brushReverseButt1 = new BorderButton();
    private final BorderButton ptoButt = new BorderButton();
    private final BorderButton fireButt = new BorderButton();
    private final BorderButton patternFireButt = new BorderButton();
    private final BorderButton purpleFireButt = new BorderButton();
    private final BorderButton greenFireButt = new BorderButton();
    private final BorderButton cancelFireButt = new BorderButton();

    private boolean isPtoActive = false;

    private Pose targetVelocity = new Pose(0,0,0);

    public static double yawSens = 6.8;
    public static double transSens = 180;
    @Override
    protected void loopRun() {
        targetVelocity = new Pose(
                -(gamepad1.right_stick_x * abs(gamepad1.right_stick_x)) * yawSens ,
                   -(gamepad1.left_stick_y * abs(gamepad1.left_stick_y)    * transSens),
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

        if (lowAimButt.get(pose.vector.minus(MatchData.team.goalPose).length()<320)) {
            EventBus.getInstance().invoke(new NewAimEvent(AIM_COMMAND.NEAR));
        }

        if (hiAimButt.get(pose.vector.minus(MatchData.team.goalPose).length()>320)) {
            EventBus.getInstance().invoke(new NewAimEvent(AIM_COMMAND.FAR));
        }




        isAngleControl = gamepad1.right_trigger>0.1;

        if (fireButt.get(gamepad1.left_bumper)) {
            EventBus.getInstance().invoke(new NewGunCommandAvailable(GUN_COMMAND.FULL_FIRE));
        }

//        if (patternFireButt.get(gamepad1.square)) {
//            EventBus.getInstance().invoke(new NewGunCommandAvailable(GUN_COMMAND.FAST_PATTERN_FIRE));
//        }

        if(greenFireButt.get(gamepad1.triangle)){
            EventBus.getInstance().invoke(new NewGunCommandAvailable(GUN_COMMAND.G_FIRE));
        }

        if(purpleFireButt.get(gamepad1.circle)){
            EventBus.getInstance().invoke(new NewGunCommandAvailable(GUN_COMMAND.P_FIRE));
        }

        if(cancelFireButt.get(gamepad1.cross)){
            EventBus.getInstance().invoke(new NewGunCommandAvailable(GUN_COMMAND.EAT));
        }

        if(gamepad1.psWasPressed()){
            DevicePool.getInstance().brakePad.setPos(GunServoPositions.brakePadOffPos);
            DevicePool.getInstance().ptoL.setPos(GunServoPositions.ptoLOpen);
            DevicePool.getInstance().ptoR.setPos(GunServoPositions.ptoROpen);

            EventBus.getInstance().invoke(new NewGunCommandAvailable(GUN_COMMAND.EAT));

        }

        if(ptoButt.get(gamepad1.dpad_up)){
            isAngleControl = false;
            EventBus.getInstance().invoke(new SetNewTargetTrajectorySegmentEvent(
                        wayPoint
            ));

            isPtoActive = true;
        }
        if(isPtoActive && pose.vector.minus(park.vector).length()<10 ){
            isAngleControl = true;
            angleToControl = PI;
        }

        if(isPtoActive && pose.vector.minus(park.vector).length()<10 && AngleUtil.normalize(abs(pose.h-PI))<0.015 ){
            DevicePool.getInstance().brakePad.setPos(GunServoPositions.brakePadOnPos);
            DevicePool.getInstance().ptoL.setPos(GunServoPositions.ptoLClose);
            DevicePool.getInstance().ptoR.setPos(GunServoPositions.ptoRClose);

            EventBus.getInstance().invoke(new NewGunCommandAvailable(GUN_COMMAND.OFF));
        }

        if(gamepad1.dpad_down){
            if(MatchData.team == Team.BLUE)
             DevicePool.getInstance().pinPoint.setPose(148,142, -1.5 * PI);
            else
                DevicePool.getInstance().pinPoint.setPose(148,-142, 1.5 * PI);
        }

    }
    private WayPoint wayPoint = new WayPoint(
            AutonomTask.Stub,
            true,park
    ).setEndAngle(()->park.h).setVel(120);
    private BorderButton goButt = new BorderButton();
    private BorderButton upButt = new BorderButton();
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
                  new PidStatus(0, 0, 0, 0, 0, 0, 0),
                  new PidStatus(0, 0, 0, 0, 0, 0, 0),
                  new PidStatus(0, 0, 0, 0, 0, 0, 0)
            );
        }
        @Override
        public Pose computeU(Pose p1, Pose p2, Pose p3, Pose p4) {
            if(isAngleControl) {
                anglePid.setTarget(angleToControl);
                anglePid.setPos(pose.h);
                anglePid.update();
                return new Pose(anglePid.getU(), 0, 0);
            } else {
                velAnglePid.setTarget(targetVelocity.h);
                velAnglePid.setPos(velocity.h);
                velAnglePid.update();

                velForwardPid.setTarget(targetVelocity.x);
                velForwardPid.setPos(velocity.vector.rotate(-pose.h).x);
                velForwardPid.update();

                return new Pose(velAnglePid.getU(), velForwardPid.getU(), 0);
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

