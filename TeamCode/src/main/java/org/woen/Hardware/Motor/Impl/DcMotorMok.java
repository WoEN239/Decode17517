package org.woen.Hardware.Motor.Impl;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.woen.Hardware.Motor.Inter.Motor;

public class DcMotorMok implements Motor {

    Double motorPos = 0d;

    Double motorVol = 0d;

    public DcMotorMok(Double motorPos, Double motorVol){
        this.motorPos = motorPos;
        this.motorVol = motorVol;
    }

    @Override
    public double getPosition(){
       return motorPos;
    }

    /// do we need getPosition()?,i think nah

    @Override
    public void setPower(double v){

    }

    @Override
    public void setDirection(DcMotorSimple.Direction direction) {

    }

    @Override
    public double getVel() {
        return 0d;
    }
    @Override
    public double getCurrent(){
        return motorVol;
    }
    @Override
    public void reset(){

    }

    @Override
    public void setZeroPowerBehavior(DcMotor.ZeroPowerBehavior m) {

    }

}
