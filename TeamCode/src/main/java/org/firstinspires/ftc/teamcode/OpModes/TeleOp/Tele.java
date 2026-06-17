package org.firstinspires.ftc.teamcode.OpModes.TeleOp;

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
        follower.setStartingPose(new Pose(0, 0, 0));

        follower.drivetrain.breakFollowing() ;

        follower.update();
        telemetryM = PanelsTelemetry.INSTANCE.getTelemetry();
    }

    @Override
    public void start() {
        fsm.start(hardwareMap, follower);
        telemetryTimer.reset();
    }

    @Override
    public void loop() {
        double forward = fpv(-gamepad1.left_stick_y);
        double strafe = fpv(-gamepad1.left_stick_x);
        double turn = fpv(-gamepad1.right_stick_x);

        isAngleControl = gamepad1.right_trigger > 0.1;
        if (isAngleControl) {
            anglePid.setCoefficients(anglePidC);
            anglePid.updateError(normalizeAngleSigned(angleToControl - follower.getHeading()));
            turn = anglePid.run();
        }

        follower.setTeleOpDrive(forward, strafe, turn, false);

        follower.update();
        fsm.update();

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

        if (telemetryTimer.milliseconds() > 100) {
            panelsField.setStyle(robotLook);
            panelsField.moveCursor(follower.getPose().getX(), follower.getPose().getY());
            panelsField.circle(ROBOT_RADIUS);

            Vector v = follower.getPose().getHeadingAsUnitVector();
            v.setMagnitude(v.getMagnitude() * ROBOT_RADIUS);
            panelsField.line(follower.getPose().getX() + v.getXComponent(), follower.getPose().getY() + v.getYComponent());
            panelsField.update();

            telemetryM.debug("X", follower.getPose().getX());
            telemetryM.debug("Y", follower.getPose().getY());
            telemetryM.debug("Heading", follower.getPose().getHeading());
            telemetryM.update();

            telemetryTimer.reset();
        }
    }

    private double fpv(double x) {
        return (1 - 0.68) * x + 0.68 * x * x * x;
    }
}