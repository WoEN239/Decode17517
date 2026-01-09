package org.woen.Config;

import org.woen.Util.Vectors.Pose;

public enum Start {
    FAR_BLUE(new Pose(0,156,-57)),
    NEAR_BLUE(new Pose(0,156,-57)),
    FAR_RED (new Pose(0,156,-57).teamReverse()),
    NEAR_RED(new Pose(0,0,0).teamReverse());

    public final Pose pose;
    Start(Pose pose){
        this.pose = pose;
    }
}
