package org.woen.Autonom.Architecture;

import org.woen.Util.Vectors.Pose;

public class WayPoint {
    public final AutonomTask onWay  ;
    public final AutonomTask onPoint;

    private Pose tagret  ;
    private Pose position;

    private double endDetect;

    private boolean isRunOnce = false;
    private boolean isEndNear = false;

    public void update(){
        isRunOnce = true;

        if(tagret.vector.minus(position.vector).length() < endDetect){
            isEndNear = true;
        }

        if(isEndNear) {
            onPoint.run();
        }else{
            onWay.run();
        }

    }

    public WayPoint(AutonomTask onWay, AutonomTask onPoint){

        this.onWay = onWay;
        this.onPoint = onPoint;
    }
}
