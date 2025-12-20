package org.woen.RobotModule.Modules.Camera;

import android.graphics.Canvas;
import android.graphics.Color;
import android.util.Size;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Position;
import org.firstinspires.ftc.robotcore.external.navigation.YawPitchRollAngles;
import org.firstinspires.ftc.robotcore.internal.camera.calibration.CameraCalibration;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.VisionProcessor;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;
import org.firstinspires.ftc.vision.opencv.ColorBlobLocatorProcessor;
import org.firstinspires.ftc.vision.opencv.ColorRange;
import org.firstinspires.ftc.vision.opencv.ImageRegion;
import org.firstinspires.ftc.vision.opencv.PredominantColorProcessor;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;
import org.woen.Architecture.EventBus.EventBus;
import org.woen.Hardware.DevicePool.DevicePool;
import org.woen.Telemetry.Telemetry;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Config
public class CameraImpl implements Camera{
    public static double minArea = 4000;

    private AprilTagProcessor aprilTagProcessor;

    private VisionPortal visionPortal;

    public static int height = 1080;

    public static int width = 1920;

    PredominantColorProcessor colorSensor;

    public void init() {
        HardwareMap hardwareMap = DevicePool.getInstance().hardwareMap;
        aprilTagProcessor = new AprilTagProcessor.Builder()
                .setTagFamily(AprilTagProcessor.TagFamily.TAG_36h11)
                .build();


         colorSensor = new PredominantColorProcessor.Builder()
                .setRoi(ImageRegion.asImageCoordinates(0, 0, 400, 400))
                .setSwatches(
                        PredominantColorProcessor.Swatch.ARTIFACT_GREEN,
                        PredominantColorProcessor.Swatch.ARTIFACT_PURPLE,
                        PredominantColorProcessor.Swatch.RED,
                        PredominantColorProcessor.Swatch.BLUE,
                        PredominantColorProcessor.Swatch.YELLOW,
                        PredominantColorProcessor.Swatch.BLACK,
                        PredominantColorProcessor.Swatch.WHITE)
                .build();

        VisionPortal.Builder builder = new VisionPortal.Builder();

        builder.setCamera(hardwareMap.get(WebcamName.class, "Webcam 1"));

        //builder.addProcessor(colorSensor);

        builder.setStreamFormat(VisionPortal.StreamFormat.MJPEG);
        builder.setCameraResolution(new Size(width, height));
        //builder.addProcessor(new myProcessor());
        builder.addProcessor(aprilTagProcessor);

        visionPortal = builder.build();
    }

    private MOTIF latterMotif = null;
    public void update() {
        List<AprilTagDetection> currentDetectionList = aprilTagProcessor.getDetections();

        if (!currentDetectionList.isEmpty()) {
            double id = currentDetectionList.get(0).id;
            MOTIF motif = latterMotif;
            if(id == 22){
                motif = MOTIF.PGP;
            }else if (id == 23){
                motif = MOTIF.PPG;
            }else if(id == 21){
                motif = MOTIF.GPP;
            }
            if(latterMotif!=motif){
                EventBus.getInstance().invoke(new NewMotifEvent(motif));
            }
            latterMotif = motif;
        }
        Telemetry.getInstance().add("color",colorSensor.getAnalysis().closestSwatch.name());
    }

}

class myProcessor implements VisionProcessor{
    @Override
    public void init(int width, int height, CameraCalibration calibration) {}

    @Override
    public Object processFrame(Mat frame, long captureTimeNanos) {
            Imgproc.resize(frame,frame,new org.opencv.core.Size(192,108));
            Imgproc.resize(frame,frame,new org.opencv.core.Size(1920,1080));

            return null;
    }

    @Override
    public void onDrawFrame(Canvas canvas, int onscreenWidth, int onscreenHeight, float scaleBmpPxToCanvasPx, float scaleCanvasDensity, Object userContext) {

    }

}