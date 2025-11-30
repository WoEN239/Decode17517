package org.woen.RobotModule.Modules.TrajectoryFollower.Impls;

import static org.woen.Trajectory.Math.Line.LineSegment.lineFromTwoPoint;

import static java.lang.Math.abs;
import static java.lang.Math.signum;
import static java.lang.Math.sqrt;

import org.woen.Architecture.EventBus.EventBus;
import org.woen.Config.ControlSystemConstant;
import org.woen.Config.MatchData;
import org.woen.RobotModule.Modules.Localizer.Position.Architecture.RegisterNewLocalPositionListener;
import org.woen.RobotModule.Modules.Localizer.Position.Architecture.RegisterNewPositionListener;
import org.woen.RobotModule.Modules.TrajectoryFollower.Arcitecture.Feedback.FeedbackReference;
import org.woen.RobotModule.Modules.TrajectoryFollower.Arcitecture.Feedback.FeedbackReferenceObserver;
import org.woen.RobotModule.Modules.TrajectoryFollower.Arcitecture.Feedforward.FeedforwardReference;
import org.woen.RobotModule.Modules.TrajectoryFollower.Arcitecture.Feedforward.FeedforwardReferenceObserver;
import org.woen.RobotModule.Modules.TrajectoryFollower.Arcitecture.TargetSegment.NewTargetTrajectorySegmentEvent;
import org.woen.RobotModule.Modules.TrajectoryFollower.Interface.TrajectoryFollower;
import org.woen.Telemetry.Telemetry;
import org.woen.Trajectory.Math.Line.LineSegment;
import org.woen.Util.Vectors.Pose;
import org.woen.Util.Vectors.Vector2d;

import java.util.ArrayList;

public class PurePursuitFollowerImpl implements TrajectoryFollower {
    private ArrayList<LineSegment> targetPath;
    private double localRadius = ControlSystemConstant.feedbackConfig.PPLocalR;

    private double transVelocity = ControlSystemConstant.feedbackConfig.PPTransVel;
    private double endAngle = 0;
    private double endDetect = 5;
    private boolean isReverse = false;
    private boolean isEndNear = false;

    private Pose position =  MatchData.startPosition;
    private Pose localPosition = new Pose(0,0,0);

    @Override
    public void update() {
        if(targetPath.isEmpty()) return;

        localRadius = ControlSystemConstant.feedbackConfig.PPLocalR;
        if(position.vector.minus(targetPath.get(targetPath.size()-1).end).lengthSquare()<localRadius*localRadius){
            localRadius = position.vector.minus(targetPath.get(targetPath.size()-1).end).length();
        }

        Vector2d projection = targetPath.get(0).findProjection(position.vector);
        LineSegment targetSegment = targetPath.get(0);
        for (int i = 0; i<targetPath.size();i++) {
            Vector2d p = targetPath.get(i).findProjection(position.vector);
            if(localRadius*localRadius > p.minus(position.vector).lengthSquare()){
                projection = p;
                targetSegment = targetPath.get(i);
            }
        }


        double chord = localRadius;
        double step = sqrt(localRadius*localRadius - projection.minus(position.vector).lengthSquare());
        if(Double.isNaN(step)) {
            step = localRadius;
            chord = projection.plus(targetSegment.unitVector.multiply(step)).minus(position.vector).length();
        }

        Vector2d virtualTarget = projection.plus(targetSegment.unitVector.multiply(step));
        double y = virtualTarget.minus(position.vector).rotate(-position.h).y;

        double screwR = (chord*chord)/(2d*y);
        double dir = signum(targetSegment.end.minus(position.vector).rotate(-position.h).getAngle());
        double angleVel = dir*(transVelocity/screwR);

        if(isReverse){
            transVelocity = - abs(transVelocity);
            angleVel = -angleVel;
        }

        if(targetSegment == targetPath.get(targetPath.size()-1)){
        if(targetSegment.start.minus(projection).lengthSquare() > targetSegment.start.minus(targetSegment.end).lengthSquare()){
            transVelocity = - abs(transVelocity);
            angleVel = -angleVel;
        }
        }

        Pose targetPos = localPosition;

        if(position.vector.minus(targetPath.get(targetPath.size()-1).end).lengthSquare() < endDetect*endDetect){
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

        for(LineSegment i: targetPath) {
            Telemetry.getInstance().getField().line(i.start, i.end);
        }
        Telemetry.getInstance().getField().line(position.vector,projection);

    }

    private final FeedbackReferenceObserver observerPos = new FeedbackReferenceObserver();
    private final FeedforwardReferenceObserver observerVel = new FeedforwardReferenceObserver();

    @Override
    public void subscribeInit() {
        EventBus.getInstance().subscribe(NewTargetTrajectorySegmentEvent.class,this::setNewTrajectoryEvent);
    }

    private Vector2d laterPoint;
    private void setNewTrajectoryEvent(NewTargetTrajectorySegmentEvent e){

        ArrayList<LineSegment> targetBuild = new ArrayList<>();
        targetBuild.add(lineFromTwoPoint(laterPoint,e.getData().target[0].vector));

        for(int i =0; i<e.getData().target.length-1; i++ ){
            targetBuild.add(lineFromTwoPoint(e.getData().target[i].vector,e.getData().target[i+1].vector));
        }
        targetPath = targetBuild;

        laterPoint = e.getData().target[e.getData().target.length-1].vector;
        endAngle = e.getData().target[e.getData().target.length-1].h;
        endDetect = e.getData().getEndDetect();
        isReverse = e.getData().isReverse;
        isEndNear = false;
        transVelocity = e.getData().getVel();
    }

    @Override
    public void init() {
        EventBus.getListenersRegistration().invoke(new RegisterNewLocalPositionListener(this::setLocalPosition));
        EventBus.getListenersRegistration().invoke(new RegisterNewPositionListener(this::setPosition));
        laterPoint = MatchData.startPosition.vector;
    }

    private void setLocalPosition(Pose localPosition) {
        this.localPosition = localPosition;
    }

    private void setPosition(Pose position) {
        this.position = position;
    }

}
