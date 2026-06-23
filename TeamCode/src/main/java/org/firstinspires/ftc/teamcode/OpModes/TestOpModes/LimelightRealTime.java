package org.firstinspires.ftc.teamcode.OpModes.TestOpModes;

import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLResultTypes;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Modules.Camera.LimeLight;

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


    LimeLight.SIDE_OF_FIELD theBiggestCounter = LimeLight.SIDE_OF_FIELD.RIGHT;

    public LimeLight.SIDE_OF_FIELD getTheCounter() {
        return theBiggestCounter;
    }

    @Override
    public void loop() {

        LLResult result = limelight.getLatestResult();


        if (result != null && result.isValid()) {

            int leftCount = 0;
            int centerCountLeft = 0;
            int centerCountRight = 0;
            int rightCount = 0;


            List<LLResultTypes.DetectorResult> detections = result.getDetectorResults();


            for (LLResultTypes.DetectorResult detection : detections) {
                double objX = detection.getTargetXDegrees();

                if (objX < -15.0) {
                    leftCount++;
                } else if (objX >= -15.0 && objX <= 0.0) {
                    centerCountLeft++;
                } else if (objX > 0.0 && objX <= 15.0) {
                    centerCountRight++;
                } else if (objX > 15.0) {
                    rightCount++;
                }
            }


            telemetry.addData("Count", detections.size());
            telemetry.addData("Left", leftCount);
            telemetry.addData("Center Left", centerCountLeft);
            telemetry.addData("Center Right", centerCountRight);
            telemetry.addData("Right", rightCount);


            telemetry.addData("Задержка (Staleness)", result.getStaleness() + " мс");

            if (leftCount > rightCount && leftCount > centerCountRight && leftCount > centerCountLeft) {
                theBiggestCounter = LimeLight.SIDE_OF_FIELD.LEFT;
            } else if (rightCount > leftCount && rightCount > centerCountRight && rightCount > centerCountLeft) {
                theBiggestCounter = LimeLight.SIDE_OF_FIELD.RIGHT;
            } else if (centerCountLeft > leftCount && centerCountLeft > centerCountRight && centerCountLeft > rightCount) {
                theBiggestCounter = LimeLight.SIDE_OF_FIELD.CENTER_L;
            } else if (centerCountRight > leftCount && centerCountRight > centerCountLeft && centerCountRight > rightCount) {
                theBiggestCounter = LimeLight.SIDE_OF_FIELD.CENTER_R;
            }

        } else {
            telemetry.addData("Статус", "Объекты не найдены");
        }

        telemetry.update();
    }
}