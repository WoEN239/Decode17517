package org.woen.Util.MotionProfile;
public class TrapezoidMotionProfile {
    private final double accel;
    private double maxVel;
    private final double startVel;
    private final double targetDisp;
    private final double targetPos;
    private final double startPos;

    private final double t1;
    private final double t2;
    public  final double duration;
    private double accelLength;

    public TrapezoidMotionProfile(double accel, double maxVel,double targetPos,double pos, double vel) {
        this.targetPos = targetPos;
        this.startPos = pos;
        this.targetDisp = targetPos-pos;
        final double direction = Math.signum(targetDisp);

        this.accel = accel*direction;
        this.maxVel = maxVel*direction;

        this.startVel = vel;

        double accelTime = Math.abs((this.maxVel-vel)/this.accel);
        accelLength = accelTime*vel + accelTime*accelTime*this.accel*0.5;

        if(Math.abs(targetDisp) > Math.abs(accelLength)*2d){
            t1 = accelTime;
            if(this.maxVel!=0) {
                t2 = Math.abs((targetDisp - accelLength * 2d) / this.maxVel) + accelTime;
                duration = t2 + accelTime;
            }else{
                t2 = 0;
                duration = 0;
            }
        }else{
            t1 = Math.abs((-vel+Math.sqrt(vel*vel+2d*this.accel*targetDisp *0.5))/this.accel);
            this.maxVel = startVel + this.accel*t1;
            accelLength = startVel*t1 + this.accel*t1*t1*0.5;
            t2 = t1;
            duration = 2d*t1;
        }
    }

    public TrapezoidMotionProfile reset(double targetPos,double pos, double vel){
        return new TrapezoidMotionProfile(accel,maxVel,targetPos,pos,vel);
    }

    public double getPos(double t){
        if(t<t1){
            return startPos + t*t*accel*0.5 + startVel*t;
        }else if(t>t1 && t<t2){
            return startPos + accelLength + (t-t1)*maxVel;
        }else if(t<duration){
            return startPos+accelLength + (t2-t1)*maxVel  + maxVel*(t-t2) - accel*0.5*(t-t2)*(t-t2);
        }else{
            return targetPos;
        }
    }

    public double getVel(double t){
        if(t<t1){
            return startVel+accel*t;
        }else if(t>t1 && t<t2){
            return maxVel;
        }else if(t<duration){
            return maxVel - accel*(t-t2);
        }else{
            return 0;
        }
    }
    public double getAccel(double t){
        if(t<t1){
            return accel;
        }else if(t>t1 && t<t2){
            return 0;
        }else if(t<duration){
            return -accel;
        }else{
            return 0;
        }
    }


}
