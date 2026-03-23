package org.woen.Config;

import org.woen.Util.Vectors.Vector2d;

public enum Team {
    RED(new Vector2d(-172,172),new Vector2d(-172,160)),
    BLUE(new Vector2d(-172,-172),new Vector2d(-172,-160));

    public final Vector2d goalPose;
    public final Vector2d farGoalPose;
    Team(Vector2d pose,Vector2d farPose){
        this.goalPose = pose;
        this.farGoalPose = farPose;
    }
}
