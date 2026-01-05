package org.woen.RobotModule.Modules.Gun.Config;

import static org.woen.RobotModule.Modules.Gun.Config.GunServoPositions.*;

public enum GUN_COMMAND {

    OFF(shotL, shotC, shotR,open),FULL_FIRE(shotL, shotC, shotR,open),PATTERN_FIRE(shotL,shotC,shotR,open)
    ,EAT(eatL, eatC, eatR,halfClose),REVERSE(eatL,eatC,eatR,open), PURPLE_FIRE(shotL,shotC,shotR,open),
    GREEN_FIRE(shotL,shotC,shotR,open), WAIT_SERVO(shotL,shotC,shotR,open),
    SHOT_LEFT(shotL, eatC, eatR,open),SHOT_CENTER(eatL, shotC, eatR,open),SHOT_RIGHT(eatL, eatC, shotR,open),
    TARGET(eatL, eatC, eatR,close);

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
