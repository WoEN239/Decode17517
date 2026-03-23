package org.woen.OpModes.Main;

import static java.lang.Math.PI;
import static java.lang.Math.abs;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.R;
import org.woen.Architecture.EventBus.EventBus;
import org.woen.Autonom.Architecture.SetNewWaypointsSequenceEvent;
import org.woen.Autonom.Architecture.WayPoint;
import org.woen.Autonom.Pools.WayPointPool;
import org.woen.Config.MatchData;
import org.woen.Hardware.Factory.DeviceActivationConfig;
import org.woen.Hardware.DevicePool.DevicePool;
import org.woen.OpModes.BaseOpMode;
import org.woen.RobotModule.Factory.ModulesActivateConfig;
import org.woen.RobotModule.Modules.Gun.Arcitecture.NewAimEvent;
import org.woen.RobotModule.Modules.Gun.Arcitecture.NewBrushReversEvent;
import org.woen.RobotModule.Modules.Gun.Arcitecture.NewGunCommandAvailable;
import org.woen.RobotModule.Modules.Gun.Config.AIM_COMMAND;
import org.woen.RobotModule.Modules.Gun.Config.GUN_COMMAND;
import org.woen.RobotModule.Modules.Gun.Config.GunServoPositions;
import org.woen.RobotModule.Modules.TrajectoryFollower.Arcitecture.Feedback.TankFeedbackReference;
import org.woen.RobotModule.Modules.TrajectoryFollower.Arcitecture.Feedback.TankFeedbackReferenceObserver;
import org.woen.RobotModule.Modules.TrajectoryFollower.Arcitecture.Feedforward.FeedforwardReference;
import org.woen.RobotModule.Modules.TrajectoryFollower.Arcitecture.Feedforward.FeedforwardReferenceObserver;
import org.woen.Util.Vectors.Pose;

@Config
@TeleOp(name = "teleOp", group = "A1")
public class TeleOpMode extends BaseOpMode {
    private final FeedforwardReferenceObserver feedforwardReferenceObserver = new FeedforwardReferenceObserver();
    private final TankFeedbackReferenceObserver feedbackObserver = new TankFeedbackReferenceObserver();

