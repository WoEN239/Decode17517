package org.woen.RobotModule.Modules.TrajectoryFollower.Impls;

import static org.woen.Util.Trajectory.Math.Line.LineSegment.lineFromTwoPoint;

import static java.lang.Math.abs;
import static java.lang.Math.signum;
import static java.lang.Math.sqrt;

import com.qualcomm.robotcore.util.RobotLog;

import org.woen.Architecture.EventBus.EventBus;
import org.woen.Autonom.Architecture.WayPoint;
import org.woen.Config.ControlSystemConstant;
import org.woen.Config.MatchData;
import org.woen.RobotModule.Modules.Localizer.Architecture.RegisterNewPositionListener;
import org.woen.RobotModule.Modules.Localizer.Architecture.RegisterNewVelocityListener;
import org.woen.RobotModule.Modules.TrajectoryFollower.Arcitecture.Feedback.FeedbackReference;
import org.woen.RobotModule.Modules.TrajectoryFollower.Arcitecture.Feedback.FeedbackReferenceObserver;
import org.woen.RobotModule.Modules.TrajectoryFollower.Arcitecture.Feedback.TankFeedbackReference;
import org.woen.RobotModule.Modules.TrajectoryFollower.Arcitecture.Feedback.TankFeedbackReferenceObserver;
import org.woen.RobotModule.Modules.TrajectoryFollower.Arcitecture.Feedforward.FeedforwardReference;
import org.woen.RobotModule.Modules.TrajectoryFollower.Arcitecture.Feedforward.FeedforwardReferenceObserver;
import org.woen.RobotModule.Modules.TrajectoryFollower.Arcitecture.TargetSegment.SetNewTargetTrajectorySegmentEvent;
import org.woen.RobotModule.Modules.TrajectoryFollower.Interface.TrajectoryFollower;
import org.woen.Telemetry.Telemetry;
import org.woen.Util.Trajectory.Math.Line.LineSegment;
import org.woen.Util.Vectors.Pose;
import org.woen.Util.Vectors.Vector2d;

import java.util.ArrayList;
import java.util.function.Supplier;

public class PurePursuitFollowerImpl implements TrajectoryFollower {

    @Override
    public void update() {
        if(targetPath.isEmpty()) {
            return;
        }

        //define variables for algorithm
        double localRadius = wayPoint.getLookAheadRadius();
        double transVelocity = wayPoint.getVel();
        double endDetect = wayPoint.getEndDetect();

        Vector2d lastPoint = targetPath.get(targetPath.size()-1).end;
        double distanceToEnd = pose.vector.minus(lastPoint).length();

        if(distanceToEnd < localRadius){
            localRadius = distanceToEnd;
        }

        //define current projection and target segment
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

        //pure pursuit magic for define screw radius
        double chord = localRadius;
        double step = sqrt(localRadius*localRadius - projection.minus(pose.vector).lengthSquare());
        if(Double.isNaN(step)) {
            step = localRadius;
            chord = projection.plus(targetSegment.unitVector.multiply(step)).minus(pose.vector).length();
        }

        Vector2d virtualTarget = projection.plus(targetSegment.unitVector.multiply(step));
        double y = virtualTarget.minus(pose.vector).rotate(-pose.h).y;
        double screwR = (chord*chord)/(2d*y);

        //define velocity command
        if(isSegmentLast) transVelocity = Double.min(transVelocity,sqrt(2*ControlSystemConstant.feedforwardConfig.maxPPAccel*distanceToEnd));



        if(wayPoint.isReverse){
            transVelocity = -abs(transVelocity);
        }
        double angleVelocity = (transVelocity/screwR);

        //send velocity command to control loop
        if(pose.vector.minus(lastPoint).lengthSquare() < endDetect*endDetect && isSegmentLast){
            isEndNear = true;
        }

        if(isEndNear){
            angleVelocity = 0;
            transVelocity = 0;
        }

        observerVel.notifyListeners(new FeedforwardReference(
                        new Pose(angleVelocity,transVelocity,0),
                        new Pose(0,0,0)
                )
        );

        observerPos.notifyListeners(new TankFeedbackReference(
                isEndNear,wayPoint.getEndAngle().get()
        ));

        //telemetry output
        for(LineSegment i: targetPath) {
            Telemetry.getInstance().getField().line(i.start, i.end);
        }

        Telemetry.getInstance().getField().line(virtualTarget,pose.vector);
        Telemetry.getInstance().getField().line(pose.vector,projection);
    }
    private void setNewTrajectoryEvent(SetNewTargetTrajectorySegmentEvent e){
        if(e.getData().path.length == 0){
            targetPath = new ArrayList<>();
            return;
        }
        wayPoint = e.getData();

        ArrayList<LineSegment> buildPath = new ArrayList<>();
        buildPath.add(lineFromTwoPoint(pose.vector,e.getData().path[0].vector));
        for(int i = 0; i<e.getData().path.length-1; i++ ){
            buildPath.add(lineFromTwoPoint(e.getData().path[i].vector,e.getData().path[i+1].vector));
        }
        targetPath = buildPath;

        isEndNear = false;

        StringBuilder path_string = new StringBuilder(" ");
        for(LineSegment i : targetPath) {
            path_string.append(String.format("from %s to %s \n", new Pose(0,i.start), new Pose(0,i.end)));
        }
        path_string.append("total ").append(targetPath.size());
        RobotLog.dd("new_path_segment", path_string.toString());
    }
    private WayPoint wayPoint;
    private ArrayList<LineSegment> targetPath = new ArrayList<>();
    private boolean isEndNear = false;
    private Pose pose =  MatchData.getStartPose();
    private void setPose(Pose pose) {this.pose = pose;}
    private Pose velocity =  new Pose(0,0,0);
    private void setVelocity(Pose velocity) {this.velocity = velocity;}
    private final TankFeedbackReferenceObserver observerPos = new TankFeedbackReferenceObserver();
    private final FeedforwardReferenceObserver observerVel = new FeedforwardReferenceObserver();
    @Override
    public void subscribeInit() {
        EventBus.getInstance().subscribe(SetNewTargetTrajectorySegmentEvent.class,this::setNewTrajectoryEvent);
    }

    @Override
    public void init() {
        EventBus.getListenersRegistration().invoke(new RegisterNewPositionListener(this::setPose));
        EventBus.getListenersRegistration().invoke(new RegisterNewVelocityListener(this::setVelocity));
    }

}
