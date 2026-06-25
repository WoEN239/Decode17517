package org.firstinspires.ftc.teamcode.Robot;

import static java.lang.Math.PI;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.bylazar.configurables.annotations.Configurable;
import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.Pose;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.teamcode.Modules.FSM;
import org.firstinspires.ftc.teamcode.Modules.Turret.Turret;
import org.firstinspires.ftc.teamcode.Pedro.Constants;


@TeleOp
@Config
@Configurable
public class Boot extends LinearOpMode {

    public static Follower follower;

    public static ALLIANCE alliance = ALLIANCE.BLUE;

    public static Pose startPose;
    public static double turret_start_offset = 0;
    public static double turret_start_angle  =PI*0.5;


    @Override
    public void runOpMode() throws InterruptedException {

        boolean oldCross = false;
        follower = Constants.getInstance().createFollower(hardwareMap);
        follower.setStartingPose(new Pose(0,0, PI));

        waitForStart();
        turret_start_offset = 0;
        turret_start_angle = -PI*0.5;

        FSM.usingK = false;

        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());

        DcMotorEx enc = hardwareMap.get(DcMotorEx.class, "motor_lb");
        enc.setDirection(DcMotorSimple.Direction.FORWARD);
        enc.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        enc.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        while (opModeIsActive()) {
            follower.update();
            boolean cross = gamepad1.cross;
            if(gamepad1.left_bumper)
                alliance = ALLIANCE.RED;
            if(gamepad1.right_bumper)
                alliance = ALLIANCE.BLUE;

            if(gamepad1.dpadDownWasPressed()){
                startPose=null ;
            }
            if(gamepad1.dpadUpWasPressed()){
                startPose = follower.getPose();
            }

            FtcDashboard.getInstance().getTelemetry().addData("ALLIANCE", alliance);

            telemetry.addData("ALLIANCE", alliance);
            telemetry.addData("pose",follower.getPose());
            telemetry.addData("start",startPose==null?"null":startPose);

            telemetry.update();

        }


    }
}
