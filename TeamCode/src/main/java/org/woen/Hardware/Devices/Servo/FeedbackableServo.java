package org.woen.Hardware.Devices.Servo;

import static java.lang.Math.abs;

import com.qualcomm.robotcore.util.ElapsedTime;

import org.woen.Hardware.Devices.Servo.Interface.ServoMotor;
import org.woen.Util.MotionProfile.TrapezoidMotionProfile;

public class FeedbackableServo {
    private final ElapsedTime timer = new ElapsedTime();
    private final ServoMotor servo;
    public TrapezoidMotionProfile motionProfile = new TrapezoidMotionProfile(accel,velocity,0,0,0);
    private final static double accel = 12;
    private final static double velocity = 20;

    public void setTarget(double pos){
    //    if(abs(pos-motionProfile.getPos(motionProfile.duration+1)) < 0.015) return;

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

    public FeedbackableServo(ServoMotor servo) {
        this.servo = servo;
    }
    public FeedbackableServo(ServoMotor servo,double pos) {
        this.servo = servo;
        servo.setPos(pos);

        motionProfile = new TrapezoidMotionProfile(accel,velocity,pos,pos,0);
    }

}
