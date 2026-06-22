package org.firstinspires.ftc.teamcode.Modules.Camera;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.telemetry;

import com.acmerobotics.dashboard.config.Config;
import com.bylazar.configurables.annotations.Configurable;
import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLResultTypes;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.hardware.HardwareMap;

import java.util.List;


@Configurable
@Config
public class LimeLight {

    Limelight3A limelight;


    public void start(HardwareMap hardwareMap){
        limelight = hardwareMap.get(Limelight3A.class, "limelight");
    }


    private double[] findDensest2DCluster(List<LLResultTypes.DetectorResult> detections, double clusterRadius) {

        if (detections == null || detections.isEmpty()) {
            return new double[]{0.0, 0.0, 0.0};
        }

        int maxCount = 0;
        double bestCenterX = 0.0;
        double bestCenterY = 0.0;


        for (int i = 0; i < detections.size(); i++) {
            double anchorX = detections.get(i).getTargetXDegrees();
            double anchorY = detections.get(i).getTargetYDegrees();

            int currentCount = 0;
            double sumX = 0;
            double sumY = 0;


            for (int j = 0; j < detections.size(); j++) {
                double checkX = detections.get(j).getTargetXDegrees();
                double checkY = detections.get(j).getTargetYDegrees();


                double distance = Math.hypot(checkX - anchorX, checkY - anchorY);


                if (distance <= clusterRadius) {
                    currentCount++;
                    sumX += checkX;
                    sumY += checkY;
                }
            }


            if (currentCount > maxCount) {
                maxCount = currentCount;
                bestCenterX = sumX / currentCount;
                bestCenterY = sumY / currentCount;
            }
        }

        return new double[]{bestCenterX, bestCenterY, maxCount};
    }

    double targetY = 0;

    public double getTargetY(){
        return targetY;
    }

    public static double searchRadius = 8;

    public void update(){
        LLResult result = limelight.getLatestResult();

        if (result != null && result.isValid()) {
            List<LLResultTypes.DetectorResult> detections = result.getDetectorResults();


            double[] bestCluster = findDensest2DCluster(detections, searchRadius);

            double targetX = bestCluster[0];
            double targetY = bestCluster[1];
            int maxObjects = (int) bestCluster[2];


            telemetry.addData("Objects in frame", detections.size());

            if (maxObjects > 0) {
                telemetry.addData("Biggest artifacts zone", maxObjects);
                telemetry.addData("Coords", "%.2f, %.2f", targetX, targetY);

                this.targetY = targetY;

            }
            telemetry.update();
        }

    }

}
