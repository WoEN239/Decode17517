package org.firstinspires.ftc.teamcode.Modules.Camera;


import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.bylazar.configurables.annotations.Configurable;
import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLResultTypes;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.Telemetry;

import org.firstinspires.ftc.teamcode.Robot.ALLIANCE;
import org.firstinspires.ftc.teamcode.Robot.Boot;
import org.opencv.core.Mat;

import java.util.List;


@Configurable
@Config
public class LimeLight {

    Limelight3A limelight;


    Telemetry telemetry;

    public void start(HardwareMap hardwareMap, Telemetry telemetry1) {
        limelight = hardwareMap.get(Limelight3A.class, "limelight");
        limelight.start();
        limelight.pipelineSwitch(0);

        telemetry = new MultipleTelemetry(telemetry1, FtcDashboard.getInstance().getTelemetry());
    }

    public static double searchRadius = 8;

    public enum SIDE_OF_FIELD {
        LEFT, CENTER_R, CENTER_L, LimeLight, RIGHT
    }

    SIDE_OF_FIELD theBiggestCounter = SIDE_OF_FIELD.RIGHT;

    public SIDE_OF_FIELD getTheCounter(){
        return theBiggestCounter;
    }

    public void update() {
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
                theBiggestCounter = SIDE_OF_FIELD.LEFT;
            } else if (rightCount > leftCount && rightCount > centerCountRight && rightCount > centerCountLeft) {
                theBiggestCounter = SIDE_OF_FIELD.RIGHT;
            } else if (centerCountLeft > leftCount && centerCountLeft > centerCountRight && centerCountLeft > rightCount) {
                theBiggestCounter = SIDE_OF_FIELD.CENTER_L;
            } else if (centerCountRight > leftCount && centerCountRight > centerCountLeft && centerCountRight > rightCount) {
                theBiggestCounter = SIDE_OF_FIELD.CENTER_R;
            }

        } else {
            telemetry.addData("Статус", "Объекты не найдены");
        }

        telemetry.update();
    }
}


