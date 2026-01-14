package org.woen.Config;

import static java.lang.Math.PI;

import org.woen.Util.Vectors.Pose;

public enum Start {
    FAR_BLUE(new Pose(0,156.7,-63.5)),
    NEAR_BLUE(new Pose(-2.25+PI,-128,-134)),
    FAR_RED (new Pose(0,156.7,-63.5).teamReverse()),
    NEAR_RED(new Pose(-2.25+PI,-124,-134).teamReverse());



    public final Pose pose;
    Start(Pose pose){
        this.pose = pose;
    }
}
