package org.woen.RobotModule.Modules.Gun.Config;

import org.woen.Util.Color.RgbColorVector;

public enum BallColor {
    GREEN (new RgbColorVector(3,8,5)),
    PURPLE(new RgbColorVector(3,6,6)),
    NONE  (new RgbColorVector(3,7,6 ));

    public final RgbColorVector color;

    BallColor(RgbColorVector color){
        this.color = color;
    }

}
