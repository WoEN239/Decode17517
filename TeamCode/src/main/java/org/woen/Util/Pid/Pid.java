package org.woen.Util.Pid;

import static java.lang.Math.abs;
import static java.lang.Math.signum;

import com.qualcomm.robotcore.util.ElapsedTime;

import org.woen.Telemetry.Telemetry;
import org.woen.Util.Angel.AngleUtil;

public class Pid {
    private final PidStatus status;

    public Pid(PidStatus status) {
        this.status = status;
    }

    public String name;

    public boolean isNormolized = false;
    public boolean isDAccessible = false;

    public void setName(String n) {
        name = n;
    }

    private double tLast = (double) System.nanoTime() / (double) ElapsedTime.SECOND_IN_NANO;
    private double errLast = 0;
    private double P = 0;
    private double I = 0;
    private double D = 0;
    private double F = 0;
    private double u = 0;
    private double target = 0;
    private double targetD = 0;
    private double pos = 0;
    private double posD = 0;

    public void setPos(double pos) {
        this.pos = pos;
    }

    public void setTarget(double target) {
        this.target = target;
    }

    public void setTargetD(double targetD){
        this.targetD = targetD;
    }

    public void setPosD(double posD){
        this.posD = posD;
    }

    public double getU() {
        return u;
    }

    public void update() {
        calc();

        if (status.isTelemetry) {
            Telemetry.getInstance().add(name + " P", P);
            Telemetry.getInstance().add(name + " I", I);
            Telemetry.getInstance().add(name + " D", D);
            Telemetry.getInstance().add(name + " F", F);
            Telemetry.getInstance().add(name + " Target", target);
            Telemetry.getInstance().add(name + " position", pos);
            Telemetry.getInstance().add(name + " err", err);
            Telemetry.getInstance().add(name + " pidU", u);
        }
    }

    private double err = 0;

    private void calc() {
        err = target - pos;

        if(isNormolized){
            err = AngleUtil.normalize(err);
        }

        double dErr = err - errLast;
        errLast = err;

        double tNow = (double) System.nanoTime() / (double) ElapsedTime.SECOND_IN_NANO;
        double dt = tNow - tLast;
        tLast = tNow;

        P = status.kp * err;
        I += status.ki * err * dt;

        D = status.kd * dErr / dt;
        if(isDAccessible){
            D = status.kd*(targetD-posD);
        }
        F = status.kf*target+status.offset*signum(err);

        if(abs(err) < status.errorBorder){
            P=0;
        }

        if (abs(I) > status.maxI) {
            I = status.maxI * signum(I);
        }
        double u = P + I + D + F;

        if (abs(u) < status.zeroBorder) {
            u = 0;
        }

        this.u = u;
    }
}
