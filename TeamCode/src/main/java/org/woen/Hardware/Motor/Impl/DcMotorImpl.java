package org.woen.Hardware.Motor.Impl;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit;
import org.woen.Hardware.Motor.Inter.Motor;

public class DcMotorImpl implements Motor {
    protected DcMotorEx dcMotorEx;

    public DcMotorImpl(DcMotorEx motor) {
        this.dcMotorEx = motor;
    }



    @Override
    public double getPosition() {
        return dcMotorEx.getCurrentPosition();
    }

    /// do we need getPosition()?,i think nah

    @Override
    public void setPower(double v) {
        dcMotorEx.setPower(v);
    }

    @Override
    public void setDirection(DcMotorSimple.Direction direction) {
        dcMotorEx.setDirection(direction);
    }

    @Override
    public double getVel() {
        return dcMotorEx.getVelocity();
    }

    @Override
    public double getCurrent() {
        return dcMotorEx.getCurrent(CurrentUnit.AMPS);
    }

    @Override
    public void reset() {
        dcMotorEx.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
        dcMotorEx.setMode(DcMotorEx.RunMode.RUN_WITHOUT_ENCODER);
    }


    @Override
    public void setZeroPowerBehavior(com.qualcomm.robotcore.hardware.DcMotor.ZeroPowerBehavior m) {
        dcMotorEx.setZeroPowerBehavior(m);
    }

}
