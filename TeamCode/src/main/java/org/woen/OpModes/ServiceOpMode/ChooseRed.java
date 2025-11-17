package org.woen.OpModes.ServiceOpMode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.woen.Config.MatchData;
import org.woen.Config.Team;

public class ChooseRed extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        waitForStart();
        while (opModeIsActive()){
            MatchData.team = Team.RED;
        }
    }
}
