package org.firstinspires.ftc.teamcode;

import com.pedropathing.geometry.Pose;

public class Utility {
    public static Pose revertPose(Pose pose){
        return new Pose(pose.getX(), -pose.getY(), pose.getHeading());
    }

}
