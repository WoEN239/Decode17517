package org.woen.Math.Spline;

public class QuinticBezierSplineSegmentProjection {
    private final double a;
    private final double b;
    private final double c;
    private final double d;
    private final double e;
    private final double f;

    private QuinticBezierSplineSegmentProjection(double a, double b, double c, double d, double e, double f) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
        this.e = e;
        this.f = f;
    }

    private static QuinticBezierSplineSegmentProjection createFromPoints(double v0, double v1, double v2, double v3, double v4, double v5){
        return new QuinticBezierSplineSegmentProjection(
                                      -v0+5*v1-10*v2+10*v3-5*v4+v5 ,
                                       5*v0-20*v1+30*v2-20*v3+5*v4,
                                         -10*v0+30*v1-30*v2+10*v3,
                                            10*v0-20*v1+10*v2,
                                                -5*v0+5*v1,
                                                       v0);
    }

    public static QuinticBezierSplineSegmentProjection create(double s0, double s1, double s2, double e0, double e1, double e2){

        double p0 = s0;
        double p1 = 0.2*s1+p0;
        double p2 = 0.05*s2+2*p1-p0;

        double p5 = e0;
        double p4 = p5-0.2*e1;
        double p3 = 0.05*e2+2*p4-p5;

        return createFromPoints(p0, p1, p2, p3, p4, p5);
    }

    public double get(double u){
        if(u>1){
            u = 1;
        }

        if(u<0){
            u = 0;
        }

        return ((((a*u+b)*u+c)*u+d)*u+e)*u+f;

    }

    public double get(double u,int d){
        if(d == 0){
            return get(u);
        }

        if(d == 1){
            return (((5.0 * a * u + 4.0 * b) * u + 3.0 * c) * u + 2.0 * d) * u + e;
        }

        if(d == 2){
            return ((20.0 * a * u + 12.0 * b) * u + 6.0 * c) * u + 2.0 * d;
        }

        throw new RuntimeException("incorrect derivative power");

    }
}
