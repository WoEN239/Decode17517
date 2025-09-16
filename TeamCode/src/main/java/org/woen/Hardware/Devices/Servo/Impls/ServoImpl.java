package org.woen.Hardware.Devices.Servo.Impls;

import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.woen.Hardware.Devices.Servo.ServoMotion;
import org.woen.Telemetry.ConfigurableVariables.Provider;

import java.util.EnumSet;

public class ServoImpl implements org.woen.Hardware.Devices.Servo.Inter.Servo {

    ServoMotion servoMotion;

    ElapsedTime t = new ElapsedTime();


    Servo servo;


    private double accel;
    private double maxVel;


    public ServoImpl(Servo servo, double accel, double maxVel) {
        this.servo = servo;
        this.accel = accel;
        this.maxVel = maxVel;

    }

    @Override
    public void setPos(double target, double startPos) {
        servoMotion = new ServoMotion(accel, maxVel, target, startPos);
        if (!isItTarget()) {
            setMotionPos();
        } else {
            if (servoMotion.getPos(t.milliseconds()) == target)
                t.reset();
        }
    }

    private void setMotionPos() {
        servo.setPosition(servoMotion.getPos(t.milliseconds()));
    }

    @Override
    public boolean isItTarget() {
        if (servoMotion.t3 > t.milliseconds())
            return false;
        else {
            return true;
        }
    }
}
