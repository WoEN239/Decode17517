package org.firstinspires.ftc.teamcode.Modules.Camera;



import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.bylazar.configurables.annotations.Configurable;
import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLResultTypes;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.teamcode.Robot.ALLIANCE;
import org.firstinspires.ftc.teamcode.Robot.Boot;
import org.opencv.core.Mat;

import java.util.List;


@Configurable
@Config
public class LimeLight {

    Limelight3A limelight;


    public void start(HardwareMap hardwareMap){
        limelight = hardwareMap.get(Limelight3A.class, "limelight");
        limelight.start();
        limelight.pipelineSwitch(0);
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

    public double calculateY(){

        double y = Math.round(0.1125 * targetY * 10)/10.0 ;
        if(Boot.alliance == ALLIANCE.RED){
            y = 72 - y;
        }
        return y = Range.clip(y,18,72);


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


            FtcDashboard.getInstance().getTelemetry().addData("Objects in frame", detections.size());

            if (maxObjects > 0) {
                FtcDashboard.getInstance().getTelemetry().addData("Biggest artifacts zone", maxObjects);
                FtcDashboard.getInstance().getTelemetry().addData("Coords", "%.2f, %.2f", targetX, targetY);

                this.targetY = targetY;

            }
            FtcDashboard.getInstance().getTelemetry().update();
        }

    }

}
