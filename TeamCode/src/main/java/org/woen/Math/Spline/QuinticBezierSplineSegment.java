package org.woen.Math.Spline;

import org.woen.Math.Point.Point;

public class QuinticBezierSplineSegment {
    private QuinticBezierSplineSegmentProjection x;
    private QuinticBezierSplineSegmentProjection y;

    private QuinticBezierSplineSegment(Point start0,Point start1,Point start2,
                                                     Point end0,  Point end1  ,Point end2  ){

        x = QuinticBezierSplineSegmentProjection.create(
                start0.x,start1.x,start2.x,
                    end0.x,end1.x,end2.x);
        
        y = QuinticBezierSplineSegmentProjection.create(
                start0.y,start1.y,start2.y,
                end0.y,end1.y,end2.y);
    }
}
