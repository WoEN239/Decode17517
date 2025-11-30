package org.woen.Autonom;

import org.woen.Architecture.EventBus.EventBus;

import org.woen.RobotModule.Modules.TrajectoryFollower.Arcitecture.TargetSegment.NewTargetTrajectorySegmentEvent;

import java.util.ArrayList;
import java.util.Arrays;

public class AutonomTaskManagerImpl implements AutonomTaskManager {
    private ArrayList<WayPoint> wayPoints = new ArrayList<>();
    private void setWayPoints(SetNewTrajectoryEvent event){
        wayPoints = new ArrayList<>(Arrays.asList(event.getData()));
        EventBus.getInstance().invoke(new NewTargetTrajectorySegmentEvent(wayPoints.get(0)));
        EventBus.getInstance().invoke(new NewTargetTrajectorySegmentEvent(wayPoints.get(1)));
    }

    public void update(){
        if(wayPoints.size()<2) return;

        wayPoints.get(1).update();

        if(wayPoints.get(1).isDone()){
            wayPoints.remove(0);
            if(wayPoints.size()<2) return;
            EventBus.getInstance().invoke(new NewTargetTrajectorySegmentEvent(wayPoints.get(1)));
        }

    }

    @Override
    public void subscribeInit() {
        EventBus.getInstance().subscribe(SetNewTrajectoryEvent.class,this::setWayPoints);
    }

}
