package org.woen.Util.Vectors;

import static java.lang.Math.cos;
import static java.lang.Math.sin;


public class Vector2d  {

    public final double y;
    public final double x;

    public Vector2d rotate(double rad){
        double y1 = y * cos(rad) - x * sin(rad);
        double x1 = y * sin(rad) + x * cos(rad);
        return new Vector2d(x1,
                            y1);
    }


    public Vector2d plus(Vector2d b) {
        return new Vector2d(x+b.x,
                            y+b.y);
    }


    public Vector2d minus(Vector2d b) {
        return new Vector2d(x - b.x,
                            y - b.y);
    }

    public Vector2d multiply(double b) {
        return new Vector2d(x*b,
                            y*b);
    }

    public Vector2d() {
        x = 0;
        y = 0;
    }

    public Vector2d(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Vector2d(Vector2d vector2d) {
        x = vector2d.x;
        y = vector2d.y;
    }

    public double length(){
        return Math.sqrt(x*x+y*y);
    }

    public double lengthSquare(){
        return x*x + y*y;
    }
}
