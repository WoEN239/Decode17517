package org.woen.RobotModule.Modules.Gun.Config;

import org.woen.Util.Color.RgbColorVector;

public enum BallColor {
    GREEN (new RgbColorVector(23,40,30)),
    PURPLE(new RgbColorVector(23,41,39)),
    NONE  (new RgbColorVector(28,48,40));

    public final RgbColorVector color;

    BallColor(RgbColorVector color){
        this.color = color;
    }

}
