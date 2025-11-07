package org.woen.Autonom;

import org.woen.Architecture.EventBus.EventBus;

import org.woen.RobotModule.Modules.TrajectoryFollower.Arcitecture.TargetSegment.NewTargetTrajectoryPointEvent;
import org.woen.Trajectory.TrajectoryPoint;

import java.util.ArrayList;
import java.util.Arrays;

public class AutonomTaskManagerImpl implements AutonomTaskManager {
    private ArrayList<WayPoint> wayPoints = new ArrayList<>();
    private void setWayPoints(SetNewTrajectoryEvent event){
        wayPoints = new ArrayList<>(Arrays.asList(event.getData()));
        EventBus.getInstance().invoke(new NewTargetTrajectoryPointEvent(
                new TrajectoryPoint(wayPoints.get(0).target.vector)));
        EventBus.getInstance().invoke(new NewTargetTrajectoryPointEvent(
                new TrajectoryPoint(wayPoints.get(1).target.vector)));
    }

    public void update(){
        if(wayPoints.isEmpty()) return;

        wayPoints.get(0).update();


        if(wayPoints.get(0).isDone()){
            wayPoints.remove(0);
            EventBus.getInstance().invoke(new NewTargetTrajectoryPointEvent(
                    new TrajectoryPoint(wayPoints.get(0).target.vector)));
        }

    }

    @Override
    public void subscribeInit() {
        EventBus.getInstance().subscribe(SetNewTrajectoryEvent.class,this::setWayPoints);
    }

}
