package org.woen.Trajectory.Math.Spline;

import org.woen.Trajectory.TrajectoryPoint;
import org.woen.Util.Vectors.Vector2d;

public class QuinticBezierSplineSegment {

    private final QuinticBezierSplineSegmentProjection x;
    private final QuinticBezierSplineSegmentProjection y;

    private QuinticBezierSplineSegment(Vector2d start0, Vector2d start1, Vector2d start2,
                                       Vector2d end0  , Vector2d end1  , Vector2d end2   ){

        x = QuinticBezierSplineSegmentProjection.create(
                start0.x,start1.x,start2.x,
                    end0.x,end1.x,end2.x);
        
        y = QuinticBezierSplineSegmentProjection.create(
                start0.y,start1.y,start2.y,
                end0.y,end1.y,end2.y);

    }

    public static QuinticBezierSplineSegment createSpline(TrajectoryPoint start, TrajectoryPoint end){
        return new QuinticBezierSplineSegment(start.position,start.velocity,start.acceleration,
                                       end.position,end.velocity,end.acceleration);
    }

    public Vector2d get(double u){
        return new Vector2d(x.get(u),y.get(u));
    }

    public Vector2d get(double u, int d){
        return new Vector2d(x.get(u,d),y.get(u,d));
    }

}
