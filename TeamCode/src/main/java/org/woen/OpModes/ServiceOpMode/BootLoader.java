package org.woen.OpModes.ServiceOpMode;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose2D;
import org.woen.Config.MatchData;
import org.woen.Config.Team;

@Autonomous(group = "boot")
public class BootLoader extends LinearOpMode {
    private String[] auto = new String[]{
            "far9pattern","near9pattern6ball","near9pattern", "far15ball", "far9pattern3ball", "heaveyballsauto"
    };
    private int i = 0;
    @Override
    public void runOpMode() throws InterruptedException {
        GoBildaPinpointDriver pinpointDriver = hardwareMap.get(GoBildaPinpointDriver.class, "odometerComputer");

        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());

        waitForStart();
        telemetry.clear();

        while (opModeIsActive()) {
            if (gamepad1.crossWasPressed()) {
                if(MatchData.team == Team.RED){
                    MatchData.team = Team.BLUE;
                }else{
                    MatchData.team = Team.RED;
                }
            }

            if (gamepad1.dpadDownWasPressed()) {
                pinpointDriver.setEncoderResolution(GoBildaPinpointDriver.GoBildaOdometryPods.goBILDA_SWINGARM_POD);
                pinpointDriver.setEncoderDirections(GoBildaPinpointDriver.EncoderDirection.FORWARD,
                        GoBildaPinpointDriver.EncoderDirection.FORWARD);
                pinpointDriver.setOffsets(-10.85 ,-18.24, DistanceUnit.CM);
                pinpointDriver.setPosition(new Pose2D(DistanceUnit.CM, MatchData.start.pose.x, MatchData.start.pose.y,
                        AngleUnit.RADIANS,MatchData.start.pose.h));

                pinpointDriver.recalibrateIMU();
            }

            if (gamepad1.triangleWasPressed()) {
                i++;
                i = i% auto.length;
                MatchData.auto = auto[i];
            }



            telemetry.addData("alliance", MatchData.team.toString());
            telemetry.addData("auto", MatchData.auto);
            telemetry.addLine("");
            telemetry.addLine("x - change alliance");
            telemetry.addLine("dpad_down - initialize pinpoint");
            telemetry.update();
        }
    }
}

