package org.woen.RobotModule.Modules.Camera;


import android.util.Size;


import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;

import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;
import org.firstinspires.ftc.vision.opencv.ImageRegion;
import org.firstinspires.ftc.vision.opencv.PredominantColorProcessor;
import org.woen.Architecture.EventBus.EventBus;
import org.woen.Hardware.DevicePool.DevicePool;
import org.woen.RobotModule.Modules.Camera.Enums.BALL_COLOR;
import org.woen.RobotModule.Modules.Camera.Enums.MOTIF;
import org.woen.RobotModule.Modules.Camera.Events.NewDetectionBallsCenterEvent;
import org.woen.RobotModule.Modules.Camera.Events.NewDetectionBallsLeftEvent;
import org.woen.RobotModule.Modules.Camera.Events.NewDetectionBallsRightEvent;
import org.woen.RobotModule.Modules.Camera.Events.NewMotifEvent;
import org.woen.RobotModule.Modules.Camera.Interfaces.Camera;
import org.woen.Telemetry.Telemetry;


import java.util.List;

@Config
public class CameraImpl implements Camera {
    public static double minArea = 4000;

    private AprilTagProcessor aprilTagProcessor;

    private VisionPortal visionPortal;

    public static int height = 480;//1080

    public static int width = 640;//1920

    /// left
    public static double leftL = -0.8;
    public static double topL = 0.1;
    public static double rightL = -0.6;
    public static double bottomL = -0.1;
    /// center
    public static double   leftC = -0.1;
    public static double    topC = 0.1;
    public static double  rightC = 0.1;
    public static double bottomC = -0.1;
    /// right
    public static double   leftR = 0.6;
    public static double    topR = 0.1;
    public static double  rightR = 0.8;
    public static double bottomR = -0.1;
    PredominantColorProcessor leftDetection = new PredominantColorProcessor.Builder()
            .setRoi(ImageRegion.asUnityCenterCoordinates(leftL, topL, rightL, bottomL))
            .setSwatches(
                    PredominantColorProcessor.Swatch.ARTIFACT_GREEN,
                    PredominantColorProcessor.Swatch.ARTIFACT_PURPLE,
                    PredominantColorProcessor.Swatch.RED,
                    PredominantColorProcessor.Swatch.BLUE,
                    PredominantColorProcessor.Swatch.YELLOW,
                    PredominantColorProcessor.Swatch.BLACK,
                    PredominantColorProcessor.Swatch.WHITE)
            .build();
    PredominantColorProcessor rightDetection = new PredominantColorProcessor.Builder()
            .setRoi(ImageRegion.asUnityCenterCoordinates(leftR, topR, rightR, bottomR))
            .setSwatches(
                    PredominantColorProcessor.Swatch.ARTIFACT_GREEN,
                    PredominantColorProcessor.Swatch.ARTIFACT_PURPLE,
                    PredominantColorProcessor.Swatch.RED,
                    PredominantColorProcessor.Swatch.BLUE,
                    PredominantColorProcessor.Swatch.YELLOW,
                    PredominantColorProcessor.Swatch.BLACK,
                    PredominantColorProcessor.Swatch.WHITE)
            .build();
    PredominantColorProcessor centerDetection = new PredominantColorProcessor.Builder()
            .setRoi(ImageRegion.asUnityCenterCoordinates(leftC, topC, rightC, bottomC))
            .setSwatches(
                    PredominantColorProcessor.Swatch.ARTIFACT_GREEN,
                    PredominantColorProcessor.Swatch.ARTIFACT_PURPLE,
                    PredominantColorProcessor.Swatch.RED,
                    PredominantColorProcessor.Swatch.BLUE,
                    PredominantColorProcessor.Swatch.YELLOW,
                    PredominantColorProcessor.Swatch.BLACK,
                    PredominantColorProcessor.Swatch.WHITE)
            .build();

    public void init() {
        HardwareMap hardwareMap = DevicePool.getInstance().hardwareMap;
        aprilTagProcessor = new AprilTagProcessor.Builder()
                .setTagFamily(AprilTagProcessor.TagFamily.TAG_36h11)
                .build();



        VisionPortal.Builder builder = new VisionPortal.Builder();

        builder.setCamera(hardwareMap.get(WebcamName.class, "Webcam 1"));


        builder.setStreamFormat(VisionPortal.StreamFormat.MJPEG);
        builder.setCameraResolution(new Size(width, height));
        builder.addProcessor(aprilTagProcessor)
                .addProcessor(leftDetection)
                .addProcessor(rightDetection)
                .addProcessor(centerDetection);

        visionPortal = builder.build();
    }

    private MOTIF latterMotif = null;

   private BALL_COLOR ballsInMouthLeftOld = null;
    private BALL_COLOR ballsInMouthRightOld = null;
    private BALL_COLOR ballsInMouthCenterOld = null;

    public void update() {
        List<AprilTagDetection> currentDetectionList = aprilTagProcessor.getDetections();

        if (!currentDetectionList.isEmpty()) {
            double id = currentDetectionList.get(0).id;
            MOTIF motif = latterMotif;
            if (id == 22) {
                motif = MOTIF.PGP;
            } else if (id == 23) {
                motif = MOTIF.PPG;
            } else if (id == 21) {
                motif = MOTIF.GPP;
            }
            if (latterMotif != motif) {
                EventBus.getInstance().invoke(new NewMotifEvent(motif));
            }
            latterMotif = motif;
        }

        PredominantColorProcessor.Result resultL = leftDetection.getAnalysis();
        PredominantColorProcessor.Result resultR = rightDetection.getAnalysis();
        PredominantColorProcessor.Result resultC = centerDetection.getAnalysis();

        BALL_COLOR left = ballsInMouthLeftOld;
        BALL_COLOR right = ballsInMouthRightOld;
        BALL_COLOR center = ballsInMouthCenterOld;

        if(ballsInMouthCenterOld != center)
            EventBus.getInstance().invoke(new NewDetectionBallsCenterEvent(center));
        if(ballsInMouthLeftOld != left)
            EventBus.getInstance().invoke(new NewDetectionBallsLeftEvent(left));
        if(ballsInMouthRightOld != right)
            EventBus.getInstance().invoke(new NewDetectionBallsRightEvent(right));

        Telemetry.getInstance().add("left", resultL.closestSwatch);
        Telemetry.getInstance().add("center", resultC.closestSwatch);
        Telemetry.getInstance().add("right", resultR.closestSwatch);

    }
}