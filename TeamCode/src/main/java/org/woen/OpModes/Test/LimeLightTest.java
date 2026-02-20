package org.woen.OpModes.Test;

import com.acmerobotics.dashboard.FtcDashboard;
import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLResultTypes;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import java.nio.file.attribute.FileTime;
import java.util.List;

@Autonomous
public class LimeLightTest extends LinearOpMode {

    @Override
    public void runOpMode() {

        Limelight3A limelight3A = hardwareMap.get(Limelight3A.class, "Limelight");

        Limelight3A camera = limelight3A;

        DcMotor light = hardwareMap.get(DcMotor.class, "light");

        waitForStart();



        limelight3A.start();
        camera.start();

        limelight3A.getLatestResult();
        LLResult llResult;

        camera.getLatestResult();
        LLResult camRes;


        while (opModeIsActive()) {
            limelight3A.pipelineSwitch(0);
            camera.pipelineSwitch(1);
            llResult = limelight3A.getLatestResult();
            camRes = camera.getLatestResult();

            double[] llResultPythonOutput = llResult.getPythonOutput();

            FtcDashboard.getInstance().getTelemetry().addLine("------");


            //in telemtrey use for [0] is left [1] is center [2] right

            light.setPower(1);

            FtcDashboard.getInstance().getTelemetry().addData("left", llResultPythonOutput[0]);

            FtcDashboard.getInstance().getTelemetry().addData("right", llResultPythonOutput[1]);

            FtcDashboard.getInstance().getTelemetry().addData("center", llResultPythonOutput[2]);


            List<LLResultTypes.FiducialResult> fiducialResults = camRes.getFiducialResults();
            for (LLResultTypes.FiducialResult fr : fiducialResults) {
                FtcDashboard.getInstance().getTelemetry().addData("id", fr.getFiducialId());

            }
            FtcDashboard.getInstance().getTelemetry().update();




        }
        camera.stop();
        limelight3A.stop();

    }

}
