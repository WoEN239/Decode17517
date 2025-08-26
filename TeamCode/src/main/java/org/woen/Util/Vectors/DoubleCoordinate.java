package org.woen.Util.Vectors;

public class DoubleCoordinate implements ICoordinate<DoubleCoordinate> {
    private final double data;

    public DoubleCoordinate(double data) {
        this.data = data;
    }

    public double getData() {
        return data;
    }

    @Override
    public DoubleCoordinate plus(DoubleCoordinate b) {
        return new DoubleCoordinate(b.data + data);
    }

    @Override
    public DoubleCoordinate minus(DoubleCoordinate b) {
        return new DoubleCoordinate(b.data - data);
    }


}
