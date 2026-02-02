package org.woen.Config;

import org.woen.Util.Vectors.Pose;

public enum Start {
    ZERO(new Pose(0,0,0)),
    FAR_BLUE(new Pose(0,158,-58)),
    NEAR_BLUE(new Pose(0.836,-122,-136)),
    FAR_RED (new Pose(0,158,-58).teamReverse()),
    NEAR_RED(new Pose(0.836,-122,-136).teamReverse());



    public final Pose pose;
    Start(Pose pose){
        this.pose = pose;
    }
}
