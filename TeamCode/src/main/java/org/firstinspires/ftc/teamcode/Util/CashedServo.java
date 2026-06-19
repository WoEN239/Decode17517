package org.firstinspires.ftc.teamcode.Util;

import com.qualcomm.robotcore.hardware.Servo;

public class CashedServo {
    private final Servo servo;
    private double lastPosition = -1;

    public Servo getServo() {
        return servo;
    }

    public CashedServo(Servo servo) {
        this.servo = servo;
    }
    public void setPosition(double p){
        if( Math.abs(p-lastPosition)>0.025){
            servo.setPosition(p);
        }
        lastPosition = p;
    }

}
