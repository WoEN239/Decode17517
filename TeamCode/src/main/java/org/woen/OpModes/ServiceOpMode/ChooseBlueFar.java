package org.woen.OpModes.ServiceOpMode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.woen.Config.MatchData;
import org.woen.Config.Start;
import org.woen.Config.Team;

@Autonomous(group = "choose")
public class ChooseBlueFar extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        waitForStart();
        while (opModeIsActive()){
            MatchData.team = Team.BLUE;
            MatchData.start = Start.FAR_BLUE;
        }
    }
}
