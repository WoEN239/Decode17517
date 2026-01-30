package org.woen.OpModes.Test;

import com.acmerobotics.dashboard.FtcDashboard;
import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLResultTypes;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import java.util.List;

@Autonomous
public class LimeLightTest extends LinearOpMode {

    @Override
    public void runOpMode(){

        Limelight3A limelight3A = hardwareMap.get(Limelight3A.class, "Limelight");

        waitForStart();

        limelight3A.pipelineSwitch(11);

        limelight3A.start();

        LLResult llResult = limelight3A.getLatestResult();




        while (opModeIsActive()){
            llResult = limelight3A.getLatestResult();

            List<LLResultTypes.FiducialResult> fiducialResults = llResult.getFiducialResults();

            for(LLResultTypes.FiducialResult cr : fiducialResults) {
                FtcDashboard.getInstance().getTelemetry().addData("id", cr.getFiducialId());
                FtcDashboard.getInstance().getTelemetry().update();
            }
        }

    }
}
