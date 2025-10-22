package org.woen.RobotModule.Modules.Gun;

import static org.woen.RobotModule.Modules.Gun.GunServoPositions.*;

public enum GUN_COMMAND {

    RAPIR_FIRE(shot,shot,shot,open),EAT(eat,eat,eat,close),
    SHOT_LEFT(shot,eat,eat,open),SHOT_CENTER(eat,shot,eat,open),SHOT_RIGHT(eat,eat,shot,open);

    public final double right ;
    public final double center;
    public final double left  ;
    public final double wall  ;

    GUN_COMMAND(double left, double center, double right, double wall){
        this.right = right;
        this.center = center;
        this.left = left;
        this.wall = wall;
    }

}
