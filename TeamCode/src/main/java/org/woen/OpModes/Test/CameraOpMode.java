package org.woen.OpModes.Test;


import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.vision.apriltag.AprilTagPoseFtc;
import org.woen.RobotModule.Camera.Camera;
import org.woen.Robot.TEAM;

@TeleOp(name = "camera_test")

@Config
public class CameraOpMode extends LinearOpMode {


    public static TEAM team = TEAM.RED;

    @Override
    public void runOpMode() throws InterruptedException {

        Camera camera = new Camera();

        camera.initAprilTag(hardwareMap, TEAM.RED);


        waitForStart();

        while(opModeIsActive()){
            int id = camera.getId();
            camera.update();

           AprilTagPoseFtc pose = camera.getDistance();

            FtcDashboard.getInstance().getTelemetry().addData("id", id);
            FtcDashboard.getInstance().getTelemetry().addData("x", pose.x);
            FtcDashboard.getInstance().getTelemetry().addData("y", pose.y);
            FtcDashboard.getInstance().getTelemetry().addData("z", pose.z);
            FtcDashboard.getInstance().getTelemetry().update();
        }

    }
}
