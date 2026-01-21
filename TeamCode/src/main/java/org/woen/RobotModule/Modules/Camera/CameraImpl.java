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
import org.woen.OpModes.EndOfOpModeEvent;
import org.woen.RobotModule.Modules.Camera.Enums.MOTIF;
import org.woen.RobotModule.Modules.Camera.Events.NewDetectionBallsCenterEvent;
import org.woen.RobotModule.Modules.Camera.Events.NewDetectionBallsLeftEvent;
import org.woen.RobotModule.Modules.Camera.Events.NewDetectionBallsRightEvent;
import org.woen.RobotModule.Modules.Camera.Events.NewTargetMotifEvent;
import org.woen.RobotModule.Modules.Camera.Interfaces.Camera;
import org.woen.Telemetry.Telemetry;


import java.util.List;

@Config
public class CameraImpl implements Camera {
    private AprilTagProcessor aprilTagProcessor;

    public static int height = 480;

    public static int width = 640;

    /// left
    public static double leftL = -0.6;
    public static double topL = 0.9;
    public static double rightL = -0.4;
    public static double bottomL = 0.7;
    /// center
    public static double   leftC = -0.2;
    public static double    topC = 0.9;
    public static double  rightC = -0.1;
    public static double bottomC = 0.7;
    /// right
    public static double   leftR = 0.8;
    public static double    topR = 0.9;
    public static double  rightR = 1;
    public static double bottomR = 0.8;
    PredominantColorProcessor leftDetection = new PredominantColorProcessor.Builder()
            .setRoi(ImageRegion.asUnityCenterCoordinates(leftL, topL, rightL, bottomL))
            .setSwatches(
                    PredominantColorProcessor.Swatch.ARTIFACT_GREEN,
                    PredominantColorProcessor.Swatch.ARTIFACT_PURPLE,
                    PredominantColorProcessor.Swatch.YELLOW,
                    PredominantColorProcessor.Swatch.WHITE)
            .build();
    PredominantColorProcessor rightDetection = new PredominantColorProcessor.Builder()
            .setRoi(ImageRegion.asUnityCenterCoordinates(leftR, topR, rightR, bottomR))
            .setSwatches(
                    PredominantColorProcessor.Swatch.ARTIFACT_GREEN,
                    PredominantColorProcessor.Swatch.ARTIFACT_PURPLE,
                    PredominantColorProcessor.Swatch.YELLOW,
                    PredominantColorProcessor.Swatch.WHITE)
            .build();
    PredominantColorProcessor centerDetection = new PredominantColorProcessor.Builder()
            .setRoi(ImageRegion.asUnityCenterCoordinates(leftC, topC, rightC, bottomC))
            .setSwatches(
                    PredominantColorProcessor.Swatch.ARTIFACT_GREEN,
                    PredominantColorProcessor.Swatch.ARTIFACT_PURPLE,
                    PredominantColorProcessor.Swatch.YELLOW,
                    PredominantColorProcessor.Swatch.WHITE)
            .build();

    private VisionPortal visionPortal;

    @Override
    public void subscribeInit(){
        EventBus.getInstance().subscribe(EndOfOpModeEvent.class,this::end);
    }
    private void end(EndOfOpModeEvent e){visionPortal.close();}
    @Override
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

    private MOTIF latterTargetMotif = null;
    private PredominantColorProcessor.Swatch latterLeftColor = null;
    private PredominantColorProcessor.Swatch latterRightColor = null;
    private PredominantColorProcessor.Swatch latterCenterColor = null;


    public void update() {
        List<AprilTagDetection> currentDetectionList = aprilTagProcessor.getDetections();

        if (!currentDetectionList.isEmpty()) {
            double id = currentDetectionList.get(0).id;
            MOTIF motif = latterTargetMotif;
            if (id == 22) {
                motif = MOTIF.PGP;
            } else if (id == 23) {
                motif = MOTIF.PPG;
            } else if (id == 21) {
                motif = MOTIF.GPP;
            }
            if (latterTargetMotif != motif) {
                EventBus.getInstance().invoke(new NewTargetMotifEvent(motif));
            }
            latterTargetMotif = motif;
        }

        PredominantColorProcessor.Result resultL = leftDetection.getAnalysis();
        PredominantColorProcessor.Result resultR = rightDetection.getAnalysis();
        PredominantColorProcessor.Result resultC = centerDetection.getAnalysis();


        if(latterCenterColor != resultC.closestSwatch) {
            EventBus.getInstance().invoke(new NewDetectionBallsCenterEvent(resultC.closestSwatch));
        }
        if(latterLeftColor != resultL.closestSwatch) {
            EventBus.getInstance().invoke(new NewDetectionBallsLeftEvent(resultL.closestSwatch));
        }
        if(latterRightColor != resultR.closestSwatch) {
            EventBus.getInstance().invoke(new NewDetectionBallsRightEvent(resultR.closestSwatch));
        }

        Telemetry.getInstance().add("left   ball color", resultL.closestSwatch);
        Telemetry.getInstance().add("center ball color", resultC.closestSwatch);
        Telemetry.getInstance().add("right  ball color", resultR.closestSwatch);

        latterCenterColor = resultC.closestSwatch;
        latterLeftColor   = resultL.closestSwatch;
        latterRightColor  = resultR.closestSwatch;
    }
}
