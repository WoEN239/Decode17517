package org.woen.Hardware.DevicePool.Devices.Motor.Impl;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.woen.Hardware.DevicePool.Devices.Motor.Interface.Motor;
import org.woen.Telemetry.ConfigurableVariables.SimpleProvider;

public class DcMotorMok implements Motor {

    SimpleProvider<Double> motorPos;
    SimpleProvider<Double> motorVol;

    public DcMotorMok(SimpleProvider<Double> motorPos, SimpleProvider<Double> motorVol){
        this.motorPos = motorPos;
        this.motorVol = motorVol;
    }

    @Override
    public double getPosition(){
       return motorPos.get();
    }



    @Override
    public void setPower(double power){

    }

    @Override
    public void setDir(int dir) {

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
