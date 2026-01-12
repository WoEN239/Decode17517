package org.woen.OpModes.ServiceOpMode;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.woen.Config.MatchData;
import org.woen.Config.Team;

@Autonomous(group = "boot")
public class BootLoader extends LinearOpMode {
    private String[] auto = new String[]{
            "far9pattern","far15ball"
    };
    private int i = 0;
    @Override
    public void runOpMode() throws InterruptedException {
        GoBildaPinpointDriver odo = hardwareMap.get(GoBildaPinpointDriver.class, "odometerComputer");

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
                odo.resetPosAndIMU();
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

