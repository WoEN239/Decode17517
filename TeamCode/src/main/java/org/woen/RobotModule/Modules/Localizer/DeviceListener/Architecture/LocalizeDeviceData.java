package org.woen.RobotModule.Modules.Localizer.DeviceListener.Architecture;

public class LocalizeDeviceData {
    public double rightOdPos = 0;
    public double leftOdPos  = 0;
    public double sideOdPos  = 0;

    public double rightOdVel = 0;
    public double leftOdVel  = 0;
    public double sideOdVel  = 0;

    public LocalizeDeviceData(double rightOdPos, double leftOdPos, double sideOdPos,
                              double rightOdVel, double leftOdVel, double sideOdVel) {
        this.rightOdPos = rightOdPos;
        this.leftOdPos = leftOdPos;
        this.sideOdPos = sideOdPos;

        this.rightOdVel = rightOdVel;
        this.leftOdVel = leftOdVel;
        this.sideOdVel = sideOdVel;
    }

    public LocalizeDeviceData() {}
}
