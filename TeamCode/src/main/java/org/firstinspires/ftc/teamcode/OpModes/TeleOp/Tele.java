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

import org.firstinspires.ftc.teamcode.Modules.FSM;
import org.firstinspires.ftc.teamcode.Modules.FSM_STATE;
import org.firstinspires.ftc.teamcode.Pedro.Constants;

@Configurable
@Config
@TeleOp
public class Tele extends OpMode {
    private Follower follower;
    public static Pose startingPose; //See ExampleAuto to understand how to use this
    private TelemetryManager telemetryM;

    public static final double ROBOT_RADIUS = 9; // woah
    private static final FieldManager panelsField = PanelsField.INSTANCE.getField();

    private static final Style robotLook = new Style(
            "", "#3F51B5", 0.75
    );

    @Override
    public void init() {
        follower = Constants.createFollower(hardwareMap);
        follower.setStartingPose(new Pose(0, 0, PI));
        follower.update();
        telemetryM = PanelsTelemetry.INSTANCE.getTelemetry();
    }

    //Turret turret = new Turret();

    public static double angle = 30;
    FSM fsm = new FSM();

    public static double k = 1;

    @Override
    public void start() {
        //turret.start(hardwareMap,()->follower.getPose().getHeading());

        fsm.start(hardwareMap, follower);
        enc = hardwareMap.get(
                DcMotorEx.class, "motor_lb"
        );

    }

    public static PIDFCoefficients xPidC = new PIDFCoefficients(0.015, 0, 0, 0.012);
    public static PIDFCoefficients yPidC = new PIDFCoefficients(0.02, 0, 0, 0.0154);
    public static PIDFCoefficients hPidC = new PIDFCoefficients(0.05, 0, 0, 0.14);
    private PIDFController xPid = new PIDFController(xPidC);
    private PIDFController yPid = new PIDFController(yPidC);
    private PIDFController hPid = new PIDFController(hPidC);


    boolean isAngleControl = true;
    double angleToControl = PI * 0.5;
    public static PIDFCoefficients anglePidC = new PIDFCoefficients(1, 0, 0, 0);
    private PIDFController anglePid = new PIDFController(anglePidC);

    boolean old_left_bumper = false;
    int count_press = 0;

    boolean isItReversed = false;

    DcMotorEx enc;


    @Override
    public void loop() {
        //Call this once per loop
        updateAll();


        xPid.setCoefficients(xPidC);
        hPid.setCoefficients(hPidC);
        yPid.setCoefficients(yPidC);

        Vector sticks = new Vector();
        sticks.setOrthogonalComponents(fpv(-gamepad1.left_stick_y), fpv(-gamepad1.left_stick_x));
        sticks.rotateVector(-follower.getPose().getHeading() + PI * 0.5);

        Vector vel = follower.getVelocity();
        vel.rotateVector(-follower.getPose().getHeading());


        double x = sticks.getXComponent();


        double y = sticks.getYComponent();

        double h = fpv(-gamepad1.right_stick_x);

        telemetryM.debug(hPid.getError());

        isAngleControl = gamepad1.right_trigger > 0.1;
        if (isAngleControl) {
            anglePid.setCoefficients(anglePidC);
            anglePid.updateError(normalizeAngleSigned(angleToControl - follower.getHeading()));
            h = anglePid.run();
        }

        double lf = x - h - y;
        double rf = x + h + y;
        double rb = x + h - y;
        double lb = x - h + y;

        follower.drivetrain.runDrive(
                new double[]{lf, lb, rf, rb}
        );

        telemetryM.debug("r x", vel.getXComponent());

        FtcDashboard.getInstance().getTelemetry().addData("x", follower.getPose().getX());

        FtcDashboard.getInstance().getTelemetry().addData("y", follower.getPose().getY());

        FtcDashboard.getInstance().getTelemetry().addData("h", Math.toDegrees(follower.getPose().getHeading()));
        telemetryM.debug("r y", vel.getYComponent());
        telemetryM.debug("r h", follower.getAngularVelocity());
        FtcDashboard.getInstance().getTelemetry().addData("angele ",enc.getCurrentPosition() * k);
        FtcDashboard.getInstance().getTelemetry().update();
        if (gamepad1.left_trigger > 0.1) {
            fsm.setState(FSM_STATE.REVERSE);
            isItReversed = true;
        }
        if(gamepad1.left_trigger < 0.1 && isItReversed){
            fsm.setState(FSM_STATE.EAT);
            isItReversed = false;
        }
        if (gamepad1.left_bumper && !old_left_bumper && count_press == 0) {
            fsm.setState(FSM_STATE.DRIVE);
            count_press = 1;
        } else if (gamepad1.left_bumper && !old_left_bumper && count_press == 1) {
            fsm.setState(FSM_STATE.SHOOT);
            count_press = 0;
        }

        if(gamepad1.square)
            fsm.setState(FSM_STATE.SHOOT_LEFT);
        if(gamepad1.triangle)
            fsm.setState(FSM_STATE.SHOOT_CENTER);
        if(gamepad1.circle)
            fsm.setState(FSM_STATE.SHOOT_RIGHT);


        //turret.debug(telemetryM);




        panelsField.setStyle(robotLook);
        panelsField.moveCursor(follower.getPose().getX(), follower.getPose().getY());
        panelsField.circle(ROBOT_RADIUS);

        Vector v = follower.getPose().getHeadingAsUnitVector();
        v.setMagnitude(v.getMagnitude() * ROBOT_RADIUS);
        double x1 = follower.getPose().getX() + v.getXComponent() / 2, y1 = follower.getPose().getY() + v.getYComponent() / 2;
        double x2 = follower.getPose().getX() + v.getXComponent(), y2 = follower.getPose().getY() + v.getYComponent();

        panelsField.setStyle(robotLook);
        panelsField.moveCursor(x1, y1);
        panelsField.line(x2, y2);
        panelsField.update();
        old_left_bumper = gamepad1.left_bumper;

    }

    private void updateAll() {
        follower.update();
        telemetryM.update();
        // turret.update();
        fsm.update();
    }

    private double fpv(double x) {
        return (1 - 0.68) * x + 0.68 * x * x * x;
    }
}

