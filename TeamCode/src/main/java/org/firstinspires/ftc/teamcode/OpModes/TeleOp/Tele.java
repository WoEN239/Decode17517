package org.firstinspires.ftc.teamcode.OpModes.TeleOp;

import static com.pedropathing.math.MathFunctions.normalizeAngle;
import static com.pedropathing.math.MathFunctions.normalizeAngleSigned;
import static java.lang.Math.PI;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.bylazar.configurables.annotations.Configurable;
import com.bylazar.field.FieldManager;
import com.bylazar.field.PanelsField;
import com.bylazar.field.Style;
import com.bylazar.telemetry.PanelsTelemetry;
import com.bylazar.telemetry.TelemetryManager;
import com.pedropathing.control.PIDFCoefficients;
import com.pedropathing.control.PIDFController;
import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.Pose;
import com.pedropathing.math.Vector;
import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Modules.FSM;
import org.firstinspires.ftc.teamcode.Modules.FSM_STATE;
import org.firstinspires.ftc.teamcode.Pedro.Constants;

@Configurable
@Config
@TeleOp
public class Tele extends OpMode {
    private Follower follower;
    private TelemetryManager telemetryM;

    public static final double ROBOT_RADIUS = 9;
    private static final FieldManager panelsField = PanelsField.INSTANCE.getField();

    private static final Style robotLook = new Style("", "#3F51B5", 0.75);

    FSM fsm = new FSM();

    boolean isAngleControl = true;
    double angleToControl = PI * 0.5;
    public static PIDFCoefficients anglePidC = new PIDFCoefficients(1, 0, 0, 0);
    private PIDFController anglePid = new PIDFController(anglePidC);

    boolean old_left_bumper = false;
    int count_press = 0;
    boolean isItReversed = false;

    ElapsedTime telemetryTimer = new ElapsedTime();

    @Override
    public void init() {
        follower = Constants.createFollower(hardwareMap);
        follower.setStartingPose(new Pose(0, 0, PI));

        follower.drivetrain.breakFollowing() ;

        follower.update();
        telemetryM = PanelsTelemetry.INSTANCE.getTelemetry();

        motor_lf = hardwareMap.get(DcMotorEx.class,"motor_lf");
        motor_rf = hardwareMap.get(DcMotorEx.class,"motor_rf");
        motor_rb = hardwareMap.get(DcMotorEx.class,"motor_rb");
        motor_lb = hardwareMap.get(DcMotorEx.class,"motor_lb");

        hardwareMap.getAll(LynxModule.class).forEach(i->i.setBulkCachingMode(LynxModule.BulkCachingMode.AUTO));
    }

    @Override
    public void start() {
        fsm.start(hardwareMap, follower);
        telemetryTimer.reset();
    }

    ElapsedTime deltaTime = new ElapsedTime();
    @Override
    public void loop() {
        fsm.update();
        follower.poseTracker.update();

        if (gamepad1.left_trigger > 0.1) {
            fsm.setState(FSM_STATE.REVERSE);
            isItReversed = true;
        }
        if (gamepad1.left_trigger < 0.1 && isItReversed) {
            fsm.setState(FSM_STATE.EAT);
            isItReversed = false;
        }

        if (gamepad1.left_bumper && !old_left_bumper) {
            if (count_press == 0) {
                fsm.setState(FSM_STATE.DRIVE);
                count_press = 1;
            } else {
                fsm.setState(FSM_STATE.SHOOT);
                count_press = 0;
            }
        }
        old_left_bumper = gamepad1.left_bumper;

        if (gamepad1.square) fsm.setState(FSM_STATE.SHOOT_LEFT);
        if (gamepad1.triangle) fsm.setState(FSM_STATE.SHOOT_CENTER);
        if (gamepad1.circle) fsm.setState(FSM_STATE.SHOOT_RIGHT);


        xPid.setCoefficients(xPidC);
        hPid.setCoefficients(hPidC);
        yPid.setCoefficients(yPidC);

        Vector sticks = new Vector();
        sticks.setOrthogonalComponents(fpv(-gamepad1.left_stick_y),fpv(-gamepad1.left_stick_x));
        sticks.rotateVector(-follower.getPose().getHeading() + Math.PI*0.5);

        Vector vel = follower.getVelocity();
        vel.rotateVector(-follower.getPose().getHeading());

        xPid.setTargetPosition(sticks.getXComponent()*84);
        xPid.updatePosition(vel.getXComponent());
        xPid.updateFeedForwardInput(sticks.getXComponent()*84);
        double x = xPid.run();

        yPid.setTargetPosition(sticks.getYComponent()*65);
        yPid.updatePosition(vel.getYComponent());
        yPid.updateFeedForwardInput(sticks.getYComponent()*65);
        double y = yPid.run();

        hPid.setTargetPosition(fpv(-gamepad1.right_stick_x)*7);
        hPid.updatePosition(follower.getAngularVelocity());
        hPid.updateFeedForwardInput(fpv(-gamepad1.right_stick_x)*7);
        double h = hPid.run();

        telemetryM.debug(hPid.getError());

        isAngleControl = gamepad1.right_trigger>0.1;
        if(isAngleControl){
            anglePid.setCoefficients(anglePidC);
            anglePid.updateError( normalizeAngle(angleToControl-follower.getHeading()) );
            h = anglePid.run();
        }

        double lf = x - h - y;
        double rf = x + h + y;
        double rb = x + h - y;
        double lb = x - h + y;

        follower.getDrivetrain().runDrive(
                new double[]{lf,lb,rf,rb}
        );
        FtcDashboard.getInstance().getTelemetry().addData("herz",1d/(deltaTime.seconds()));
        deltaTime.reset();
        lastTime = System.nanoTime();
        FtcDashboard.getInstance().getTelemetry().update();
    }
    double lastTime = 0;
    private double fpv(double x) {
        return (1 - 0.68) * x + 0.68 * x * x * x;
    }

    public static PIDFCoefficients xPidC = new PIDFCoefficients(0.015, 0, 0, 0.012);
    public static PIDFCoefficients yPidC = new PIDFCoefficients(0.02, 0, 0, 0.0154);
    public static PIDFCoefficients hPidC = new PIDFCoefficients(0.05, 0, 0, 0.14);
    private PIDFController xPid = new PIDFController(xPidC);
    private PIDFController yPid = new PIDFController(yPidC);
    private PIDFController hPid = new PIDFController(hPidC);

    DcMotorEx motor_lf;
    DcMotorEx motor_rf;
    DcMotorEx motor_rb;
    DcMotorEx motor_lb;

    private void runDrive(double lf, double rf, double rb, double lb){
        motor_lf.setPower(lf);
        motor_rf.setPower(rf);
        motor_rb.setPower(rb);
        motor_lb.setPower(lb);
    }


}