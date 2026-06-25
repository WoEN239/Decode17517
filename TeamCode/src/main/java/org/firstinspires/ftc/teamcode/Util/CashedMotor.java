package org.firstinspires.ftc.teamcode.Util;

import com.qualcomm.robotcore.hardware.DcMotorEx;

public class CashedMotor {
    private final DcMotorEx motor;
    private double lastPower = -2;



    public DcMotorEx getMotor() {
        return motor;
    }

    public CashedMotor(DcMotorEx motor) {
        this.motor = motor;
    }
    public void setPower(double p){
        if( Math.abs(p-lastPower)>0.01){
            motor.setPower(p);
            lastPower = p;
        }
    }

}
