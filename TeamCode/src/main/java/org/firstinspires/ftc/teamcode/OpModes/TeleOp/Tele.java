package org.firstinspires.ftc.teamcode.OpModes.TeleOp;

import static com.pedropathing.math.MathFunctions.normalizeAngle;

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
import com.qualcomm.robotcore.hardware.Servo;

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
        follower.setStartingPose(new Pose(0,0,0 ));
        follower.update();
        telemetryM = PanelsTelemetry.INSTANCE.getTelemetry();
    }

    Turret turret = new Turret();
    @Override
    public void start() {
        turret.start(hardwareMap,()->follower.getPose().getHeading());
    }

    public static PIDFCoefficients xPidC = new PIDFCoefficients(0.015,0,0,0.012);
    public static PIDFCoefficients yPidC = new PIDFCoefficients(0.02,0,0,0.0154);
    public static PIDFCoefficients hPidC = new PIDFCoefficients(0.05,0,0,0.14);
    private PIDFController xPid = new PIDFController(xPidC);
    private PIDFController yPid = new PIDFController(yPidC);
    private PIDFController hPid = new PIDFController(hPidC);


    boolean isAngleControl = false;
    double angleToControl = Math.PI*0.5;
    public static PIDFCoefficients anglePidC = new PIDFCoefficients(3,0,0,0);
    private PIDFController anglePid = new PIDFController(anglePidC);


    @Override
    public void loop() {
        //Call this once per loop
        updateAll();

        xPid.setCoefficients(xPidC);
        hPid.setCoefficients(hPidC);
        yPid.setCoefficients(yPidC);

        Vector sticks = new Vector();
        sticks.setOrthogonalComponents(-gamepad1.left_stick_y,-gamepad1.left_stick_x);
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

        hPid.setTargetPosition(-gamepad1.right_stick_x*7);
        hPid.updatePosition(follower.getAngularVelocity());
        hPid.updateFeedForwardInput(-gamepad1.right_stick_x*7);
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

        follower.drivetrain.runDrive(
             new double[]{lf,lb,rf,rb}
        );

        telemetryM.debug("r x",vel.getXComponent());
        telemetryM.debug("r y",vel.getYComponent());
        telemetryM.debug("r h",follower.getAngularVelocity());

        turret.debug(telemetryM);


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

    }

    private void updateAll(){
        follower.update();
        telemetryM.update();
        turret.update();
    }
}

