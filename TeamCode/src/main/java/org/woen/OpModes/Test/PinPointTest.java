package org.woen.OpModes.Test;

import com.acmerobotics.dashboard.FtcDashboard;
import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose2D;
import org.woen.Config.MatchData;

@TeleOp
@Disabled
public class PinPointTest extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        waitForStart();
        GoBildaPinpointDriver pinpointDriver = hardwareMap.get(GoBildaPinpointDriver.class,"odometerComputer");

        pinpointDriver.setEncoderDirections(GoBildaPinpointDriver.EncoderDirection.FORWARD,
                GoBildaPinpointDriver.EncoderDirection.REVERSED);
        pinpointDriver.setOffsets(12.75,0, DistanceUnit.CM);
        pinpointDriver.setPosition(new Pose2D(DistanceUnit.CM, MatchData.start.pose.x, MatchData.start.pose.y,
                AngleUnit.RADIANS,MatchData.start.pose.h));
        pinpointDriver.recalibrateIMU();

        while (opModeIsActive()){
            pinpointDriver.update();
            FtcDashboard.getInstance().getTelemetry().addData("pose",pinpointDriver.getPosition());
            FtcDashboard.getInstance().getTelemetry().update();
        }
    }
}
