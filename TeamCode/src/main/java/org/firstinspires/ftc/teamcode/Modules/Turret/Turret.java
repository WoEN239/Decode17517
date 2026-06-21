package org.firstinspires.ftc.teamcode.Modules.Turret;

import static java.lang.Math.abs;
import static java.lang.Math.signum;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.bylazar.configurables.annotations.Configurable;
import com.bylazar.telemetry.TelemetryManager;
import com.pedropathing.control.PIDFCoefficients;
import com.pedropathing.control.PIDFController;
import com.pedropathing.math.MathFunctions;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.PwmControl;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Supplier;

@Configurable
@Config
public class Turret {

    public static PIDFCoefficients turretPidC = new PIDFCoefficients(0.5,0,0.05,0);

    public static double kTrans = 0.2;
    public static double kAngular = -0.14;

    private PIDFController turretPid = new PIDFController(turretPidC);

    private Supplier<Double> robotAngle;
    private Supplier<Double> robotVel;
    private double robotAngleVel = 0;

    public void setRobotAngleVel(double robotAngleVel) {
        this.robotAngleVel = robotAngleVel;
    }

    public static double ENCODER_TICK_PER_REV = 8192.0;
    public static double GEAR_RATIO = 15.0/130.0;
    public static double START_OFFSET = -Math.PI * 0.;


    DcMotorEx enc;
    public void start(HardwareMap hardwareMap, Supplier<Double> robotAngle,Supplier<Double> robotVel ) {
        turret1 = hardwareMap.get(Servo.class,"turret1");
        turret2 = hardwareMap.get(Servo.class,"turret2");
        turret0 = hardwareMap.get(Servo.class,"turret0");
        ((PwmControl)turret1).setPwmRange(new PwmControl.PwmRange(500.0, 2500.0));
        ((PwmControl)turret2).setPwmRange(new PwmControl.PwmRange(500.0, 2500.0));
        ((PwmControl)turret0).setPwmRange(new PwmControl.PwmRange(500.0, 2500.0));


        enc = hardwareMap.get(DcMotorEx.class, "motor_lb");
        enc.setDirection(DcMotorSimple.Direction.FORWARD);
        enc.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        enc.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        this.robotAngle = robotAngle;
        this.robotVel = robotVel;
    }

    Servo turret1;
    Servo turret2;
    Servo turret0;

    public void driveTurret(double power){
        power = MathFunctions.clamp(power,-1,1);
        power += 1;
        power*=0.5;
        turret1.setPosition(1-power);
        turret2.setPosition(power);
        turret0 .setPosition(power);
    }

    private double angleToHold = Math.PI*0.5;
    public void setAngleToHold(double angleToHold){
        this.angleToHold = angleToHold;
    }

    public double getAngleFromEnc(){
        int currentTick = enc.getCurrentPosition();
        double encoderRev = currentTick / ENCODER_TICK_PER_REV;
        double turretRevs = encoderRev * GEAR_RATIO;
        double turretRadians = turretRevs * 2.0 * Math.PI;

        return MathFunctions.normalizeAngleSigned(-turretRadians + START_OFFSET);
    }
    public double getVelFromEnc(){
        double currentTick = enc.getVelocity();
        double encoderRev = currentTick / ENCODER_TICK_PER_REV;
        double turretRevs = encoderRev * GEAR_RATIO;
        double turretRadians = turretRevs * 2.0 * Math.PI;

        return -turretRadians;
    }


    public void update(){

        double turretAngle = getAngleFromEnc();
        turretPid.setCoefficients(turretPidC);
        double ff = robotAngleVel* kTrans + robotVel.get()* kAngular;
        double angleRF = angleToHold - (robotAngle.get() + Math.PI*0.5);
        angleRF = MathFunctions.normalizeAngleSigned(angleRF);
        angleRF = MathFunctions.clamp(angleRF,-1.3,1.3);

        double err = MathFunctions.normalizeAngleSigned(angleRF - turretAngle);
        err = MathFunctions.normalizeAngleSigned(err);

        turretPid.updateError(err);
        power = turretPid.run();

        if( abs(turretAngle) >=1.3 ){
            if(signum(turretAngle) == signum(ff)){
                ff = 0;
            }
        }

        driveTurret(power + ff);

        FtcDashboard.getInstance().getTelemetry().addData("angle",Math.toDegrees(getAngleFromEnc()));

        FtcDashboard.getInstance().getTelemetry().addData("angle target rf", angleRF);
        FtcDashboard.getInstance().getTelemetry().addData("robotAngle", robotAngle.get());
        FtcDashboard.getInstance().getTelemetry().addData("turretAngle", Math.toDegrees(turretAngle));
        FtcDashboard.getInstance().getTelemetry().addData("turret err", Math.toDegrees(err));
        FtcDashboard.getInstance().getTelemetry().addData("turret vel", Math.toDegrees(getVelFromEnc()));
        FtcDashboard.getInstance().getTelemetry().addData("turret FF", ff);


    }
    double power;
    public void debug(TelemetryManager telemetry){
        telemetry.debug("target turret", angleToHold);
        telemetry.debug("power turret", power);

        FtcDashboard.getInstance().getTelemetry().addData("target turret", angleToHold);
        FtcDashboard.getInstance().getTelemetry().addData("pos turret", angleToHold);
        FtcDashboard.getInstance().getTelemetry().addData("power turret", power);

    }

}


