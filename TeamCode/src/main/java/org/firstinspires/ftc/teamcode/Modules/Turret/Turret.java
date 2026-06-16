package org.firstinspires.ftc.teamcode.Modules.Turret;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.bylazar.configurables.annotations.Configurable;
import com.bylazar.telemetry.TelemetryManager;
import com.pedropathing.control.PIDFCoefficients;
import com.pedropathing.control.PIDFController;
import com.pedropathing.math.MathFunctions;
import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.PwmControl;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Supplier;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

@Configurable
@Config
public class Turret {

    public static PIDFCoefficients turretPidC = new PIDFCoefficients(0.4,0,0.02,0);
    public static double errorBorder = 0.02;
    private PIDFController turretPid = new PIDFController(turretPidC);
    private GoBildaPinpointDriver gyro;
    private Supplier<Double> robotAngle;
    public void start(HardwareMap hardwareMap, Supplier<Double> robotAngle) {
        turret1 = hardwareMap.get(Servo.class,"turret1");
        turret2 = hardwareMap.get(Servo.class,"turret2");
        turret0 = hardwareMap.get(Servo.class,"turret0");
        ((PwmControl)turret1).setPwmRange(new PwmControl.PwmRange(500.0, 2500.0));
        ((PwmControl)turret2).setPwmRange(new PwmControl.PwmRange(500.0, 2500.0));
        ((PwmControl)turret0).setPwmRange(new PwmControl.PwmRange(500.0, 2500.0));


        gyro = hardwareMap.get(GoBildaPinpointDriver.class,"turret_gyro");
        gyro.setHeading(Math.PI*0.5,AngleUnit.RADIANS);
        gyro.recalibrateIMU();

        this.robotAngle = robotAngle;
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

    public void update(){
        gyro.update(GoBildaPinpointDriver.ReadData.ONLY_UPDATE_HEADING);
        double turretAngle = gyro.getHeading(AngleUnit.RADIANS);
        turretPid.setCoefficients(turretPidC);
        double angleRF = angleToHold - (robotAngle.get() + Math.PI*0.5);
        angleRF = MathFunctions.normalizeAngleSigned(angleRF);
        angleRF = MathFunctions.clamp(angleRF,-1.5,1.5);
        double angleToHoldN = angleRF + (robotAngle.get() + Math.PI*0.5);
        angleToHoldN = MathFunctions.normalizeAngleSigned(angleToHoldN);

        FtcDashboard.getInstance().getTelemetry().addData("angle target rf", angleRF);
        FtcDashboard.getInstance().getTelemetry().addData("angle target gf", angleToHoldN);
        FtcDashboard.getInstance().getTelemetry().addData("robotAngle", robotAngle.get());
        FtcDashboard.getInstance().getTelemetry().addData("turretAngle", turretAngle);

        FtcDashboard.getInstance().getTelemetry().update();



        double err = MathFunctions.normalizeAngleSigned(angleToHoldN - turretAngle);
        turretPid.updateError( err );
        if(Math.abs(err)<errorBorder){ err = 0;}
        turretPid.updateFeedForwardInput(Math.signum(err));

        power = turretPid.run();

        driveTurret(power);
    }
    double power;
    public void debug(TelemetryManager telemetry){
        telemetry.debug("target turret", angleToHold);
        telemetry.debug("pos turret", gyro.getHeading(AngleUnit.RADIANS));
        telemetry.debug("power turret", power);

        FtcDashboard.getInstance().getTelemetry().addData("target turret", angleToHold);
        FtcDashboard.getInstance().getTelemetry().addData("pos turret", gyro.getHeading(AngleUnit.RADIANS));
        FtcDashboard.getInstance().getTelemetry().addData("power turret", power);

        FtcDashboard.getInstance().getTelemetry().update();
    }

}


