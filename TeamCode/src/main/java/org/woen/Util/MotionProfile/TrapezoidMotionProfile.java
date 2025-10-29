package org.woen.Util.MotionProfile;

import static java.lang.Math.signum;

public class TrapezoidMotionProfile {
    private final double accel1;
    private final double accel2;
    private double maxVel;
    private final double startVel;
    private final double targetDisp;
    private final double targetPos;
    private final double startPos;

    private final double t1;
    private final double t2;
    public  final double duration;
    private double accelLength1;
    private double accelLength2;

    public TrapezoidMotionProfile(double accel, double maxVel,double targetPos,double pos, double vel) {
        this.targetPos = targetPos;
        this.startPos = pos;
        this.targetDisp = targetPos-pos;
        final double direction = signum(targetDisp);

        this.startVel = vel;
        this.maxVel = maxVel*direction;

        this.accel1 = accel*signum(this.maxVel-this.startVel);
        this.accel2 = accel*signum(this.maxVel);


        double accelTime = Math.abs((this.maxVel-this.startVel)/this.accel1);
        accelLength1 = accelTime*this.startVel + accelTime*accelTime*this.accel1*0.5;
        accelLength2 = accelTime*this.maxVel   + accelTime*accelTime*this.accel2*0.5;

        if(Math.abs(targetDisp) > (Math.abs(accelLength1)+Math.abs(accelLength2)) ){
            t1 = accelTime;
            if(this.maxVel!=0) {
                t2 = Math.abs((targetDisp - accelLength1 * 2d) / this.maxVel) + accelTime;
                duration = t2 + accelTime;
            }else{
                t2 = 0;
                duration = 0;
            }
        }else {
            if (signum(accel1) != signum(accel2)) {
                t1 = Math.abs((-startVel + Math.sqrt(startVel * startVel + 2d * this.accel1 * targetDisp * 0.5)) / this.accel1);
                this.maxVel = startVel + this.accel1 * t1;
                accelLength1 = startVel * t1 + this.accel1 * t1 * t1 * 0.5;
                t2 = t1;
                duration = 2d * t1;
            }else{
                t1 = 0;
                t2 = 0;
                duration = 0;
            }
        }
    }

    public TrapezoidMotionProfile reset(double targetPos,double pos, double vel){
        return new TrapezoidMotionProfile(Math.abs(accel1),Math.abs(maxVel),targetPos,pos,vel);
    }

    public double getPos(double t){
        double posAtT1 = startPos + t1 * t1 * accel1 * 0.5 + startVel * t1;
        if(t<t1){
            return startPos + t*t* accel1 *0.5 + startVel*t;
        }else if(t>t1 && t<t2){
            return posAtT1 + (t-t1)*maxVel;
        }else if(t<duration){
            return (posAtT1 + (t2-t1)*maxVel) + maxVel*(t-t2) + accel2 *0.5*(t-t2)*(t-t2);
        }else{
            return targetPos;
    }
    }


    public double getVel(double t){
        if(t<t1){
            return startVel + accel1*t;
        }else if(t>t1 && t<t2){
            return startVel + accel1*t1;
        }else if(t<duration){
            return startVel + accel1*t1 + accel2 *(t-t2);
        }else{
            return 0;
        }
    }
    public double getAccel(double t){
        if(t<t1){
            return accel1;
        }else if(t>t1 && t<t2){
            return 0;
        }else if(t<duration){
            return accel2;
        }else{
            return 0;
        }
    }



}
