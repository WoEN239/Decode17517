package org.woen.Util.Pid;

public class PidStatus {
    public double kp;
    public double ki;
    public double kd;
    public double offset;
    public double kf;

    public double maxI;
    public double zeroBorder;
    public double errorBorder;
    public boolean isTelemetry = false;

    public void setTelemetry(boolean telemetry) {
        isTelemetry = telemetry;
    }

    public PidStatus(double kp, double ki, double kd, double kf, double offset, double maxI, double zeroBorder) {
        this.kp = kp;
        this.ki = ki;
        this.kd = kd;
        this.offset = offset;
        this.kf = kf;
        this.maxI = maxI;
        this.zeroBorder = zeroBorder;
    }
    public PidStatus(double kp, double ki, double kd, double kf, double offset, double maxI, double zeroBorder, double errorBorder) {
        this.kp = kp;
        this.ki = ki;
        this.kd = kd;
        this.offset = offset;
        this.kf = kf;
        this.maxI = maxI;
        this.zeroBorder = zeroBorder;
        this.errorBorder = errorBorder;
    }

    public void copyFrom(PidStatus status) {
        this.kp = status.kp;
        this.ki = status.ki;
        this.kd = status.kd;
        this.kf = status.kf;
        this.offset = status.offset;
        this.maxI = status.maxI;
        this.zeroBorder = status.zeroBorder;
    }
}
