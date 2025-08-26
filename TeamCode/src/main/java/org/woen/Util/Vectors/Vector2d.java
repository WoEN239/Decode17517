package org.woen.Util.Vectors;

import static java.lang.Math.cos;
import static java.lang.Math.sin;

public class Vector2d extends AbstractVector2d<DoubleCoordinate,DoubleCoordinate> implements ICoordinate<Vector2d> {

    public Vector2d(DoubleCoordinate x, DoubleCoordinate y) {
        super(x, y);
    }

    public Vector2d rotate(double rad){
        double y1 = y.getData() * cos(rad) - x.getData() * sin(rad);
        double x1 = y.getData() * sin(rad) + x.getData() * cos(rad);
        return new Vector2d(new DoubleCoordinate(x1),
                            new DoubleCoordinate(y1));
    }

    @Override
    public Vector2d plus(Vector2d b) {
        return new Vector2d(x.plus(b.x),
                            y.plus(b.y));
    }

    @Override
    public Vector2d minus(Vector2d b) {
        return new Vector2d(x.minus(b.x),
                            y.minus(b.y));
    }

}
