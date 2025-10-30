package org.woen.Autonom;

import org.woen.Architecture.EventBus.EventBus;
import org.woen.RobotModule.Modules.Localizer.Position.Architecture.RegisterNewLocalPositionListener;
import org.woen.Util.MotionProfile.TrapezoidMotionProfile;
import org.woen.Util.Vectors.Pose;

import java.util.ArrayList;
import java.util.Arrays;

public class AutonomTaskManagerImpl implements AutonomTaskManager {
    private final ArrayList<WayPoint> wayPoints = new ArrayList<>();
    private void setWayPoints(SetNewTrajectoryEvent event){
        wayPoints.addAll(Arrays.asList(event.getData()));
     //   profile = new TrapezoidMotionProfile(position.);
    }

    private Pose position = new Pose(0,0,0);
    private Pose target;
    private void setPosition(Pose position){this.position = position;}
    private TrapezoidMotionProfile profileX = new TrapezoidMotionProfile(0,0,0,0,0);
    private TrapezoidMotionProfile profileH = new TrapezoidMotionProfile(0,0,0,0,0);

    public void update(){
        if(!wayPoints.isEmpty()){
            wayPoints.get(0).update();
            if(wayPoints.get(0).isDone()){
                wayPoints.remove(0);
            }
        }
    }

    @Override
    public void subscribeInit() {
        EventBus.getInstance().subscribe(SetNewTrajectoryEvent.class,this::setWayPoints);
    }

    @Override
    public void init(){
        EventBus.getListenersRegistration().invoke(new RegisterNewLocalPositionListener(this::setPosition));
    }
}
