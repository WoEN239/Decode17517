package org.woen.Hardware.Devices.Servo.Impls;

import static java.lang.Math.abs;

import com.acmerobotics.dashboard.FtcDashboard;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.woen.Hardware.Devices.Servo.ServoMotion;
import org.woen.Util.MotionProfile.TrapezoidMotionProfile;


public class ServoImpl implements org.woen.Hardware.Devices.Servo.Inter.Servo {



    ElapsedTime t = new ElapsedTime();


    Servo servo;


    private double accel;
    private double maxVel;


    public ServoImpl(Servo servo, double accel, double maxVel) {
        this.servo = servo;
        this.accel = accel;
        this.maxVel = maxVel;

        t.reset();

    }


    private double oldTarget = 0d;

    double target = 0;
    public ServoMotion servoMotion = new ServoMotion(accel, maxVel, 0, 0);

    @Override
    public void setPos(double target, double startPos) {
        if (!isItTarget() && target != oldTarget) {
            servoMotion = new ServoMotion(accel, maxVel, target, startPos);
            this.target = target;
            setMotionPos();
        }
        else{
            t.reset();
        }
    }

    private void setMotionPos() {
        servo.setPosition(servoMotion.getPos(t.milliseconds()));
        FtcDashboard.getInstance().getTelemetry().addData("pos", servoMotion.getPos(t.milliseconds()));
        FtcDashboard.getInstance().getTelemetry().update();
    }


    public void resetTimer(){
        t.reset();
    }

    @Override
    public boolean isItTarget() {
        if (abs(servoMotion.t3 + servoMotion.t2 + servoMotion.t1) > t.milliseconds())
            return false;
        else {
            oldTarget = target;
            return true;
        }
    }
}
