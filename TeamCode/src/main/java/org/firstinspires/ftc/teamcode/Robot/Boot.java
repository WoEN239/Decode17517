package org.firstinspires.ftc.teamcode.Robot;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.bylazar.configurables.annotations.Configurable;
import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.Pose;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Pedro.Constants;


@TeleOp
@Config
@Configurable
public class Boot extends LinearOpMode {

    public static Pose startPose = new Pose(0,0,0);

    public static Follower follower;

    public static ALLIANCE alliance = ALLIANCE.BLUE;


    @Override
    public void runOpMode() throws InterruptedException {

        boolean oldCross = false;

        follower = Constants.createFollower(hardwareMap);


        while (opModeIsActive()) {
            boolean cross = gamepad1.cross;

            if (!oldCross && cross) {
                startPose = follower.getPose();
            }

            if(gamepad1.left_bumper)
                alliance = ALLIANCE.RED;
            if(gamepad1.right_bumper)
                alliance = ALLIANCE.BLUE;

            FtcDashboard.getInstance().getTelemetry().addData("startPose", startPose);
            FtcDashboard.getInstance().getTelemetry().update();
        }
    }
}
