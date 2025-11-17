package org.woen.RobotModule.Modules.Camera;

import android.util.Size;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Position;
import org.firstinspires.ftc.robotcore.external.navigation.YawPitchRollAngles;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;
import org.woen.Architecture.EventBus.EventBus;
import org.woen.Hardware.DevicePool.DevicePool;

import java.util.List;

public class CameraImpl implements Camera{

    //TODO static pos
    private static Position cameraPosition = new Position(DistanceUnit.METER,
            0, 0, 0, 0);

    private static YawPitchRollAngles cameraOrient = new YawPitchRollAngles(AngleUnit.DEGREES,
            0, -90, 0, 0);

    private AprilTagProcessor aprilTagProcessor;

    private VisionPortal visionPortal;

    public static int height = 600;

    public static int width = 800;

    public void init() {
        HardwareMap hardwareMap = DevicePool.getInstance().hardwareMap;
        aprilTagProcessor = new AprilTagProcessor.Builder()
                .setCameraPose(cameraPosition, cameraOrient)
                .build();

        VisionPortal.Builder builder = new VisionPortal.Builder();

        builder.setCamera(hardwareMap.get(WebcamName.class, "Webcam 1"));

        builder.addProcessor(aprilTagProcessor);
        builder.setStreamFormat(VisionPortal.StreamFormat.MJPEG);
        builder.setCameraResolution(new Size(width, height));

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
    }
}
