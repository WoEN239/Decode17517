package org.woen.Hardware.DevicePool.Devices.Servo;

import static java.lang.Math.abs;

import com.qualcomm.robotcore.util.ElapsedTime;

import org.woen.Hardware.DevicePool.Devices.Servo.Interface.ServoMotor;
import org.woen.Util.MotionProfile.TrapezoidMotionProfile;

public class ServoWithFeedback {
    private final ElapsedTime timer = new ElapsedTime();
    private final ServoMotor servo;
    public TrapezoidMotionProfile motionProfile = new TrapezoidMotionProfile(accel,velocity,0,0,0);
    private final static double accel = 12;
    private final static double velocity = 20;
    private double latterTarget = -1;

    public void setTarget(double pos){
        if(abs(pos-latterTarget) < 0.015) return;
        latterTarget = pos;

        motionProfile = new TrapezoidMotionProfile(accel, velocity,pos,getPos(),getVel());
        timer.reset();
    }

    public void update(){
        servo.setPos(getPos());
    }

    public double getPos(){
        return motionProfile.getPos(timer.seconds());
    }

    public double getVel(){
        return motionProfile.getVel(timer.seconds());
    }

    public boolean isAtTarget(){
        return timer.seconds() > motionProfile.duration;
    }

    public ServoWithFeedback(ServoMotor servo) {
        this.servo = servo;
    }
    public ServoWithFeedback(ServoMotor servo, double pos) {
        this.servo = servo;
        servo.setPos(pos);

        motionProfile = new TrapezoidMotionProfile(accel,velocity,pos,pos,0);
    }

}
