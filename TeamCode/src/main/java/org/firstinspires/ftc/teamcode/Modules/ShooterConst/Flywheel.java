package org.firstinspires.ftc.teamcode.Modules.ShooterConst;

import com.acmerobotics.dashboard.config.Config;
import com.bylazar.configurables.annotations.Configurable;
import com.pedropathing.control.PIDFCoefficients;
import com.pedropathing.control.PIDFController;
import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.Pose;
import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.Robot.ALLIANCE;
import org.firstinspires.ftc.teamcode.Utility;

import java.util.function.Supplier;


@Config
@Configurable
public class Flywheel {
    public static PIDFCoefficients flywheelMotorCoef = new PIDFCoefficients(0,0,0,0);

    private PIDFController lPIDFCotroler = new PIDFController(flywheelMotorCoef);

    private PIDFController cPIDFCotroler = new PIDFController(flywheelMotorCoef);

    private PIDFController rPIDFCotroler = new PIDFController(flywheelMotorCoef);
    private DcMotorEx lMotor;
    private DcMotorEx rMotor;
    private DcMotorEx cMotor;
    private Servo l;
    private Servo r;
    private Servo c;

    public static double errorBorder = 0;

    ShooterConst shooterConst = new ShooterConst();

    private Follower follower;

    public static Pose goal = new Pose(180,180,0);

    public void start(HardwareMap hardwareMap, Follower follower, ALLIANCE alliance){
        this.follower = follower;
        lMotor = hardwareMap.get(DcMotorEx.class, "");
        rMotor = hardwareMap.get(DcMotorEx.class, "");
        cMotor = hardwareMap.get(DcMotorEx.class, "");
        r = hardwareMap.get(Servo.class, "");
        c = hardwareMap.get(Servo.class, "");
        l = hardwareMap.get(Servo.class, "");
        if(alliance == ALLIANCE.RED){
            goal = Utility.revertPose(goal);
        }
    }

    public void update(){
        follower.update();

        double distToTarget = goal.distanceFrom(follower.getPose());///add for future

        lPIDFCotroler.setCoefficients(flywheelMotorCoef);
        rPIDFCotroler.setCoefficients(flywheelMotorCoef);
        cPIDFCotroler.setCoefficients(flywheelMotorCoef);

        double errL = lMotor.getVelocity() - ShooterConst.leftS[0];
        double errC = cMotor.getVelocity() - ShooterConst.centerS[0];
        double errR = rMotor.getVelocity() - ShooterConst.rightS[0];

        lPIDFCotroler.updateError(errL);
        cPIDFCotroler.updateError(errC);
        rPIDFCotroler.updateError(errR);

        if(Math.abs(errL)<errorBorder){ errL = 0;}
        if(Math.abs(errR)<errorBorder){ errC = 0;}
        if(Math.abs(errC)<errorBorder){ errR = 0;}

        lPIDFCotroler.updateFeedForwardInput(Math.signum(errL));
        rPIDFCotroler.updateFeedForwardInput(Math.signum(errR));
        cPIDFCotroler.updateFeedForwardInput(Math.signum(errC));

        double powerR = rPIDFCotroler.run();
        double powerC = cPIDFCotroler.run();
        double powerL = lPIDFCotroler.run();

        rMotor.setPower(powerR);
        lMotor.setPower(powerL);
        cMotor.setPower(powerC);

        l.setPosition(ShooterConst.leftS[1]);
        r.setPosition(ShooterConst.rightS[1]);
        c.setPosition(ShooterConst.centerS[1]);

    }

}
