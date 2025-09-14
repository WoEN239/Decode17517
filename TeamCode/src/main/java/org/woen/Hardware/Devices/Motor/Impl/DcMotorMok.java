package org.woen.Hardware.Devices.Motor.Impl;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.woen.Hardware.Devices.Motor.Interface.Motor;
import org.woen.Telemetry.Configs.Provider;

public class DcMotorMok implements Motor {

    Provider<Double> motorPos;
    Provider<Double> motorVol;

    public DcMotorMok(Provider<Double> motorPos, Provider<Double> motorVol){
        this.motorPos = motorPos;
        this.motorVol = motorVol;
    }

    @Override
    public double getPosition(){
       return motorPos.get();
    }

    /// do we need getPosition()?,i think nah

    @Override
    public void setPower(double power){

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
        return motorVol.get();
    }
    @Override
    public void reset(){

    }

    @Override
    public void setZeroPowerBehavior(DcMotor.ZeroPowerBehavior m) {

    }

}
