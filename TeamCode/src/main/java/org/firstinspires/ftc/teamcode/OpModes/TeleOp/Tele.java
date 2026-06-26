package org.firstinspires.ftc.teamcode.OpModes.TeleOp;

import static com.pedropathing.math.MathFunctions.normalizeAngleSigned;
import static java.lang.Math.PI;
import static java.lang.Math.abs;
import static java.lang.Math.max;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.bylazar.configurables.annotations.Configurable;
import com.bylazar.telemetry.PanelsTelemetry;
import com.bylazar.telemetry.TelemetryManager;
import com.pedropathing.control.PIDFCoefficients;
import com.pedropathing.control.PIDFController;
import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.Pose;
import com.pedropathing.math.MathFunctions;
import com.pedropathing.math.Vector;

import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose2D;
import org.firstinspires.ftc.teamcode.Modules.FSM;
import org.firstinspires.ftc.teamcode.Modules.FSM_STATE;
import org.firstinspires.ftc.teamcode.Modules.Shooter.Flywheel;
import org.firstinspires.ftc.teamcode.Pedro.Constants;
import org.firstinspires.ftc.teamcode.Robot.ALLIANCE;
import org.firstinspires.ftc.teamcode.Robot.Boot;
import org.firstinspires.ftc.teamcode.Util.CashedMotor;

@Configurable
@Config
@TeleOp
public class Tele extends OpMode {
    private TelemetryManager telemetryM;

    FSM fsm = new FSM();

    boolean isAngleControl = true;
    double angleToControl = PI * 0.5;
    public static PIDFCoefficients anglePidC = new PIDFCoefficients(2.5, 0, 0.1, 0);
    private PIDFController anglePid = new PIDFController(anglePidC);

    boolean old_left_bumper = false;
    int count_press = 0;
    boolean isItReversed = false;

    ElapsedTime telemetryTimer = new ElapsedTime();
    public static double wX = 0.8;
    public static double wY = 0.8;
    public static double wH = 0.8;

    GoBildaPinpointDriver odo;
    @Override
    public void init() {
        odo = hardwareMap.get(GoBildaPinpointDriver.class, "pinpoint");

        telemetryM = PanelsTelemetry.INSTANCE.getTelemetry();

        motor_lf = new CashedMotor(hardwareMap.get(DcMotorEx.class,"motor_lf"));
        motor_rf = new CashedMotor(hardwareMap.get(DcMotorEx.class,"motor_rf"));
        motor_rb = new CashedMotor(hardwareMap.get(DcMotorEx.class,"motor_rb"));
        motor_lb = new CashedMotor(hardwareMap.get(DcMotorEx.class,"motor_lb"));

        FSM.usingK = true;


        //hardwareMap.getAll(LynxModule.class).forEach(i->i.setBulkCachingMode(LynxModule.BulkCachingMode.AUTO));
    }

    @Override
    public void start() {
        fsm.start(hardwareMap,this::getPose, this::getVelocity);
        telemetryTimer.reset();
    }

    ElapsedTime deltaTime = new ElapsedTime();
    Pose velocity;
    Pose pose;

    public Pose getPose() {
        return pose.copy();
    }
    public Pose getVelocity(){
        return velocity.copy();
    }

