package org.woen.Util.MotionProfile;

public class TrapezoidMotionProfile {
    private final double accel;
    private final double maxVel;
    private final double startVel;
    private final double target;

    private double t1;
    private double t2;
    private double duration;

    public TrapezoidMotionProfile(double accel, double maxVel,double targetPos,double pos, double vel) {
        this.accel = accel;
        this.maxVel = maxVel;
        this.target = targetPos-pos;
        this.startVel = vel;

        double accelTime = (maxVel-vel)/accel;
        double accelLength = accelTime*vel + accelTime*accelTime * accel*0.5;
        if(target<accelLength*2){
            t1 = accelLength;
            t2 = (target-accelTime*2)/maxVel+accelTime;
        }else{
            t1 = (-vel+Math.sqrt(vel*vel+2*accel*target*0.5))/accel;
            t2 = t1;
        }
    }

    public double getPos(double t){
        if(t<t1){
            return t*t*accel*0.5+startVel*t;
        }else if(t>t1 && t<t2){
            return t*t*accel*0.5+startVel*t + (t-t1)*maxVel;
        }else{
            return t*t*accel*0.5+startVel*t + (t-t1)*maxVel + (t-t2)*maxVel - accel*0.5*(t-t2)*(t-t2);
        }
    }

    public double getVel(double t){
        if(t<t1){
            return startVel+accel*t;
        }else if(t>t1 && t<t2){
            return maxVel;
        }else{
            return maxVel - accel*t;
        }
    }


}
