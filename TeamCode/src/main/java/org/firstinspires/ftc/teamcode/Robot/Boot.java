package org.firstinspires.ftc.teamcode.Robot;

import static java.lang.Math.PI;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.bylazar.configurables.annotations.Configurable;
import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.Pose;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Modules.Turret.Turret;
import org.firstinspires.ftc.teamcode.Pedro.Constants;


@TeleOp
@Config
@Configurable
public class Boot extends LinearOpMode {

    //public static Pose startPose = new Pose(-58, -47, Math.toRadians(145));

    public static Follower follower;

    public static ALLIANCE alliance = ALLIANCE.BLUE;


    @Override
    public void runOpMode() throws InterruptedException {

        boolean oldCross = false;

        waitForStart();
        Turret.START_OFFSET = 0;
        while (opModeIsActive()) {
            boolean cross = gamepad1.cross;
            if(gamepad1.left_bumper)
                alliance = ALLIANCE.RED;
            if(gamepad1.right_bumper)
                alliance = ALLIANCE.BLUE;

            FtcDashboard.getInstance().getTelemetry().addData("ALLIANCE", alliance);
            telemetry.addData("ALLIANCE", alliance);
            telemetry.update();

            if(gamepad1.dpadDownWasPressed()){
                follower = Constants.createFollower(hardwareMap);
                if(alliance == ALLIANCE.BLUE) {
                    follower.setStartingPose(new Pose(0, 0, PI));
                    //follower.setStartingPose(new Pose(-58, -47, Math.toRadians(145)));
                }else{
                    //follower.setStartingPose(new Pose(-58, -47, -Math.toRadians(145)));
                }
                follower.drivetrain.breakFollowing() ;

                follower.update();

            }
        }


    }
}
