package org.woen.Autonom.Architecture;

import com.qualcomm.robotcore.util.RobotLog;

import org.woen.Architecture.EventBus.EventBus;

import org.woen.RobotModule.Modules.TrajectoryFollower.Arcitecture.TargetSegment.SetNewTargetTrajectorySegmentEvent;
import org.woen.Telemetry.Telemetry;

import java.util.ArrayList;
import java.util.Arrays;

public class WaypointsManagerImpl implements WaypointsManager {
    private ArrayList<WayPoint> wayPoints = new ArrayList<>();

    private void setWayPoints(SetNewWaypointsSequenceEvent event){
        wayPoints = new ArrayList<>(Arrays.asList(event.getData()));
        EventBus.getInstance().invoke(new SetNewTargetTrajectorySegmentEvent(wayPoints.get(0)));
    }

    public void update(){
        if(wayPoints.isEmpty()) {
            RobotLog.dd("end_of_trajectory","actual waypoints sequence finished");
            return;
        }

        wayPoints.get(0).update();
        Telemetry.getInstance().add("is waypoint update","update");
        Telemetry.getInstance().add("is waypoint done",wayPoints.get(0).isDone());
        Telemetry.getInstance().add("waypoints amount",wayPoints.size());
        if(wayPoints.get(0).isDone()){
            if(wayPoints.size()>1) {
                RobotLog.dd("waypoint_change", "change waypoint from " + wayPoints.get(0).getName() + " to " + wayPoints.get(1).getName());
            }else{
                RobotLog.dd("waypoint_change", "change waypoint from " + " this is end");
            }
            wayPoints.remove(0);
            if(!wayPoints.isEmpty()) {
                EventBus.getInstance().invoke(new SetNewTargetTrajectorySegmentEvent(wayPoints.get(0)));
            }
        }

    }

    @Override
    public void subscribeInit() {
        EventBus.getInstance().subscribe(SetNewWaypointsSequenceEvent.class,this::setWayPoints);
    }

}
