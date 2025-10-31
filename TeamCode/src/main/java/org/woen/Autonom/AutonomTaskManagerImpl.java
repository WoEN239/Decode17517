package org.woen.Autonom;

import com.qualcomm.robotcore.util.ElapsedTime;

import org.woen.Architecture.EventBus.EventBus;
import org.woen.Config.ControlSystemConstant;
import org.woen.RobotModule.Modules.Localizer.Position.Architecture.RegisterNewLocalPositionListener;
import org.woen.RobotModule.Modules.Localizer.Velocity.Architecture.RegisterNewLocalVelocityListener;
import org.woen.RobotModule.Modules.TrajectoryFollower.Arcitecture.Feedback.FeedbackReference;
import org.woen.RobotModule.Modules.TrajectoryFollower.Arcitecture.Feedback.FeedbackReferenceObserver;
import org.woen.RobotModule.Modules.TrajectoryFollower.Arcitecture.Feedforward.FeedforwardReference;
import org.woen.RobotModule.Modules.TrajectoryFollower.Arcitecture.Feedforward.FeedforwardReferenceObserver;
import org.woen.Util.MotionProfile.TrapezoidMotionProfile;
import org.woen.Util.Vectors.Pose;

import java.util.ArrayList;
import java.util.Arrays;

public class AutonomTaskManagerImpl implements AutonomTaskManager {
    private final ArrayList<WayPoint> wayPoints = new ArrayList<>();
    private void setWayPoints(SetNewTrajectoryEvent event){
        wayPoints.addAll(Arrays.asList(event.getData()));
    }

    private final FeedforwardReferenceObserver feedforwardObserver = new FeedforwardReferenceObserver();
    private final FeedbackReferenceObserver feedbackObserver = new FeedbackReferenceObserver();

    private Pose localPosition = new Pose(0,0,0);
    private Pose localVelocity = new Pose(0,0,0);

    private void setLocalPosition(Pose localPosition){this.localPosition = localPosition;}
    public void setLocalVelocity(Pose localVelocity) {this.localVelocity = localVelocity;}

    private TrapezoidMotionProfile profileX = new TrapezoidMotionProfile(0,0,0,0,0);
    private TrapezoidMotionProfile profileH = new TrapezoidMotionProfile(0,0,0,0,0);

    private ElapsedTime timer = new ElapsedTime();
    private boolean isRunOnce = false;
    public void update(){
        if(!wayPoints.isEmpty()){
            if(!isRunOnce){
                timer.reset();
                isRunOnce = true;
            }

            wayPoints.get(0).update();
            Pose target = wayPoints.get(0).target;

            profileX = new TrapezoidMotionProfile(ControlSystemConstant.transAccel,ControlSystemConstant.transVel,
                    localPosition.vector.x, target.vector.x,localVelocity.vector.x);

            profileH = new TrapezoidMotionProfile(ControlSystemConstant.angleAccel,ControlSystemConstant.angleVel,
                    localPosition.h, target.h,localVelocity.vector.x);

            double t = timer.seconds();
            Pose targetPosition = new Pose(profileH.getPos(t),profileX.getPos(t),0);
            Pose targetVelocity = new Pose(profileH.getVel(t),profileX.getVel(t),0);
            Pose targetAccel    = new Pose(profileH.getAccel(t),profileX.getAccel(t),0);

            feedbackObserver.notifyListeners(
                new FeedbackReference(targetPosition,targetVelocity)
            );
            feedforwardObserver.notifyListeners(
                    new FeedforwardReference(targetVelocity,targetAccel)
            );

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
        EventBus.getListenersRegistration().invoke(new RegisterNewLocalPositionListener(this::setLocalPosition));
        EventBus.getListenersRegistration().invoke(new RegisterNewLocalVelocityListener(this::setLocalVelocity));
    }
}
