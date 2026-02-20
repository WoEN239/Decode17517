package org.woen.Util.Trajectory.Math.Line;

import static java.lang.Math.cos;
import static java.lang.Math.sin;

import androidx.annotation.NonNull;
import org.woen.Util.Vectors.Vector2d;

public class LineSegment {

    public final Vector2d start;
    public final Vector2d end;
    public  final double lineAngle;
    public  final Vector2d unitVector;
    private final double length;
    public double getLength() {return length;}
    private final double kX;
    private final double kY;
    private final double c;

    public static LineSegment lineFromTwoPoint(Vector2d start, Vector2d end) {
        return new LineSegment(start.x, start.y, end.x, end.y);
    }

    public LineSegment(double x1, double y1, double x2, double y2){
        double kX = y2 - y1;
        double kY = -(x2 - x1);
        double c =  -(kY*y1 + kX*x1) ;

        double h = Math.atan2(x2 - x1, y2 - y1);

        Vector2d start = new Vector2d(x1,y1);
        Vector2d end   = new Vector2d(x2,y2);

        Vector2d unitVector = new Vector2d(sin(h), cos(h));

        double length = Math.sqrt((x2-x1)*(x2-x1)+(y2-y1)*(y2-y1));

        this.start = start;
        this.end = end;
        this.lineAngle = h;
        this.unitVector = unitVector;
        this.length = length;
        this.kX = kX;
        this.kY = kY;
        this.c = c;
    }

    public static LineSegment createFromOnePoint(Vector2d s, double h, double length){
        Vector2d dispVector = new Vector2d(sin(h), cos(h));
        Vector2d e = s.plus(dispVector).multiply(length);
        return lineFromTwoPoint(s,e);
    }

    public Vector2d findProjection(Vector2d p){
        double kX1 = -kY;
        double kY1 = kX;
        double c1 =  -(kX1*p.x + kY1*p.y);

        double x = 0;
        double y = 0;

        x =  (kY1*c-c1*kY) / (kX1*kY - kY1*kX);
        y =  (c1*kX-kX1*c) / (kX1*kY - kY1*kX);


        if(Double.isNaN(x) || Double.isNaN(y)){
            return new Vector2d(p);
        }

        return new Vector2d(x,y);
    }

    @NonNull
    @Override
    public String toString(){
        return "from " + start.toString() + " to " + end.toString();
    }
}
