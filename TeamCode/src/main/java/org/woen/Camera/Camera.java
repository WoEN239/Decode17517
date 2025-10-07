package org.woen.Camera;

import android.icu.text.Transliterator;
import android.util.Size;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose3D;
import org.firstinspires.ftc.robotcore.external.navigation.Position;
import org.firstinspires.ftc.robotcore.external.navigation.YawPitchRollAngles;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;
import org.opencv.core.Mat;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvPipeline;

import java.util.List;


@Config
public class Camera {

    //TODO static pos
    private Position cameraPosition = new Position(DistanceUnit.METER,
            0, 0, 0, 0);

    private YawPitchRollAngles cameraOrient = new YawPitchRollAngles(AngleUnit.DEGREES,
            0, -90, 0, 0);

    private AprilTagProcessor aprilTag;

    private VisionPortal visionPortal;

    private List<AprilTagDetection> detectionList;

    OpenCvCamera openCvCamera;

    private int id = 0;

    public int id22 = 22;

    public int id21 = 21;

    public int id23 = 23;

    public static int height = 640;

    public static int width = 480;


    public void initAprilTag(HardwareMap hardwareMap) {
        aprilTag = new AprilTagProcessor.Builder()
                .setCameraPose(cameraPosition, cameraOrient)
                .build();


        VisionPortal.Builder builder = new VisionPortal.Builder();

        builder.setCamera(hardwareMap.get(WebcamName.class, "Webcam1"));

        builder.addProcessor(aprilTag);

        builder.setCameraResolution(new Size(width, height));

        visionPortal = builder.build();
    }


    public void closePortal() {
        visionPortal.close();
    }


    public int getId(){
        return id;
    }

    Pose3D pos = null;

    public Pose3D getDistance(){
        return pos;
    }

    public void update() {
        List<AprilTagDetection> currentDetectionList = aprilTag.getDetections();

        if (currentDetectionList.size() != 0) {
            for (AprilTagDetection tag : currentDetectionList) {
                if (tag.id == id21 || tag.id == id22 || tag.id == id23) {
                    id = tag.id;
                    pos = tag.robotPose;
                    break;
                }
            }
        }
    }
}
