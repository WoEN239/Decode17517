package org.woen.Util.Vectors;

public class Pose {
    public final double h;
    public final Vector2d vector;

    public Pose(double h, Vector2d vector) {
        this.h = h;
        this.vector = vector;
    }

    public Pose plus(Pose b){
        return new Pose(h+b.h,
                        vector.plus(b.vector));

    }

    public Pose minus(Pose b){
        return new Pose(h-b.h,
                vector.minus(b.vector));
    }

}
