package org.woen.Trajectory;

import static org.woen.Trajectory.Math.Spline.QuinticBezierSplineSegment.createSpline;

import org.woen.Trajectory.Config.RobotDriveConstants;
import org.woen.Trajectory.Math.Spline.QuinticBezierSplineSegment;
import org.woen.Trajectory.Math.TrajectorySegment.TrajectorySegment;
import org.woen.Util.Vectors.Vector2d;

import java.util.ArrayList;
import java.util.Arrays;

public class Trajectory {
    private final ArrayList<TrajectorySegment> trajectorySegments = new ArrayList<>();
    private final ArrayList<Double> trajectorySegmentsDuration = new ArrayList<>();

    private void computeDurations(){
        double currentDuration = 0;
        for (int i = 0; i<trajectorySegments.size()-1; i++) {
            trajectorySegmentsDuration.set(i,trajectorySegments.get(i).getDuration()+currentDuration);
        }
    }

    private void manualAddSingle(TrajectoryPoint startSpline, TrajectoryPoint endSpline,
                              double startVelValue,double endVelValue){

        trajectorySegments.add(TrajectorySegment.createTrajectorySegment(
        createSpline(startSpline,endSpline),startVelValue,endVelValue,RobotDriveConstants.getDefault()
        ));
        computeDurations();
    }

    private void manualAdd(TrajectorySegment... segments){
        trajectorySegments.addAll(Arrays.asList(segments));
        computeDurations();
    }

    public Vector2d getPosition(double time){
        int index = Arrays.binarySearch(trajectorySegmentsDuration.toArray(),time);

        if(time>=trajectorySegmentsDuration.get(trajectorySegmentsDuration.size()-1)){
            return trajectorySegments.get(trajectorySegments.size()-1).getPosition(
                    trajectorySegments.get(trajectorySegments.size()-1).getDuration()
            );
        }

        if(index >= 0){
            return trajectorySegments.get(index+1).getPosition(0);
        };

        int loIndex = -(index + 1) - 1;
        return trajectorySegments.get(loIndex+1).getPosition(time-trajectorySegmentsDuration.get(loIndex));
    }

    public Vector2d getVelocity(double time){
        int index = Arrays.binarySearch(trajectorySegmentsDuration.toArray(),time);

        if(time>=trajectorySegmentsDuration.get(trajectorySegmentsDuration.size()-1)){
            return trajectorySegments.get(trajectorySegments.size()-1).getVelocity(
                    trajectorySegments.get(trajectorySegments.size()-1).getDuration()
            );
        }

        if(index >= 0){
            return trajectorySegments.get(index+1).getVelocity(0);
        };

        int loIndex = -(index + 1) - 1;
        return trajectorySegments.get(loIndex+1).getVelocity(time-trajectorySegmentsDuration.get(loIndex));
    }

    public double getAngularVelocity(double time){
        int index = Arrays.binarySearch(trajectorySegmentsDuration.toArray(),time);

        if(time>=trajectorySegmentsDuration.get(trajectorySegmentsDuration.size()-1)){
            return trajectorySegments.get(trajectorySegments.size()-1).getAngularVelocity(
                    trajectorySegments.get(trajectorySegments.size()-1).getDuration()
            );
        }

        if(index >= 0){
            return trajectorySegments.get(index+1).getAngularVelocity(0);
        };

        int loIndex = -(index + 1) - 1;
        return trajectorySegments.get(loIndex+1).getAngularVelocity(time-trajectorySegmentsDuration.get(loIndex));
    }

    public Trajectory() {}
}