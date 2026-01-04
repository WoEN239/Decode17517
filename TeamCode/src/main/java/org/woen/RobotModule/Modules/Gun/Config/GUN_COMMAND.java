package org.woen.RobotModule.Modules.Gun.Config;

import static org.woen.RobotModule.Modules.Gun.Config.GunServoPositions.*;

public enum GUN_COMMAND {
    OFF(shotLPos, shotCPos, shotRPos,open),FULL_FIRE(shotLPos, shotCPos, shotRPos,open),PATTERN_FIRE(shotLPos, shotCPos, shotRPos,open),
    EAT(eatLPos, eatCPos, eatRPos,halfClose),REVERSE(eatLPos, eatCPos, eatRPos,open);

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
