package org.woen.RobotModule.Modules.TrajectoryFollower.Impls;

import static org.woen.Config.ControlSystemConstant.*;
import static org.woen.Trajectory.Math.Line.LineSegment.lineFromTwoPoint;

import org.woen.RobotModule.Modules.TrajectoryFollower.Arcitecture.Feedback.FeedbackReference;
import org.woen.RobotModule.Modules.TrajectoryFollower.Arcitecture.Feedback.FeedbackReferenceObserver;
import org.woen.RobotModule.Modules.TrajectoryFollower.Interface.TrajectoryFollower;
import org.woen.Trajectory.Math.Line.LineSegment;
import org.woen.Util.MotionProfile.TrapezoidMotionProfile;
import org.woen.Util.Vectors.Pose;
import org.woen.Util.Vectors.Vector2d;

public class PurePuresuitFollowerImpl implements TrajectoryFollower {
    private FeedbackReferenceObserver observer = new FeedbackReferenceObserver();
    private LineSegment targetSegment = new LineSegment(0,0,0,0);
    private double targetAngle = 0;
    private Pose position = new Pose(0,0,0);
    private Pose localVelocity = new Pose(0,0,0);
    private double localRadius = 0;

    private double endDetect = 0;

    private boolean isEndNear = false;


    @Override
    public void update() {
        Vector2d projection = targetSegment.findProjection(position.vector);
        Vector2d virtualTarget
                = projection.plus(lineFromTwoPoint(projection,targetSegment.end).unitVector.multiply(localRadius));

        double targetAngle = lineFromTwoPoint(position.vector,virtualTarget).lineAngle;
        observer.notifyListeners(new FeedbackReference(new Pose(targetAngle,position.vector.minus(virtualTarget).length(),0),new Pose(0,0,0)));
    }
}
