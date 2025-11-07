package org.woen.Util.Vectors;

import android.annotation.SuppressLint;

public class Pose {
    public final double h;
    public final double x;
    public final double y;
    public final Vector2d vector;

    public Pose(double h, Vector2d vector) {
        this.h = h;
        this.x = vector.x;
        this.y = vector.y;
        this.vector = vector;
    }

    public Pose(double h, double x,double y) {
        this.h = h;
        this.x = x;
        this.y = y;
        this.vector = new Vector2d(x,y) ;
    }

    public Pose plus(Pose b){
        return new Pose(h+b.h,
                        vector.plus(b.vector));

    }

    public Pose minus(Pose b){
        return new Pose(h-b.h,
                vector.minus(b.vector));
    }
    public Pose unaryMinus(){
        return new Pose(-h,new Vector2d(-vector.x, -vector.y));
    }

    @SuppressLint("DefaultLocale")
    @Override
    public String toString() {
        return "h:" + String.format("%.3f",h) + " x:" + String.format("%.3f",vector.x) + " y:" + String.format("%.3f",vector.y);
    }
}
