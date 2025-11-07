package org.woen.RobotModule.Modules.TrajectoryFollower.Impls;

import static org.woen.Trajectory.Math.Line.LineSegment.lineFromTwoPoint;

import org.woen.Architecture.EventBus.EventBus;
import org.woen.Config.ControlSystemConstant;
import org.woen.RobotModule.Modules.Localizer.Position.Architecture.RegisterNewLocalPositionListener;
import org.woen.RobotModule.Modules.Localizer.Position.Architecture.RegisterNewPositionListener;
import org.woen.RobotModule.Modules.TrajectoryFollower.Arcitecture.Feedback.FeedbackReference;
import org.woen.RobotModule.Modules.TrajectoryFollower.Arcitecture.Feedback.FeedbackReferenceObserver;
import org.woen.RobotModule.Modules.TrajectoryFollower.Arcitecture.TargetSegment.NewTargetTrajectoryPointEvent;
import org.woen.RobotModule.Modules.TrajectoryFollower.Interface.TrajectoryFollower;
import org.woen.Trajectory.Math.Line.LineSegment;
import org.woen.Util.Vectors.Pose;
import org.woen.Util.Vectors.Vector2d;

public class PurePursuitFollowerImpl implements TrajectoryFollower {
    private LineSegment targetSegment;
    private double localRadius = ControlSystemConstant.FeedbackConfig.PPLocalR;

    private Pose position = new Pose(0,0,0);
    private Pose localPosition = new Pose(0,0,0);

    @Override
    public void update() {
        if(targetSegment == null) return;

        Vector2d projection = targetSegment.findProjection(position.vector);

        double k =  Math.sqrt(localRadius*localRadius - position.vector.minus(projection).lengthSquare());
        if(Double.isNaN(k)) k = localRadius;
        Vector2d virtualTarget = projection.plus(lineFromTwoPoint(projection,targetSegment.end).unitVector.multiply(k));
        double targetAngle = virtualTarget.minus(position.vector).getAngle();
        double targetX  = localRadius + localPosition.x;

        observer.notifyListeners(new FeedbackReference(
                new Pose(targetAngle,targetX,0),
                new Pose(0,0,0)
                )
        );

    }

    private final FeedbackReferenceObserver observer = new FeedbackReferenceObserver();

    @Override
    public void subscribeInit() {
        EventBus.getInstance().subscribe(NewTargetTrajectoryPointEvent.class,this::setNewTrajectoryPoint);
    }

    private Vector2d laterPoint;
    private void setNewTrajectoryPoint(NewTargetTrajectoryPointEvent e){
        if(laterPoint != null){
            targetSegment = lineFromTwoPoint(laterPoint,e.getData().position);
        }
        laterPoint = e.getData().position;
    }

    @Override
    public void init() {
        EventBus.getListenersRegistration().invoke(new RegisterNewLocalPositionListener(this::setLocalPosition));
        EventBus.getListenersRegistration().invoke(new RegisterNewPositionListener(this::setPosition));
    }

    private void setLocalPosition(Pose localPosition) {
        this.localPosition = localPosition;
    }
    private void setPosition(Pose position) {
        this.position = position;
    }

}
