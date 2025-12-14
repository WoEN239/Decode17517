package org.woen.Autonom;

import com.qualcomm.robotcore.util.RobotLog;

import org.woen.Architecture.EventBus.EventBus;

import org.woen.Config.MatchData;
import org.woen.RobotModule.Modules.TrajectoryFollower.Arcitecture.TargetSegment.SetNewTargetTrajectorySegmentEvent;

import java.util.ArrayList;
import java.util.Arrays;
/*
     waypoints has a view : latter waypoint , actual target waypoint, next waypoints
*/
public class WaypointsManagerImpl implements WaypointsManager {
    private ArrayList<WayPoint> wayPoints = new ArrayList<>();
    {
        wayPoints.add(new WayPoint(AutonomTask.Stub,MatchData.startPosition));
    }
    private void setWayPoints(SetNewWaypointsSequenceEvent event){
        wayPoints = new ArrayList<>(Arrays.asList(event.getData()));
        EventBus.getInstance().invoke(new SetNewTargetTrajectorySegmentEvent(wayPoints.get(0)));
        EventBus.getInstance().invoke(new SetNewTargetTrajectorySegmentEvent(wayPoints.get(1)));
    }

    public void update(){
        if(wayPoints.size()<2) {
            RobotLog.dd("end_of_trajectory","actual waypoints sequence finished");
            return;
        }

        wayPoints.get(1).update();

        if(wayPoints.get(1).isDone()){
            if(wayPoints.size()>2) {
                RobotLog.dd("waypoint_change", "change waypoint from " + wayPoints.get(1) + " to " + wayPoints.get(2));
            }
            wayPoints.remove(0);
            if(wayPoints.size()>1) {
                EventBus.getInstance().invoke(new SetNewTargetTrajectorySegmentEvent(wayPoints.get(1)));
            }
        }

    }

    @Override
    public void subscribeInit() {
        EventBus.getInstance().subscribe(SetNewWaypointsSequenceEvent.class,this::setWayPoints);
    }

}
