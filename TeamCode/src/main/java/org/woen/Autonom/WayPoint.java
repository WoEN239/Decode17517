package org.woen.Autonom;

import org.woen.Architecture.EventBus.EventBus;
import org.woen.RobotModule.Modules.Localizer.Position.Architecture.RegisterNewPositionListener;
import org.woen.Util.Vectors.Pose;

public class WayPoint {
    public final AutonomTask onWay  ;
    public final AutonomTask onPoint;

    private Pose position;
    public final Pose target;
    private void setPosition(Pose position){this.position = position;}

    public final boolean isReverse;

    private double endDetect = 3;

    public double getVel() {
        return vel;
    }

    private double vel = 20;
    public double getEndDetect() {return endDetect;}

    private boolean isRunOnce = false;
    private boolean isEndNear = false;
    private boolean isDone    = false;

    public boolean isDone() {
        return isDone;
    }

    public boolean isEndNear() {
        return isEndNear;
    }

    public boolean isRunOnce() {
        return isRunOnce;
    }

    public void update(){
        isRunOnce = true;

        if(target.vector.minus(position.vector).length() < endDetect){
            isEndNear = true;
        }

        if(isEndNear && onWay.isDone()) {
            onPoint.run();
            if(onPoint.isDone()){
                isDone = true;
            }
        }else{
            onWay.run();
        }

    }

    public WayPoint(AutonomTask onPoint, Pose target,boolean isReverse){
        EventBus.getListenersRegistration().invoke(new RegisterNewPositionListener(this::setPosition));
        this.onPoint = onPoint;
        this.onWay = AutonomTask.Stub;
        this.target = target;
        this.isReverse = isReverse;
    }

    public WayPoint(AutonomTask onWay, AutonomTask onPoint, Pose target, boolean isReverse){
        EventBus.getListenersRegistration().invoke(new RegisterNewPositionListener(this::setPosition));
        this.onWay = onWay;
        this.onPoint = onPoint;
        this.target = target;
        this.isReverse = isReverse;
    }
    public WayPoint(AutonomTask onPoint, Pose target){
        EventBus.getListenersRegistration().invoke(new RegisterNewPositionListener(this::setPosition));
        this.onPoint = onPoint;
        this.onWay = AutonomTask.Stub;
        this.target = target;
        this.isReverse = false;
    }

    public WayPoint setEndDetect(double endDetect){
        this.endDetect = endDetect;
        return this;
    }
    public WayPoint setVel(double vel){
        this.vel = vel;
        return this;
    }

}
