package org.woen.RobotModule.Camera;

import android.util.Size;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Position;
import org.firstinspires.ftc.robotcore.external.navigation.YawPitchRollAngles;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagPoseFtc;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;
import org.woen.Config.Team;

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


    private int id = 0;

    public int id22 = 22;

    public int id21 = 21;

    public int id23 = 23;

    public static int height = 600;

    public static int width = 800;

    public static Team TEAM = null;

    Position position = null;

    YawPitchRollAngles orient = null;

    AprilTagPoseFtc pos = new AprilTagPoseFtc(0,0,0,0,0,0,0,0,0);

    public void initAprilTag(HardwareMap hardwareMap, Team TEAM) {
        aprilTag = new AprilTagProcessor.Builder()
                .setCameraPose(cameraPosition, cameraOrient)
                .build();

        this.TEAM = TEAM;

        this.position = position;

        this.orient = orient;

        VisionPortal.Builder builder = new VisionPortal.Builder();

        builder.setCamera(hardwareMap.get(WebcamName.class, "Webcam 1"));

        builder.addProcessor(aprilTag);

        builder.setCameraResolution(new Size(width, height));

        visionPortal = builder.build();
    }


    public void closePortal() {
        visionPortal.close();
    }


    public int getId() {
        return id;
    }


    public AprilTagPoseFtc getDistance() {
        return pos;
    }

    public void update() {
        List<AprilTagDetection> currentDetectionList = aprilTag.getDetections();

        if (!currentDetectionList.isEmpty()) {
            for (AprilTagDetection tag : currentDetectionList) {
                if (tag.id == id21 || tag.id == id22 || tag.id == id23) {
                    id = tag.id;
                    break;
                }
                if (tag.id == 24 && TEAM == Team.RED) {
                    pos = tag.ftcPose;
                    break;
                }
                if (tag.id == 20 && TEAM == Team.BLUE) {
                    pos = tag.ftcPose;
                    break;
                }
            }
        }
    }
}
