package org.woen.RobotModule.Modules.Camera;

public class Roi {
    public double left;
    public double top;
    public double right;
    public double bottom;

    public Roi(double left, double top, double right, double bottom) {
        this.left = left;
        this.top = top;
        this.right = right;
        this.bottom = bottom;
    }
}
