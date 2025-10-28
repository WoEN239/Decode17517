package org.woen.RobotModule.Modules.Gun;

import static org.woen.RobotModule.Modules.Gun.GunServoPositions.*;

public enum GUN_COMMAND {

    RAPID_FIRE(shotL, shotC, shotR,open),FULL_FIRE(shotL, shotC, shotR,open),EAT(eatL, eatC, eatR,halfClose),
    SHOT_LEFT(shotL, eatC, eatR,open),SHOT_CENTER(eatL, shotC, eatR,open),SHOT_RIGHT(eatL, eatC, shotR,open),
    TARGET(eatL, eatC, eatR,close),  PATTERN_FIRE(shotL, shotC, shotR,open), PPG(shotL, shotC, shotR,open),
    PGP(shotL, shotC, shotR,open),GPP(shotL, shotC, shotR,open);

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
