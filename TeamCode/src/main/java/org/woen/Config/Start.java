package org.woen.Config;

import org.woen.Util.Vectors.Pose;

public enum Start {
    ZERO(new Pose(0,0,0)),
    FAR_BLUE(new Pose(0,157.2,-58.5)),
    NEAR_BLUE(new Pose(0,-153,-100)),
    FAR_RED (new Pose(0,157.2,-58.5).teamReverse()),
    NEAR_RED(new Pose(0,-153,-100).teamReverse());



    public final Pose pose;
    Start(Pose pose){
        this.pose = pose;
    }
}
