package org.firstinspires.ftc.teamcode.OpModes.TestOpModes;

import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLResultTypes;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import java.util.List;

@TeleOp
public class LimelightRealTime extends OpMode {

    private Limelight3A limelight;

    @Override
    public void init() {

        limelight = hardwareMap.get(Limelight3A.class, "limelight");


        limelight.pipelineSwitch(0);


        limelight.start();

        telemetry.addData("Status", "Limelight готов!");
        telemetry.update();
    }

    @Override
    public void loop() {

        LLResult result = limelight.getLatestResult();


        if (result != null && result.isValid()) {

            int leftCount = 0;
            int centerCount = 0;
            int rightCount = 0;


            List<LLResultTypes.DetectorResult> detections = result.getDetectorResults();


            for (LLResultTypes.DetectorResult detection : detections) {
                double objX = detection.getTargetXDegrees();

                if (objX < -10.0) {
                    leftCount++;
                } else if (objX >= -10.0 && objX <= 10.0) {
                    centerCount++;
                } else if (objX > 10.0) {
                    rightCount++;
                }

            }


            telemetry.addData("Count", detections.size());
            telemetry.addData("Left", leftCount);
            telemetry.addData("Center", centerCount);
            telemetry.addData("Right", rightCount);


            telemetry.addData("Задержка (Staleness)", result.getStaleness() + " мс");

        } else {
            telemetry.addData("Статус", "Объекты не найдены");
        }

        telemetry.update();
    }

    @Override
    public void stop() {
        // Обязательно останавливаем камеру при выключении OpMode, 
        // чтобы освободить ресурсы Control Hub
        if (limelight != null) {
            limelight.stop();
        }
    }
}