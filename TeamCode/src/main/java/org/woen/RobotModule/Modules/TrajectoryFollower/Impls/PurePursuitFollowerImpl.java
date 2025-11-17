package org.woen.RobotModule.Modules.TrajectoryFollower.Impls;

import static org.woen.Trajectory.Math.Line.LineSegment.lineFromTwoPoint;

import static java.lang.Math.abs;
import static java.lang.Math.signum;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;

import org.woen.Architecture.EventBus.EventBus;
import org.woen.Config.ControlSystemConstant;
import org.woen.RobotModule.Modules.Localizer.Position.Architecture.RegisterNewLocalPositionListener;
import org.woen.RobotModule.Modules.Localizer.Position.Architecture.RegisterNewPositionListener;
import org.woen.RobotModule.Modules.TrajectoryFollower.Arcitecture.Feedback.FeedbackReference;
import org.woen.RobotModule.Modules.TrajectoryFollower.Arcitecture.Feedback.FeedbackReferenceObserver;
import org.woen.RobotModule.Modules.TrajectoryFollower.Arcitecture.Feedforward.FeedforwardReference;
import org.woen.RobotModule.Modules.TrajectoryFollower.Arcitecture.Feedforward.FeedforwardReferenceObserver;
import org.woen.RobotModule.Modules.TrajectoryFollower.Arcitecture.TargetSegment.NewTargetTrajectoryPointEvent;
import org.woen.RobotModule.Modules.TrajectoryFollower.Interface.TrajectoryFollower;
import org.woen.Telemetry.Telemetry;
import org.woen.Trajectory.Math.Line.LineSegment;
import org.woen.Util.Vectors.Pose;
import org.woen.Util.Vectors.Vector2d;

public class PurePursuitFollowerImpl implements TrajectoryFollower {
    private LineSegment targetSegment;
    private double localRadius = ControlSystemConstant.feedbackConfig.PPLocalR;

    private double transVelocity = ControlSystemConstant.feedbackConfig.PPTransVel;
    private double endAngle = 0;
    private double endDetect = 5;
    private boolean isReverse = false;
    private boolean isEndNear = false;


    private Pose position = new Pose(0,0,0);
    private Pose localPosition = new Pose(0,0,0);

    @Override
    public void update() {
        if(targetSegment == null) return;

        localRadius = ControlSystemConstant.feedbackConfig.PPLocalR;
        if(position.vector.minus(targetSegment.end).lengthSquare()<localRadius*localRadius){
            localRadius = position.vector.minus(targetSegment.end).length();
        }

        Telemetry.getInstance().setLineSegment(targetSegment);

        Vector2d projection = targetSegment.findProjection(position.vector);

        double screwR = (localRadius*localRadius)/(2d*projection.minus(position.vector).length());
        double angleVel = (transVelocity/screwR) *signum(targetSegment.end.minus(position.vector).rotate(-position.h).getAngle());

        if(isReverse){
            transVelocity = - abs(transVelocity);
            //angleVel = abs(angleVel);
        }

        Pose targetPos = localPosition;
        if(position.vector.minus(targetSegment.end).lengthSquare() < endDetect*endDetect){
            isEndNear = true;
        }
        if(isEndNear){
            targetPos = new Pose(endAngle,localPosition.vector);
            angleVel = 0;
            transVelocity = 0;
        }


        observerPos.notifyListeners(new FeedbackReference(
                        targetPos,
                        new Pose(0,0,0)
                )
        );
        observerVel.notifyListeners(new FeedforwardReference(
                        new Pose(angleVel,transVelocity,0),
                        new Pose(0,0,0)
                )
        );

        Telemetry.getInstance().getTelemetryPacket().fieldOverlay().setScale(1/2.54, 1/2.54);
        Telemetry.getInstance().getTelemetryPacket().fieldOverlay().strokeLine(
                position.x,-position.y,projection.x,-projection.y
        );

    }

    private final FeedbackReferenceObserver observerPos = new FeedbackReferenceObserver();
    private final FeedforwardReferenceObserver observerVel = new FeedforwardReferenceObserver();

    @Override
    public void subscribeInit() {
        EventBus.getInstance().subscribe(NewTargetTrajectoryPointEvent.class,this::setNewTrajectoryPoint);
    }

    private Vector2d laterPoint;
    private void setNewTrajectoryPoint(NewTargetTrajectoryPointEvent e){
        if(laterPoint != null){
            targetSegment = lineFromTwoPoint(laterPoint,e.getData().target.vector);
        }
        laterPoint = e.getData().target.vector;
        endAngle = e.getData().target.h;
        endDetect = e.getData().getEndDetect();
        isReverse = e.getData().isReverse;
        isEndNear = false;
        transVelocity = e.getData().getVel();
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
