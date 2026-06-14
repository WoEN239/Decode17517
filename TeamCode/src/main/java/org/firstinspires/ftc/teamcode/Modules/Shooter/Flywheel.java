package org.firstinspires.ftc.teamcode.Modules.Shooter;

import android.util.SparseArray;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.bylazar.configurables.annotations.Configurable;
import com.pedropathing.control.PIDFCoefficients;
import com.pedropathing.control.PIDFController;
import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.Pose;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.teamcode.Robot.ALLIANCE;


@Config
@Configurable
public class Flywheel {
    public static PIDFCoefficients flywheelMotorCoef = new PIDFCoefficients(0.01, 0, 0, 0.00039);

    private PIDFController lPIDFCotroler = new PIDFController(flywheelMotorCoef);

    private PIDFController cPIDFCotroler = new PIDFController(flywheelMotorCoef);

    private PIDFController rPIDFCotroler = new PIDFController(flywheelMotorCoef);
    private DcMotorEx lMotor;
    private DcMotorEx rMotor;
    private DcMotorEx cMotor;

    public static boolean debug = false;

    public static double kV = 0;

    private Servo l;
    private Servo r;
    private Servo c;

    public static double errorBorder = 0;

    ShooterConst shooterConst = new ShooterConst();

    private Follower follower;

    public static double minDistNear = 52;
    public static double maxDistNear = 112;
    public static double minDistFar = 52;
    public static double maxDistFar = 112;

    public static Pose goal = new Pose(-70, 70, 0);

    public void start(HardwareMap hardwareMap, Follower follower, ALLIANCE alliance) {
        this.follower = follower;
        lMotor = hardwareMap.get(DcMotorEx.class, "gun_l");
        rMotor = hardwareMap.get(DcMotorEx.class, "gun_r");
        cMotor = hardwareMap.get(DcMotorEx.class, "gun_c");
        lMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        cMotor.setDirection(DcMotorSimple.Direction.FORWARD);
        rMotor.setDirection(DcMotorSimple.Direction.FORWARD);
        lMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        cMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        lMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        cMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        r = hardwareMap.get(Servo.class, "banan_r");
        c = hardwareMap.get(Servo.class, "banan_c");
        l = hardwareMap.get(Servo.class, "banan_l");
        ///  if (ALLIANCE.alliance == ALLIANCE.RED) {
        //   goal = Utility.revertPose(goal);
        /// }
    }

    private double calculatePowerToDist(double dist2Tar, double minDist, double maxDist, double minVel2Tar, double maxVel2Tar) {
        double y = minVel2Tar + (dist2Tar - minDist) * (maxVel2Tar - minVel2Tar) / (maxDist - minDist);
        double realMin = Math.min(minVel2Tar, maxVel2Tar);
        double realMax = Math.max(minVel2Tar, maxVel2Tar);

        if (y > realMax) y = realMax;
        if (y < realMin) y = realMin;
        return y;
    }

    double velL;
    double velR;
    double velC;
    double lPos;
    double rPos;
    double cPos;

    public void update() {
        follower.update();

        double distToTarget = goal.distanceFrom(follower.getPose());///add for future

        lPIDFCotroler.setCoefficients(flywheelMotorCoef);
        rPIDFCotroler.setCoefficients(flywheelMotorCoef);
        cPIDFCotroler.setCoefficients(flywheelMotorCoef);

        if(distToTarget < 150) {

            velL = calculatePowerToDist(distToTarget, minDistNear, maxDistNear, ShooterConst.leftS[0], ShooterConst.leftS[
                    2]);
            velR = calculatePowerToDist(distToTarget, minDistNear, maxDistNear, ShooterConst.rightS[0], ShooterConst.rightS[
                    2]);
            velC = calculatePowerToDist(distToTarget, minDistNear, maxDistNear, ShooterConst.centerS[0], ShooterConst.centerS[
                    2]);

            lPos = calculatePowerToDist(
                    distToTarget, minDistNear, maxDistNear, ShooterConst.leftS[1], ShooterConst.leftS[3]
            );
            rPos = calculatePowerToDist(
                    distToTarget, minDistNear, maxDistNear, ShooterConst.rightS[1], ShooterConst.rightS[3]
            );
            cPos = calculatePowerToDist(
                    distToTarget, minDistNear, maxDistNear, ShooterConst.centerS[1], ShooterConst.centerS[3]
            );
        }else{


                velL = calculatePowerToDist(distToTarget, minDistFar, maxDistFar, ShooterConst.leftS[4], ShooterConst.leftS[
                        6]);
                velR = calculatePowerToDist(distToTarget, minDistFar, maxDistFar, ShooterConst.rightS[4], ShooterConst.rightS[
                        6]);
                velC = calculatePowerToDist(distToTarget, minDistFar, maxDistFar, ShooterConst.centerS[4], ShooterConst.centerS[
                        6]);

                lPos = calculatePowerToDist(
                        distToTarget, minDistFar, maxDistFar, ShooterConst.leftS[5], ShooterConst.leftS[7]
                );
                rPos = calculatePowerToDist(
                        distToTarget, minDistFar, maxDistFar, ShooterConst.rightS[5], ShooterConst.rightS[7]
                );
                cPos = calculatePowerToDist(
                        distToTarget, minDistFar, maxDistFar, ShooterConst.centerS[5], ShooterConst.centerS[7]
                );

        }

        double errL = velL - lMotor.getVelocity();
        double errC = velC - cMotor.getVelocity();
        double errR = velR - rMotor.getVelocity();


        if (Math.abs(errL) < errorBorder) {
            errL = 0;
        }
        if (Math.abs(errR) < errorBorder) {
            errR = 0;
        }
        if (Math.abs(errC) < errorBorder) {
            errC = 0;
        }


        lPIDFCotroler.updateError(errL);
        cPIDFCotroler.updateError(errC);
        rPIDFCotroler.updateError(errR);


        lPIDFCotroler.updateFeedForwardInput(velL);
        rPIDFCotroler.updateFeedForwardInput(velR);
        cPIDFCotroler.updateFeedForwardInput(velC);

        double powerR = Range.clip(rPIDFCotroler.run(), 0, 1);
        double powerC = Range.clip(cPIDFCotroler.run(), 0, 1);
        double powerL = Range.clip(lPIDFCotroler.run(), 0, 1);


        rMotor.setPower(powerR);
        lMotor.setPower(powerL);
        cMotor.setPower(powerC);

        l.setPosition(lPos);
        r.setPosition(rPos);
        c.setPosition(cPos);
        if (debug) {
            FtcDashboard dashboard = FtcDashboard.getInstance();
            TelemetryPacket packet = new TelemetryPacket();


            packet.put("Target Velocity L", velL);


            packet.put("Current Velocity L", lMotor.getVelocity());
            packet.put("Current Velocity C", cMotor.getVelocity());
            packet.put("Current Velocity R", rMotor.getVelocity());
            packet.put("distance", distToTarget);
            packet.put("far", distToTarget > 150);

            dashboard.sendTelemetryPacket(packet);

        }
    }


}
