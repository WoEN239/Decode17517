package org.woen.Util;

public class AbstractVector2d<X extends Coordinate<X>,Y extends Coordinate<Y>> {
    protected final X x;
    protected final Y y;

    public AbstractVector2d(X x, Y y) {
        this.x = x;
        this.y = y;
    }

    public X getX() {
        return x;
    }

    public Y getY() {
        return y;
    }

    public AbstractVector2d<X,Y> plus(AbstractVector2d<X,Y> b){
        return new AbstractVector2d<>(x.plus(b.x),
                              y.plus(b.y));
    }

    public AbstractVector2d<X,Y> minus(AbstractVector2d<X,Y> b){
        return new AbstractVector2d<>(x.minus(b.x),
                              y.minus(b.y));
    }

}
