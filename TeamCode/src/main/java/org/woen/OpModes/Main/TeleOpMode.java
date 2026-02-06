package org.woen.OpModes.Main;

import static java.lang.Math.PI;
import static java.lang.Math.abs;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.woen.Architecture.EventBus.EventBus;
import org.woen.Autonom.Structure.AutonomTask;
import org.woen.Autonom.Structure.SetNewWaypointsSequenceEvent;
import org.woen.Autonom.Structure.WayPoint;
import org.woen.Config.ControlSystemConstant;
import org.woen.Config.MatchData;
import org.woen.Config.Team;
import org.woen.Hardware.Factory.DeviceActivationConfig;
import org.woen.Hardware.DevicePool.DevicePool;
import org.woen.OpModes.BaseOpMode;
import org.woen.RobotModule.Factory.ModulesActivateConfig;
import org.woen.RobotModule.Modules.Camera.PipeLineSwitchEvent;
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
@TeleOp(name = "teleOp", group = "A1")
public class TeleOpMode extends BaseOpMode {
    private final FeedforwardReferenceObserver feedforwardReferenceObserver = new FeedforwardReferenceObserver();

    public static PidStatus velPidStatusForward = new PidStatus(0.0141, 0, 0., 0, 0, 0, 5);
    Pid velForwardPid = new Pid(velPidStatusForward);
    {
        velForwardPid.isNormolized = false;
        velForwardPid.isDAccessible = false;
    }

    public static PidStatus velAnglePidStatus = new PidStatus(2.5, 0, 0., 0, 0, 0, 0.5,1);
    Pid velAnglePid = new Pid(velAnglePidStatus);
    {
        velAnglePid.isNormolized = false;
        velAnglePid.isDAccessible = false;
    }

    public static PidStatus anglePidStatus = new PidStatus(12, 0, 1, 0, 0, 0, 0.6 ,0.015);
    Pid anglePid = new Pid(anglePidStatus);
    {
        anglePid.isNormolized = true;
        anglePid.isDAccessible = false;
    }

