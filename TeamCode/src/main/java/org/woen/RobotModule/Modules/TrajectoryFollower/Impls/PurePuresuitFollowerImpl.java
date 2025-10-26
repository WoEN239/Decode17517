package org.woen.RobotModule.Modules.TrajectoryFollower.Impls;

import static java.lang.Math.abs;

import org.woen.RobotModule.Modules.TrajectoryFollower.Arcitecture.Feedback.FeedbackReference;
import org.woen.RobotModule.Modules.TrajectoryFollower.Arcitecture.Feedback.FeedbackReferenceObserver;
import org.woen.RobotModule.Modules.TrajectoryFollower.Interface.TrajectoryFollower;
import org.woen.Trajectory.Math.Line.LineSegment;
import org.woen.Util.Angel.AngelUtil;
import org.woen.Util.Vectors.Pose;
import org.woen.Util.Vectors.Vector2d;

public class PurePuresuitFollowerImpl implements TrajectoryFollower {
    private FeedbackReferenceObserver observer = new FeedbackReferenceObserver();
    private LineSegment targetSegment = new LineSegment(0,0,0,0);
    private double targetAngle = 0;
    private Pose position = new Pose(0,0,0);
    private double localRadius = 0;
    private double localRadiusAngle = 0;
    private double endDetectY = 0;
    private double endDetectX = 0;
    private double endDetectAngle = 0;
    private boolean isEndNear = false;
    @Override
    public void update(){
        Vector2d projection = targetSegment.findProjection(position.vector);

        LineSegment unUnitTargetVector = LineSegment.makeFromTwoPoint(projection,targetSegment.end);
        Vector2d unitTargetVector = unUnitTargetVector.unitVector;

        Vector2d linearU =  projection.plus(unitTargetVector).multiply(localRadius);

        double xError = position.vector.minus(targetSegment.end).x;
        double yError = position.vector.minus(targetSegment.end).y;
        boolean linearEndNearX = false;
        boolean linearEndNearY = false;

        if( abs(xError) < endDetectX){
            linearEndNearX = true;
            linearU = new Vector2d(targetSegment.end.x,linearU.y);
        }
        if( abs(yError) < endDetectY){
            linearEndNearY = true;
            linearU = new Vector2d(linearU.x,targetSegment.end.y);
        }

        double angleU =  position.h + localRadiusAngle * Math.signum(targetAngle - position.h);
        boolean angleEndNear = false;

        if( abs(AngelUtil.normalize(targetAngle - position.h)) < endDetectAngle ) {
            angleEndNear = true;
            angleU = targetAngle;
        }

        isEndNear = angleEndNear&&linearEndNearX&&linearEndNearY;

        observer.notifyListeners(new FeedbackReference(
                new Pose(angleU,linearU.x,linearU.y),new Pose(0,0,0)));

    }
}
