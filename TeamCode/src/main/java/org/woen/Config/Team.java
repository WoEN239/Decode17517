package org.woen.Config;

import org.woen.Util.Vectors.Vector2d;

public enum Team {
    RED(new Vector2d(-172,172)), BLUE(new Vector2d(-172,-172));

    public final Vector2d goalPose;
    Team(Vector2d pose){
        this.goalPose = pose;
    }
}
