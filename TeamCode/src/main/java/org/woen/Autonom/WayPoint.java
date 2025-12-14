package org.woen.Autonom;

import com.qualcomm.robotcore.util.RobotLog;

import org.woen.Architecture.EventBus.EventBus;
import org.woen.Config.ControlSystemConstant;
import org.woen.RobotModule.Modules.Localizer.Position.Architecture.RegisterNewPositionListener;
import org.woen.Util.Vectors.Pose;

public class WayPoint {
    private String name = "unnamed";
    public String getName() {return name;}

    public final AutonomTask onWay  ;
    public final AutonomTask onPoint;

    private Pose pose;
    public final Pose[] path;
    private void setPose(Pose pose){this.pose = pose;}

    public final boolean isReverse;

    private double endDetect = 3;
    public double getEndDetect() {return endDetect;}

    private double vel = ControlSystemConstant.feedbackConfig.PPTransVel;
    public double getVel() {return vel;}

    private boolean isEndNear  = false;
    public boolean isEndNear() {return isEndNear;}

    private boolean isDone     = false;
    public boolean isDone() {
        return isDone;
    }


    public void update(){
        if(path[path.length-1].vector.minus(pose.vector).length() < endDetect){
            if(!isEndNear) {
                RobotLog.dd("end_of_path_segment", "in waypoint " + name + " end detected");
            }
            isEndNear = true;
        }

        if(isEndNear && onWay.isDone()) {
            onPoint.run();
            if(onPoint.isDone()){
                if(!isDone) {
                    RobotLog.dd("task_end", "in waypoint " + name + " task end");
                }
                isDone = true;
            }
        }else{
            onWay.run();
        }
    }

    public WayPoint(AutonomTask onPoint, boolean isReverse, Pose... path){
        EventBus.getListenersRegistration().invoke(new RegisterNewPositionListener(this::setPose));
        this.onPoint = onPoint;
        this.onWay = AutonomTask.Stub;
        this.path = path;
        this.isReverse = isReverse;
    }
    public WayPoint(AutonomTask onWay, AutonomTask onPoint, boolean isReverse, Pose... path){
        EventBus.getListenersRegistration().invoke(new RegisterNewPositionListener(this::setPose));
        this.onWay = onWay;
        this.onPoint = onPoint;
        this.path = path;
        this.isReverse = isReverse;
    }
    public WayPoint(AutonomTask onPoint, Pose... path){
        EventBus.getListenersRegistration().invoke(new RegisterNewPositionListener(this::setPose));
        this.onPoint = onPoint;
        this.onWay = AutonomTask.Stub;
        this.path = path;
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
    public WayPoint setName(String name){
        this.name = name;
        return this;
    }

}