    @Override
    public void loop() {
        odo.update();
        Pose2D pose2D = odo.getPosition();
        pose = new Pose(pose2D.getX(DistanceUnit.INCH), pose2D.getY(DistanceUnit.INCH), pose2D.getHeading(AngleUnit.RADIANS));

        velocity = new Pose(odo.getVelX(DistanceUnit.INCH),odo.getVelY(DistanceUnit.INCH),odo.getHeading(AngleUnit.RADIANS));

        fsm.update();

        if(gamepad2.dpadUpWasPressed()){
            if(Boot.alliance == ALLIANCE.RED){
                Flywheel.yGoal += 1;
                Flywheel.yGoalFar += 1;
            }else{
                Flywheel.yGoal -= 1;
                Flywheel.yGoalFar -= 1;
            }
        }

        if(gamepad2.dpadDownWasPressed()){
            if(Boot.alliance == ALLIANCE.RED){
                Flywheel.yGoal -= 1;
                Flywheel.yGoalFar -= 1;
            }else{
                Flywheel.yGoal += 1;
                Flywheel.yGoalFar += 1;
            }
        }

        if(gamepad2.dpadRightWasPressed()){
            if(Boot.alliance == ALLIANCE.RED){
                Flywheel.xGoal += 1;
                Flywheel.xGoalFar += 1;
            }else{
                Flywheel.xGoal -= 1;
                Flywheel.xGoalFar -= 1;
            }
        }

        if(gamepad2.dpadLeftWasPressed()){
            if(Boot.alliance == ALLIANCE.RED){
                Flywheel.xGoal -= 1;
                Flywheel.xGoalFar -= 1;
            }else{
                Flywheel.xGoal += 1;
                Flywheel.xGoalFar += 1;
            }
        }

        if(gamepad2.rightBumperWasPressed()){
            Boot.turret_start_offset += 0.01;
        }
        if(gamepad2.leftBumperWasPressed()){
            Boot.turret_start_offset -= 0.01;
        }
        telemetry.addData("xGoal",Flywheel.xGoal);
        telemetry.addData("yGoal",Flywheel.yGoal);
        telemetry.addData("xGoalFar",Flywheel.xGoalFar);
        telemetry.addData("yGoalFar",Flywheel.yGoalFar);

        telemetry.addData("start offset",Boot.turret_start_offset);
        telemetry.update();
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
        sticks.setOrthogonalComponents(fpv(-gamepad1.left_stick_y,wX),fpv(-gamepad1.left_stick_x,wY));
        double allianceAngle = PI*0.5;
        if(Boot.alliance == ALLIANCE.BLUE){
            allianceAngle = -PI*0.5;
        }
        sticks.rotateVector(-pose.getHeading() + allianceAngle);

        Vector vel = getVelocity().getAsVector();
        vel.rotateVector(getPose().getHeading());

        xPid.setTargetPosition(sticks.getXComponent()*88);
        xPid.updatePosition(vel.getXComponent());
        xPid.updateFeedForwardInput(sticks.getXComponent()*88);
        double x = xPid.run();
        FtcDashboard.getInstance().getTelemetry().addData("xErr", xPid.getError());

        yPid.setTargetPosition(sticks.getYComponent()*88);
        yPid.updatePosition(vel.getYComponent());
        yPid.updateFeedForwardInput(sticks.getYComponent()*88);
        double y = yPid.run();
        FtcDashboard.getInstance().getTelemetry().addData("yErr", yPid.getError());

        hPid.setTargetPosition(fpv(-gamepad1.right_stick_x,wH)*7);
        hPid.updatePosition(getVelocity().getHeading());
        hPid.updateFeedForwardInput(fpv(-gamepad1.right_stick_x,wH)*7);
        double h = hPid.run();

        telemetryM.debug(hPid.getError());

        isAngleControl = gamepad1.right_trigger>0.1;
        if(isAngleControl){
            anglePid.setCoefficients(anglePidC);
            anglePid.updateError( normalizeAngleSigned(angleToControl-getPose().getHeading()) );
            h = anglePid.run();
        }

        x = MathFunctions.clamp(x,-1,1);
        h = MathFunctions.clamp(h,-1,1);
        y = MathFunctions.clamp(y,-1,1);

        double lf = x - h - y;
        double rf = x + h + y;
        double rb = x + h - y;
        double lb = x - h + y;

        double max = max( max(abs(lf),abs(lb)),max(abs(rf),abs(rb)) );
        if(max>1){
            lf/=max;
            lb/=max;
            rb/=max;
            rf/=max;
        }

        runDrive(lf,rf,rb,lb);
        FtcDashboard.getInstance().getTelemetry().addData("herz",1d/(deltaTime.seconds()));

        FtcDashboard.getInstance().getTelemetry().addData("jx",gamepad1.left_stick_y);
        FtcDashboard.getInstance().getTelemetry().addData("herz",1d/(deltaTime.seconds()));

        deltaTime.reset();
        lastTime = System.nanoTime();
        FtcDashboard.getInstance().getTelemetry().update();
    }
    double lastTime = 0;
    private double fpv(double x, double w) {
        return (1 - w) * x + w * x * x * x;
    }

    public static PIDFCoefficients xPidC = new PIDFCoefficients(0.015, 0, 0, 0.011);
    public static PIDFCoefficients yPidC = new PIDFCoefficients(0.02, 0, 0, 0.011);
    public static PIDFCoefficients hPidC = new PIDFCoefficients(0.05, 0, 0, 0.14);
    private PIDFController xPid = new PIDFController(xPidC);
    private PIDFController yPid = new PIDFController(yPidC);
    private PIDFController hPid = new PIDFController(hPidC);

    CashedMotor motor_lf;
    CashedMotor motor_rf;
    CashedMotor motor_rb;
    CashedMotor motor_lb;

    private void runDrive(double lf, double rf, double rb, double lb){
        motor_lf.setPower(lf);
        motor_rf.setPower(rf);
        motor_rb.setPower(rb);
        motor_lb.setPower(lb);
    }

    @Override
    public void stop() {
        //fsm.stop();
    }
}