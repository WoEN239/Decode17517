package org.woen.Hardware.Devices.Servo;

import static java.lang.Math.sqrt;

public class ServoMotion {

    private final double acceleration;

    private final double maxVel;
    private final double target;

    private final double t1;

    private final double t2;
    private final double t3;


    public ServoMotion(double acceleration, double maxVel, double target) {
        this.acceleration = acceleration;
        this.maxVel = maxVel;
        this.target = target;



        double accelTime = maxVel/acceleration;
        double accelLength = (accelTime*accelTime * acceleration)/2.0;
        double lengthWithoutAccel = target - accelLength*2;

        if(target< accelLength*2 + lengthWithoutAccel){
            t1 = t3 = accelLength;
            t2 = (target - 2 * accelTime)/maxVel;
        }
        else{
           t1 = t3 = sqrt(2 * target / acceleration);
           t2 = 0;
           maxVel = (2 * target) / t1 ;
        }

    }

    public double getPos(double t){
        if(t<t1){
            return t*t*acceleration*0.5;
        }else
            if(t<t2){
            return (t-t1)*maxVel;
        }else{
                if(t < t3){
                    return -(t-t2)*(t - t2)*acceleration*0.5 + (t-t2)*maxVel;
                }
            return 0;
        }
    }
}
