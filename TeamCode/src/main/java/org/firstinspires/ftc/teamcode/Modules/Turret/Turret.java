package org.firstinspires.ftc.teamcode.Modules.Turret;

import static org.firstinspires.ftc.teamcode.Robot.Boot.turret_start_angle;
import static org.firstinspires.ftc.teamcode.Robot.Boot.turret_start_offset;
import static java.lang.Math.PI;
import static java.lang.Math.abs;
import static java.lang.Math.signum;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.bylazar.configurables.annotations.Configurable;
import com.bylazar.telemetry.TelemetryManager;
import com.pedropathing.control.PIDFCoefficients;
import com.pedropathing.control.PIDFController;
import com.pedropathing.math.MathFunctions;
import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Supplier;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.teamcode.Robot.Boot;

@Configurable
@Config
public class Turret {

    public static PIDFCoefficients turretPidC = new PIDFCoefficients(0.8,0,0.04,0);

    public static double kTrans = 0.175;
    public static double kAngular = -0.1;

    private PIDFController turretPid = new PIDFController(turretPidC);

    private Supplier<Double> robotAngle;
    private Supplier<Double> robotVel;
    private double robotAngleVel = 0;

    public void setRobotAngleVel(double robotAngleVel) {
        this.robotAngleVel = robotAngleVel;
    }

    public static double ENCODER_TICK_PER_REV = 8192.0;
    public static double GEAR_RATIO = 15.0/130.0;

    private GoBildaPinpointDriver gyro;

    DcMotorEx enc;
    public void start(HardwareMap hardwareMap, Supplier<Double> robotAngle,Supplier<Double> robotVel ) {
        turret1 = hardwareMap.get(Servo.class,"turret1");
        turret2 = hardwareMap.get(Servo.class,"turret2");
        turret0 = hardwareMap.get(Servo.class,"turret0");

        gyro = hardwareMap.get(GoBildaPinpointDriver.class,"turret_gyro");
        gyro.setHeading(-0.5*PI, AngleUnit.RADIANS);
        gyro.recalibrateIMU();

        turret0.setPosition(0.5);
        turret1.setPosition(0.5);
        turret2.setPosition(0.5);


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

    private double angleToHold = -Math.PI*0.5;
    public void setAngleToHold(double angleToHold){
        this.angleToHold = angleToHold;
    }

    public double getAngleFromEnc(){
        int currentTick = enc.getCurrentPosition();
        FtcDashboard.getInstance().getTelemetry().addData("turret tks", currentTick);
        double encoderRev = currentTick / ENCODER_TICK_PER_REV;
        double turretRevs = encoderRev * GEAR_RATIO;
        double turretRadians = turretRevs * 2.0 * Math.PI;

        return MathFunctions.normalizeAngleSigned(-turretRadians + turret_start_offset);
    }

    public double getVelFromEnc(){
        double currentTick = enc.getVelocity();

        double encoderRev = currentTick / ENCODER_TICK_PER_REV;
        double turretRevs = encoderRev * GEAR_RATIO;
        double turretRadians = turretRevs * 2.0 * Math.PI;

        return -turretRadians;
    }


    public void update(){

        gyro.update(GoBildaPinpointDriver.ReadData.ONLY_UPDATE_HEADING);

        double turretAngle = getAngleFromEnc();//MathFunctions.normalizeAngleSigned(gyro.getHeading(AngleUnit.RADIANS) - robotAngle.get() - PI*0.5) ;//

        turretPid.setCoefficients(turretPidC);
        double ff = robotAngleVel* kTrans + robotVel.get()* kAngular;
        double angleRF = angleToHold - (robotAngle.get() + Math.PI*0.5);
        if(isLock){
            angleRF = lockAngle;
        }
        angleRF = MathFunctions.normalizeAngleSigned(angleRF);
        angleRF = MathFunctions.clamp(angleRF,-1.3,1.3);

        double err = MathFunctions.normalizeAngleSigned(angleRF - turretAngle);

        turretPid.updateError(err);
        power = turretPid.run();

        if( abs(turretAngle) >=1.3 ){
            if(signum(turretAngle) == signum(ff)){
                ff = 0;
            }
        }

        if(isLock){
            ff = 0;
        }

        driveTurret(power + ff);

        FtcDashboard.getInstance().getTelemetry().addData("angle target rf", angleRF);
        FtcDashboard.getInstance().getTelemetry().addData("robotAngle", robotAngle.get());
        FtcDashboard.getInstance().getTelemetry().addData("turret Angle rfd", Math.toDegrees(turretAngle));
      //  FtcDashboard.getInstance().getTelemetry().addData("turret Angle rfd enc", Math.toDegrees(getAngleFromEnc()));
        FtcDashboard.getInstance().getTelemetry().addData("turret Angle ffd", Math.toDegrees(gyro.getHeading(AngleUnit.RADIANS)));
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
    private boolean isLock = false;
    private double lockAngle = 0;
    public void lock(boolean lock, double lockAngle){
        isLock = lock;
        this.lockAngle = lockAngle;
    }

    public void stopTurret(){
        turret_start_offset = getAngleFromEnc();
    }
}


