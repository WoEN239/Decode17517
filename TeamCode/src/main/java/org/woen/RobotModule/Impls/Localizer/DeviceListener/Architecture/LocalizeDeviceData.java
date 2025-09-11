package org.woen.RobotModule.Impls.Localizer.DeviceListener.Architecture;

public class LocalizeDeviceData {
    public double rightOdPos = 0;
    public double leftOdPos  = 0;
    public double sideOdPos  = 0;
    public double gyroPos    = 0;

    public double rightOdVel = 0;
    public double leftOdVel  = 0;
    public double sideOdVel  = 0;
    public double gyroVel    = 0;

    public LocalizeDeviceData(double rightOdPos, double leftOdPos, double sideOdPos, double gyroPos,
                              double rightOdVel, double leftOdVel, double sideOdVel, double gyroVel) {
        this.rightOdPos = rightOdPos;
        this.leftOdPos = leftOdPos;
        this.sideOdPos = sideOdPos;
        this.gyroPos = gyroPos;
        this.rightOdVel = rightOdVel;
        this.leftOdVel = leftOdVel;
        this.sideOdVel = sideOdVel;
        this.gyroVel = gyroVel;
    }

    public LocalizeDeviceData() {}
}
