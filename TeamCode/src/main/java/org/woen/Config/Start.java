package org.woen.Config;

import static java.lang.Math.PI;

import org.woen.Util.Vectors.Pose;

public enum Start {
    ZERO(new Pose(0,0,0)),
    FAR_BLUE(new Pose(0,158,-58)),
    NEAR_BLUE(new Pose(-2.25+PI,-124,-134)),
    FAR_RED (new Pose(0,158,-58).teamReverse()),
    NEAR_RED(new Pose(-2.25+PI,-124,-134).teamReverse());



    public final Pose pose;
    Start(Pose pose){
        this.pose = pose;
    }
}
