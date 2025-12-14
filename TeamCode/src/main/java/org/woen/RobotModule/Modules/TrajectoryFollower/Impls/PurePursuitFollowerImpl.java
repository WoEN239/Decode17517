package org.woen.RobotModule.Modules.TrajectoryFollower.Impls;

import static org.woen.Util.Trajectory.Math.Line.LineSegment.lineFromTwoPoint;

import static java.lang.Math.abs;
import static java.lang.Math.signum;
import static java.lang.Math.sqrt;

import com.qualcomm.robotcore.util.RobotLog;

import org.woen.Architecture.EventBus.EventBus;
import org.woen.Config.ControlSystemConstant;
import org.woen.Config.MatchData;
import org.woen.RobotModule.Modules.Localizer.Position.Architecture.RegisterNewPositionListener;
import org.woen.RobotModule.Modules.TrajectoryFollower.Arcitecture.Feedback.FeedbackReference;
import org.woen.RobotModule.Modules.TrajectoryFollower.Arcitecture.Feedback.FeedbackReferenceObserver;
import org.woen.RobotModule.Modules.TrajectoryFollower.Arcitecture.Feedforward.FeedforwardReference;
import org.woen.RobotModule.Modules.TrajectoryFollower.Arcitecture.Feedforward.FeedforwardReferenceObserver;
import org.woen.RobotModule.Modules.TrajectoryFollower.Arcitecture.TargetSegment.SetNewTargetTrajectorySegmentEvent;
import org.woen.RobotModule.Modules.TrajectoryFollower.Interface.TrajectoryFollower;
import org.woen.Telemetry.Telemetry;
import org.woen.Util.Trajectory.Math.Line.LineSegment;
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

    private Pose pose =  MatchData.startPosition;
    private void setPose(Pose pose) {this.pose = pose;}

    @Override
    public void update() {
        if(targetPath.isEmpty()) {
            RobotLog.dd("target_pp_path_finished","path finished");
            return;
        }

        localRadius = ControlSystemConstant.feedbackConfig.PPLocalR;
        Vector2d lastPoint = targetPath.get(targetPath.size()-1).end;
        double distanceToEnd = pose.vector.minus(lastPoint).length();

        if(distanceToEnd < localRadius){
            localRadius = distanceToEnd;
        }

        Vector2d projection = targetPath.get(0).findProjection(pose.vector);
        LineSegment targetSegment = targetPath.get(0);
        boolean isSegmentLast = false;
        for (int i = 0; i<targetPath.size();i++) {
            Vector2d p = targetPath.get(i).findProjection(pose.vector);
            if(localRadius*localRadius > p.minus(pose.vector).lengthSquare()){
                projection = p;
                targetSegment = targetPath.get(i);

                if(i == targetPath.size()-1){
                    isSegmentLast = true;
                }
            }
        }


        double chord = localRadius;
        double step = sqrt(localRadius*localRadius - projection.minus(pose.vector).lengthSquare());
        if(Double.isNaN(step)) {
            step = localRadius;
            chord = projection.plus(targetSegment.unitVector.multiply(step)).minus(pose.vector).length();
        }

        Vector2d virtualTarget = projection.plus(targetSegment.unitVector.multiply(step));
        double y = virtualTarget.minus(pose.vector).rotate(-pose.h).y;

        double screwR = (chord*chord)/(2d*y);
        double dir = signum(targetSegment.end.minus(pose.vector).rotate(-pose.h).getAngle());
        double angleVel = dir*(transVelocity/screwR);

        if(isReverse){
            transVelocity = - abs(transVelocity);
            angleVel = -angleVel;
        }

        if(isSegmentLast){
            if(targetSegment.start.minus(projection).lengthSquare() >
               targetSegment.start.minus(targetSegment.end).lengthSquare()){
                transVelocity = -transVelocity;
                angleVel = -angleVel;
            }
        }

        Pose targetPos = pose;

        if(pose.vector.minus(lastPoint).lengthSquare() < endDetect*endDetect){
            isEndNear = true;
        }

        if(isEndNear){
            targetPos = new Pose(endAngle,pose.vector);
            angleVel = 0;
            transVelocity = 0;
        }

        observerVel.notifyListeners(new FeedforwardReference(
                        new Pose(angleVel,transVelocity,0),
                        new Pose(0,0,0)
                )
        );
        observerPos.notifyListeners(new FeedbackReference(
                new Pose(targetPos.h,0,0),
                new Pose(0,0,0)
        ));

        for(LineSegment i: targetPath) {
            Telemetry.getInstance().getField().line(i.start, i.end);
        }
        Telemetry.getInstance().getField().line(pose.vector,projection);

    }

    private final FeedbackReferenceObserver observerPos = new FeedbackReferenceObserver();
    private final FeedforwardReferenceObserver observerVel = new FeedforwardReferenceObserver();

    @Override
    public void subscribeInit() {
        EventBus.getInstance().subscribe(SetNewTargetTrajectorySegmentEvent.class,this::setNewTrajectoryEvent);
    }

    private void setNewTrajectoryEvent(SetNewTargetTrajectorySegmentEvent e){
        ArrayList<LineSegment> buildPath = new ArrayList<>();

        for(int i = 0; i<e.getData().path.length-1; i++ ){
            buildPath.add(lineFromTwoPoint(e.getData().path[i].vector,e.getData().path[i+1].vector));
        }
        targetPath = buildPath;

        isEndNear = false;

        endAngle = e.getData().path[e.getData().path.length-1].h;
        endDetect = e.getData().getEndDetect();
        isReverse = e.getData().isReverse;
        transVelocity = e.getData().getVel();

        String path_string = " ";
        for(LineSegment i : targetPath) {
            path_string += String.format("from %s to %s \n",i.start.toString(),i.end.toString());
        }
        RobotLog.dd("new_path_segment", path_string);
    }

    @Override
    public void init() {
        EventBus.getListenersRegistration().invoke(new RegisterNewPositionListener(this::setPose));
    }

}