    @Override
    protected void loopRun() {
        targetVelocity = new Pose(
                -(gamepad1.right_stick_x * abs(gamepad1.right_stick_x)) * yawSens,
                -(gamepad1.left_stick_y * abs(gamepad1.left_stick_y) * transSens),
                0
        );

        feedforwardReferenceObserver.notifyListeners(new FeedforwardReference(targetVelocity, new Pose(0, 0, 0)));

        angleToControl = Math.PI + MatchData.team.goalPose.minus(pose.vector).getAngle();


        if (brushReverseButt.get(gamepad1.left_trigger > 0.1)) {
            EventBus.getInstance().invoke(new NewBrushReversEvent(true));
        }
        if (brushReverseButt1.get(!(gamepad1.left_trigger > 0.1))) {
            EventBus.getInstance().invoke(new NewBrushReversEvent(false));
        }

        if (lowAimButt.get(pose.vector.x < 60)) {
            EventBus.getInstance().invoke(new NewAimEvent(AIM_COMMAND.NEAR));
        }

        if (lowAimButt.get(pose.vector.x > 60)) {
            EventBus.getInstance().invoke(new NewAimEvent(AIM_COMMAND.FAR));
        }

        isAngleControl = gamepad1.right_trigger > 0.1;

        if (fireButt.get(gamepad1.left_bumper)) {
            EventBus.getInstance().invoke(new NewGunCommandAvailable(GUN_COMMAND.FULL_FIRE));
            gamepad1.rumble(150);
        }

        if (greenFireButt.get(gamepad1.triangle)) {
            colorShootCounter += 1;
            EventBus.getInstance().invoke(new NewGunCommandAvailable(GUN_COMMAND.G_FIRE));
        }

        if (purpleFireButt.get(gamepad1.circle)) {
            colorShootCounter += 1;
            EventBus.getInstance().invoke(new NewGunCommandAvailable(GUN_COMMAND.P_FIRE));
        }
        if (colorShootCounter < 3) {
            colorShootTimer.reset();
        }
        if (colorShootTimer.seconds() > 0.5) {
            colorShootCounter = 0;
            EventBus.getInstance().invoke(new NewGunCommandAvailable(GUN_COMMAND.EAT));
        }
        if (cancelFireButt.get(gamepad1.cross)) {
            colorShootCounter = 0;
            EventBus.getInstance().invoke(new NewGunCommandAvailable(GUN_COMMAND.EAT));
        }

        if (gamepad1.rightBumperWasPressed()) {
            DevicePool.getInstance().ptoL.setPos(GunServoPositions.ptoLBrakePad);
            DevicePool.getInstance().ptoR.setPos(GunServoPositions.ptoRBrakePad);
        }
        if (gamepad1.rightBumperWasReleased()) {
            DevicePool.getInstance().ptoL.setPos(GunServoPositions.ptoLOpen);
            DevicePool.getInstance().ptoR.setPos(GunServoPositions.ptoROpen);
        }

        if (gamepad1.psWasPressed()) {
            EventBus.getInstance().invoke(new SetNewWaypointsSequenceEvent(new ParkWayPointsPool().getPool()));
//            DevicePool.getInstance().ptoL.setPos(GunServoPositions.ptoLOpen);
//            DevicePool.getInstance().ptoR.setPos(GunServoPositions.ptoROpen);
//            EventBus.getInstance().invoke(new NewGunCommandAvailable(GUN_COMMAND.EAT));
        }

        if (gamepad1.dpadUpWasPressed()) {
            DevicePool.getInstance().ptoL.setPos(GunServoPositions.ptoLClose);
            DevicePool.getInstance().ptoR.setPos(GunServoPositions.ptoRClose);
            EventBus.getInstance().invoke(new NewGunCommandAvailable(GUN_COMMAND.OFF));
        }

        if (gamepad1.dpadLeftWasPressed()) {
            EventBus.getInstance().invoke(new NewGunCommandAvailable(GUN_COMMAND.R_FIRE));
        }
        if (gamepad1.dpadRightWasPressed()) {
            EventBus.getInstance().invoke(new NewGunCommandAvailable(GUN_COMMAND.L_FIRE));
        }
        if (gamepad1.dpadDownWasPressed()) {
            EventBus.getInstance().invoke(new NewGunCommandAvailable(GUN_COMMAND.C_FIRE));
        }

        feedbackObserver.notifyListeners(new TankFeedbackReference(isAngleControl, angleToControl));

        telemetry.addData("gunR", DevicePool.getInstance().gunR.getVel());
        telemetry.addData("gunL", DevicePool.getInstance().gunL.getVel());
        telemetry.addData("gunC", DevicePool.getInstance().gunC.getVel());

        telemetry.update();
    }

    @Override
    protected void initRun() {
        DevicePool.getInstance().ptoL.setPos(GunServoPositions.ptoLOpen);
        DevicePool.getInstance().ptoR.setPos(GunServoPositions.ptoROpen);
    }

    public static double yawSens = 6;
    public static double transSens = 200;


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
        modConfig.autonomTaskManager.set(true);
        modulesActivationConfig = modConfig;

    }

    private final BorderButton lowAimButt = new BorderButton();
    private final BorderButton brushReverseButt = new BorderButton();
    private final BorderButton brushReverseButt1 = new BorderButton();
    private final BorderButton fireButt = new BorderButton();
    private final BorderButton purpleFireButt = new BorderButton();
    private final BorderButton greenFireButt = new BorderButton();
    private final BorderButton cancelFireButt = new BorderButton();

    private Pose targetVelocity = new Pose(0, 0, 0);

    private int colorShootCounter = 0;
    private final ElapsedTime colorShootTimer = new ElapsedTime();

    private double angleToControl = 0;
    private boolean isAngleControl = false;

    static class BorderButton{
        private boolean old = false;

        public boolean get(boolean button) {
            boolean indicator = (button != old) && button;
            old = button;
            return indicator;
        }

    }

    class ParkWayPointsPool extends WayPointPool {
        private Pose parkPos = new Pose(0,0,0);
        private WayPoint rotateToPark = new WayPoint(
                new Runnable[]{},
                false,pose
        ).setEndAngle(()->angleTo(parkPos.vector)).setEndDetect(Double.POSITIVE_INFINITY);
        private WayPoint park = new WayPoint(
                new Runnable[]{
                        ()->gamepad1.rumble(200)
                },
                false,parkPos
        ).setVel(150).setEndAngle(()->0d).setEndDetect(2);

        private ParkWayPointsPool(){
            parkPos = parkPos.teamReverse();
        }
        @Override
        public WayPoint[] getPool() {
            return new WayPoint[]{rotateToPark,park};
        }



    }

}