    public static Pose park = new Pose(0,82,75);
    static {
        if(MatchData.team == Team.RED){
            park = park.teamReverse();
        }
    }
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
        EventBus.getInstance().invoke(new ReplaceFeedbackControllerEvent(new TeleOpFeedback()));
    }
    private final BorderButton lowAimButt = new BorderButton();
    private final BorderButton brushReverseButt = new BorderButton();
    private final BorderButton brushReverseButt1 = new BorderButton();
    private final BorderButton ptoButt = new BorderButton();
    private final BorderButton fireButt = new BorderButton();
    private final BorderButton purpleFireButt = new BorderButton();
    private final BorderButton greenFireButt = new BorderButton();
    private final BorderButton cancelFireButt = new BorderButton();

    private Pose targetVelocity = new Pose(0,0,0);

    public static double yawSens = 7;
    public static double transSens = 200;

    private boolean brakePad = false;

    private int colorShootCounter = 0;
    private final ElapsedTime colorShootTimer = new ElapsedTime();
    @Override
    protected void loopRun() {
        targetVelocity = new Pose(
                -(gamepad1.right_stick_x * abs(gamepad1.right_stick_x)) * yawSens   ,
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

        if (lowAimButt.get(pose.vector.x<60)) {
            EventBus.getInstance().invoke(new NewAimEvent(AIM_COMMAND.NEAR));
        }

        if (lowAimButt.get(pose.vector.x>60)) {
            EventBus.getInstance().invoke(new NewAimEvent(AIM_COMMAND.FAR));
        }

        isAngleControl = gamepad1.right_trigger>0.1;

        if (fireButt.get(gamepad1.left_bumper)) {
            EventBus.getInstance().invoke(new NewGunCommandAvailable(GUN_COMMAND.FULL_FIRE));
        }

        if(greenFireButt.get(gamepad1.triangle)){
            colorShootCounter += 1;
            EventBus.getInstance().invoke(new NewGunCommandAvailable(GUN_COMMAND.G_FIRE));
        }

        if(purpleFireButt.get(gamepad1.circle)){
            colorShootCounter += 1;
            EventBus.getInstance().invoke(new NewGunCommandAvailable(GUN_COMMAND.P_FIRE));
        }
        if(colorShootCounter < 3){
            colorShootTimer.reset();
        }
        if(colorShootTimer.seconds()>0.5){
            colorShootCounter = 0;
            EventBus.getInstance().invoke(new NewGunCommandAvailable(GUN_COMMAND.EAT));
        }
        if(cancelFireButt.get(gamepad1.cross)){
            colorShootCounter = 0;
            EventBus.getInstance().invoke(new NewGunCommandAvailable(GUN_COMMAND.EAT));
        }


        if(gamepad1.rightBumperWasPressed()){
            DevicePool.getInstance().ptoL.setPos(GunServoPositions.ptoLBrakePad);
            DevicePool.getInstance().ptoR.setPos(GunServoPositions.ptoRBrakePad);
        }if(gamepad1.rightBumperWasReleased()){
            DevicePool.getInstance().ptoL.setPos(GunServoPositions.ptoLOpen);
            DevicePool.getInstance().ptoR.setPos(GunServoPositions.ptoROpen);
        }

        if(gamepad1.psWasPressed()){
            DevicePool.getInstance().ptoL.setPos(GunServoPositions.ptoLOpen);
            DevicePool.getInstance().ptoR.setPos(GunServoPositions.ptoROpen);

            EventBus.getInstance().invoke(new NewGunCommandAvailable(GUN_COMMAND.EAT));
        }

       if(gamepad1.dpadUpWasPressed()){
           EventBus.getInstance().invoke(new SetNewWaypointsSequenceEvent(parkWayPoint.copy()));

           DevicePool.getInstance().ptoL.setPos(GunServoPositions.ptoLClose);
           DevicePool.getInstance().ptoR.setPos(GunServoPositions.ptoRClose);
           EventBus.getInstance().invoke(new NewGunCommandAvailable(GUN_COMMAND.OFF));
       }

        if(gamepad1.dpadLeftWasPressed()){
            EventBus.getInstance().invoke(new NewGunCommandAvailable(GUN_COMMAND.L_FIRE));
        }
        if(gamepad1.dpadRightWasPressed()){
            EventBus.getInstance().invoke(new NewGunCommandAvailable(GUN_COMMAND.R_FIRE));
        }
        if(gamepad1.dpadDownWasPressed()){
            EventBus.getInstance().invoke(new NewGunCommandAvailable(GUN_COMMAND.C_FIRE));
        }

//        if(gamepad1.dpad_down){
//            if(MatchData.team == Team.BLUE)
//                DevicePool.getInstance().pinPoint.setPose(167.574 ,157.701, 0.0);
//            else
//                DevicePool.getInstance().pinPoint.setPose(167.574,-157.701, 0.0 );
//        }

        telemetry.addData("gunR",DevicePool.getInstance().gunR.getVel());
        telemetry.addData("gunL",DevicePool.getInstance().gunL.getVel());
        telemetry.addData("gunC",DevicePool.getInstance().gunC.getVel());

        telemetry.update();

    }
    private WayPoint rotateToPark = new WayPoint(
            new Runnable[]{},true,park
    ).setEndAngle( ()-> AngleUtil.normalize(PI + park.vector.minus(pose.vector).getAngle()) ).setEndDetect(200);

    private WayPoint parkWayPoint = new WayPoint(
            AutonomTask.Stub,
            true,park
    ).setEndAngle(()->park.h).setVel(30).setEndDetect(5).setLookAheadRadius(5);

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
                double dir = Math.signum(velocity.vector.rotate(-pose.h).x);
                velForwardPid.setPos(dir*velocity.vector.length());
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

